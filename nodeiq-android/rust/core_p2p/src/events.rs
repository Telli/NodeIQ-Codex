use serde::{Deserialize, Serialize};

#[derive(Debug, Clone, Serialize, Deserialize)]
pub enum NodeEvent {
    Started { peer_id: String },
    QueryReceived { query_id: String, from_peer: String, text: String },
    StreamDelta { query_id: String, seq: u32, delta: String },
    StreamEnd { query_id: String, status: String },
}
