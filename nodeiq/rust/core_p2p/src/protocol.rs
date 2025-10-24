include!(concat!(env!("OUT_DIR"), "/kmarket.rs"));

use crate::events::QueryEvent;

impl From<Query> for QueryEvent {
    fn from(value: Query) -> Self {
        QueryEvent {
            query_id: value.query_id,
            from_peer: value.from_peer,
            agent_id: value.agent_id,
            query_text: value.query_text,
        }
    }
}

pub type QueryMessage = Query;
