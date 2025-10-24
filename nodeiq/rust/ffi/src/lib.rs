use nodeiq_core_p2p::events::NodeEvent;
use nodeiq_core_p2p::node::{Node, NodeConfig};
use once_cell::sync::OnceCell;
use serde::Deserialize;
use std::ffi::{CStr, CString};
use std::os::raw::c_char;
use std::sync::Mutex;
static NODE: OnceCell<Mutex<Node>> = OnceCell::new();
static CALLBACK: OnceCell<extern "C" fn(*const c_char)> = OnceCell::new();

#[derive(Debug, Deserialize)]
struct ConfigWrapper {
    libp2p: NodeConfig,
}

#[no_mangle]
pub extern "C" fn niq_start_node(config_json: *const c_char) -> i32 {
    let json_str = unsafe { CStr::from_ptr(config_json) };
    let cfg: ConfigWrapper = match serde_json::from_str(json_str.to_str().unwrap_or("")) {
        Ok(cfg) => cfg,
        Err(_) => return -1,
    };

    let mut node = Node::new(cfg.libp2p);
    node.start();
    NODE.set(Mutex::new(node)).ok();
    0
}

#[no_mangle]
pub extern "C" fn niq_stop_node() -> i32 {
    if let Some(lock) = NODE.get() {
        if let Ok(mut node) = lock.lock() {
            node.stop();
        }
    }
    0
}

#[no_mangle]
pub extern "C" fn niq_get_peer_id(out_buf: *mut c_char, buf_len: i32) -> i32 {
    let peer_id = "placeholder-peer";
    let c_string = CString::new(peer_id).unwrap();
    let bytes = c_string.as_bytes_with_nul();
    if bytes.len() > buf_len as usize {
        return -1;
    }
    unsafe {
        std::ptr::copy_nonoverlapping(bytes.as_ptr() as *const c_char, out_buf, bytes.len());
    }
    0
}

#[no_mangle]
pub extern "C" fn niq_advertise(_namespace_json: *const c_char) -> i32 {
    0
}

#[no_mangle]
pub extern "C" fn niq_discover(_namespace_str: *const c_char) -> i32 {
    0
}

#[no_mangle]
pub extern "C" fn niq_send_query(_to_peer_id: *const c_char, _query_json: *const c_char) -> i32 {
    0
}

#[no_mangle]
pub extern "C" fn niq_provider_send_chunk(_query_id: *const c_char, _seq: i32, _delta_utf8: *const c_char) -> i32 {
    0
}

#[no_mangle]
pub extern "C" fn niq_provider_end(_query_id: *const c_char, _status_utf8: *const c_char) -> i32 {
    0
}

#[no_mangle]
pub extern "C" fn niq_set_event_callback(callback: extern "C" fn(*const c_char)) -> i32 {
    CALLBACK.set(callback).ok();
    // Simulate ready event
    if let Some(cb) = CALLBACK.get() {
        let json = serde_json::to_string(&NodeEvent::Ready).unwrap();
        let c_string = CString::new(json).unwrap();
        cb(c_string.as_ptr());
    }
    0
}
