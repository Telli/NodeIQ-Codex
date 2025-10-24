#[derive(Debug, Clone)]
pub struct Namespace(pub String);

impl Namespace {
    pub fn rendezvous_string(&self) -> String {
        format!("nodeiq:{}", self.0)
    }
}
