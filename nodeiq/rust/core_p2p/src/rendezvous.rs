use serde::{Deserialize, Serialize};

#[derive(Debug, Clone, Serialize, Deserialize)]
pub struct RendezvousConfig {
    pub namespace: String,
    pub bootstrap_multiaddrs: Vec<String>,
}

pub async fn advertise(_config: &RendezvousConfig) {
    // TODO: integrate libp2p rendezvous
}

pub async fn discover(_config: &RendezvousConfig) {
    // TODO: integrate libp2p rendezvous
}
