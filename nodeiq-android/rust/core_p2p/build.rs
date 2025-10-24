fn main() {
    prost_build::compile_protos(&["proto/kmarket.proto"], &["proto"]).unwrap();
}
