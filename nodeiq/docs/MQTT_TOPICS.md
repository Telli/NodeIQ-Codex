# MQTT Topic Map

| Topic Pattern | QoS | Retain | Payload | Notes |
| --- | --- | --- | --- | --- |
| `/registry/announce` | 1 | true (TTL ~60s via broker policy) | JSON `{"agent_id":...,"skills":...}` | Presence announcements |
| `/query/{agent_id}` | 1 | false | QUERY frame | Directed queries to providers |
| `/response/{query_id}` | 1 | false | STREAM / END frames | Streaming responses |

Presence announcements should include a unique `peerId` when available to differentiate multiple devices advertising the same agent.
