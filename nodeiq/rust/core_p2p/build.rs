fn main() {
    prost_build::Config::new()
        .out_dir("src/proto")
        .compile_protos(&["proto/kmarket.proto"], &["proto"]) 
        .expect("Failed to compile protobufs");
}
