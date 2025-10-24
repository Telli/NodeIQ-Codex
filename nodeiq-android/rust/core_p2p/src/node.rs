use crate::events::NodeEvent;
use crate::protocol::QueryMessage;
use serde::{Deserialize, Serialize};
use tokio::sync::mpsc::Sender;

#[derive(Debug, Clone, Serialize, Deserialize)]
pub struct NodeConfig {
    pub namespace: String,
    pub bootstrap: Vec<String>,
}

pub struct NodeHandle {
    pub events: Sender<NodeEvent>,
}

impl NodeHandle {
    pub async fn send_query(&self, _peer: String, _query: QueryMessage) -> anyhow::Result<()> {
        Ok(())
    }
}

pub async fn start_node(_config: NodeConfig) -> anyhow::Result<NodeHandle> {
    Ok(NodeHandle {
        events: tokio::sync::mpsc::channel(16).0,
    })
}
