FROM rust:1.74 as builder
WORKDIR /usr/src/bootstrap
COPY . .
RUN cargo build --release

FROM debian:bookworm-slim
WORKDIR /app
COPY --from=builder /usr/src/bootstrap/target/release/nodeiq-bootstrap /usr/local/bin/nodeiq-bootstrap
EXPOSE 8000/udp 8080
ENTRYPOINT ["nodeiq-bootstrap"]
CMD ["--listen", "/ip4/0.0.0.0/udp/8000/quic-v1", "--health", ":8080", "--namespace", "kmarket"]
