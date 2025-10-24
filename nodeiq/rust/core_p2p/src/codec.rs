use serde::{Deserialize, Serialize};
use thiserror::Error;

#[derive(Debug, Error)]
pub enum CodecError {
    #[error("serialization error: {0}")]
    Serialization(String),
}

#[derive(Debug, Clone, Serialize, Deserialize)]
pub struct JsonFrame<T> {
    pub version: String,
    pub r#type: String,
    pub body: T,
}

pub fn encode<T: Serialize>(frame: &JsonFrame<T>) -> Result<String, CodecError> {
    serde_json::to_string(frame).map_err(|err| CodecError::Serialization(err.to_string()))
}

pub fn decode<T: for<'de> Deserialize<'de>>(payload: &str) -> Result<JsonFrame<T>, CodecError> {
    serde_json::from_str(payload).map_err(|err| CodecError::Serialization(err.to_string()))
}
