use nodeiq_core_p2p::{node, NodeConfig};
use std::ffi::CStr;
use std::sync::Mutex;

type Handle = node::NodeHandle;

lazy_static::lazy_static! {
    static ref NODE: Mutex<Option<Handle>> = Mutex::new(None);
}

#[no_mangle]
pub extern "C" fn niq_start_node(config_json: *const i8) -> bool {
    let c_str = unsafe { CStr::from_ptr(config_json) };
    let config: NodeConfig = match serde_json::from_str(c_str.to_str().unwrap_or("{}")) {
        Ok(cfg) => cfg,
        Err(_) => return false,
    };
    let handle = futures::executor::block_on(node::start_node(config));
    match handle {
        Ok(h) => {
            *NODE.lock().unwrap() = Some(h);
            true
        }
        Err(_) => false,
    }
}

#[no_mangle]
pub extern "C" fn niq_stop_node() {
    *NODE.lock().unwrap() = None;
}

#[no_mangle]
pub extern "C" fn niq_get_peer_id() -> *const i8 {
    std::ptr::null()
}
