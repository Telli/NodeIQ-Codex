use clap::Parser;
use futures::StreamExt;
use libp2p::identity::Keypair;
use libp2p::multiaddr::Multiaddr;
use libp2p::swarm::SwarmEvent;
use libp2p::{identify, ping, relay, rendezvous, Swarm};
use hyper::{Body, Response, Server};
use tokio::signal;
use tracing::{info, Level};
use tracing_subscriber::FmtSubscriber;

#[derive(Parser, Debug)]
#[command(author, version, about = "NodeIQ Rendezvous & Relay Bootstrap Service")]
struct Opts {
    #[arg(long, default_value = "/ip4/0.0.0.0/udp/8000/quic-v1")]
    listen: String,
    #[arg(long, default_value = ":8080")]
    health: String,
    #[arg(long, default_value = "kmarket")]
    namespace: String,
}

#[tokio::main]
async fn main() -> anyhow::Result<()> {
    let opts = Opts::parse();
    let subscriber = FmtSubscriber::builder()
        .with_max_level(Level::INFO)
        .with_target(false)
        .finish();
    tracing::subscriber::set_global_default(subscriber)?;

    info!(?opts, "Starting bootstrap node");

    let keypair = Keypair::generate_ed25519();
    let local_peer_id = keypair.public().to_peer_id();
    info!(%local_peer_id, "Bootstrap peer ID");

    let transport = libp2p::quic::async_std::Transport::new(keypair.clone());

    let mut swarm = {
        let behaviour = BootstrapBehaviour::new(&keypair, opts.namespace.clone())?;
        Swarm::with_tokio_executor(transport, behaviour, local_peer_id)
    };

    let listen_addr: Multiaddr = opts.listen.parse()?;
    Swarm::listen_on(&mut swarm, listen_addr.clone())?;
    info!(%listen_addr, "Listening for QUIC connections");

    tokio::spawn(async move {
        Server::bind(&opts.health.parse().unwrap())
            .serve(hyper::service::make_service_fn(|_| async {
                Ok::<_, hyper::Error>(hyper::service::service_fn(|_| async {
                    Ok::<_, hyper::Error>(Response::new(Body::from("ok")))
                }))
            }))
            .await
            .unwrap();
    });

    loop {
        tokio::select! {
            event = swarm.select_next_some() => match event {
                SwarmEvent::NewListenAddr { address, .. } => info!(%address, "New listen address"),
                SwarmEvent::Behaviour(event) => info!(?event, "Behaviour event"),
                _ => {}
            },
            _ = signal::ctrl_c() => {
                info!("Shutdown signal received");
                break;
            }
        }
    }

    Ok(())
}

#[derive(libp2p::swarm::NetworkBehaviour)]
struct BootstrapBehaviour {
    identify: identify::Behaviour,
    ping: ping::Behaviour,
    relay: relay::Behaviour,
    rendezvous: rendezvous::client::Behaviour,
}

impl BootstrapBehaviour {
    fn new(keypair: &Keypair, namespace: String) -> anyhow::Result<Self> {
        let identify = identify::Behaviour::new(identify::Config::new("nodeiq/1.0.0".to_string(), keypair.public().clone()));
        let ping = ping::Behaviour::new(ping::Config::new().with_keep_alive(true));
        let relay = relay::Behaviour::new(relay::Config::default());
        let rendezvous = rendezvous::client::Behaviour::new(keypair.clone(), rendezvous::client::Config::default());

        Ok(Self { identify, ping, relay, rendezvous })
    }
}
