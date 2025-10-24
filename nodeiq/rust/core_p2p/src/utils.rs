use serde::{Deserialize, Serialize};
use std::time::Duration;

#[derive(Debug, Clone, Serialize, Deserialize)]
pub struct BackoffConfig {
    pub intervals: Vec<u64>,
}

impl BackoffConfig {
    pub fn iter(&self) -> impl Iterator<Item = Duration> + '_ {
        self.intervals
            .iter()
            .copied()
            .map(Duration::from_secs)
    }
}
