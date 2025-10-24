use crate::events::NodeEvent;
use crate::protocol::QueryMessage;
use serde::{Deserialize, Serialize};
use std::thread::JoinHandle;
use tokio::sync::mpsc::{self, Receiver, Sender};
use tracing::{info, warn};

#[derive(Debug, Clone, Serialize, Deserialize)]
pub struct NodeConfig {
    pub namespace: String,
    pub listen_on: Vec<String>,
    pub bootstrap_multiaddrs: Vec<String>,
    pub enable_relay_client: bool,
    pub discovery_interval_sec: u64,
}

pub struct Node {
    config: NodeConfig,
    tx: Sender<NodeEvent>,
    rx: Receiver<NodeEvent>,
    task: Option<JoinHandle<()>>,
}

impl Node {
    pub fn new(config: NodeConfig) -> Self {
        let (tx, rx) = mpsc::channel(32);
        Self {
            config,
            tx,
            rx,
            task: None,
        }
    }

    pub fn start(&mut self) {
        if self.task.is_some() {
            return;
        }
        let mut tx = self.tx.clone();
        let config = self.config.clone();
        self.task = Some(std::thread::spawn(move || {
            info!("nodeiq", "Node started with namespace {}", config.namespace);
            if let Err(err) = tx.blocking_send(NodeEvent::Ready) {
                warn!(?err, "failed to notify ready state");
            }
        }));
    }

    pub async fn next_event(&mut self) -> Option<NodeEvent> {
        self.rx.recv().await
    }

    pub async fn send_query(&self, query: QueryMessage) {
        let _ = self.tx.send(NodeEvent::QueryReceived(query.into())).await;
    }

    pub fn stop(&mut self) {
        if let Some(handle) = self.task.take() {
            let _ = handle.join();
        }
    }
}
