use serde::{Deserialize, Serialize};

#[derive(Debug, Clone, Serialize, Deserialize)]
pub struct QueryMessage {
    pub version: String,
    pub query_id: String,
    pub from_peer: String,
    pub agent_id: String,
    pub query_text: String,
    pub ttl_ms: u64,
}

#[derive(Debug, Clone, Serialize, Deserialize)]
pub struct StreamMessage {
    pub query_id: String,
    pub seq: u32,
    pub delta: String,
}

#[derive(Debug, Clone, Serialize, Deserialize)]
pub struct EndMessage {
    pub query_id: String,
    pub status: String,
}
