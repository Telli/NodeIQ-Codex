use serde::{Deserialize, Serialize};

#[derive(Debug, Clone, Serialize, Deserialize)]
pub enum NodeEvent {
    Ready,
    PeerDiscovered { peer_id: String, agent_id: String, skills: Vec<String> },
    QueryReceived(QueryEvent),
    Stream(StreamEvent),
    End { query_id: String, status: String },
    Error { message: String },
}

#[derive(Debug, Clone, Serialize, Deserialize)]
pub struct QueryEvent {
    pub query_id: String,
    pub from_peer: String,
    pub agent_id: String,
    pub query_text: String,
}

#[derive(Debug, Clone, Serialize, Deserialize)]
pub struct StreamEvent {
    pub query_id: String,
    pub seq: u32,
    pub delta: Option<String>,
    pub status: String,
}
