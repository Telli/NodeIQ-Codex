# Navigation & Interaction Flows

This document maps the app's primary journeys and technical integration notes for MQTT, RAG, and marketplace features.

## 1. App Shell
- **Bottom Navigation**: Home → Peers → Chat → Provider → Wallet.
- **Global Drawer**: Accessible from Home and Provider screens, includes Settings, Help, About.
- **Status Surface**: Connection indicator persists across tabs.

## 2. Onboarding Journey
1. Welcome → tap Get Started.
2. Identity Setup → validates required fields.
3. Knowledge Setup → user imports documents (optional skip).
4. Confirmation → Start Using NodeIQ (navigates to Home, sets onboarding complete flag).

**Data Hooks**:
- Save profile data locally using encrypted shared preferences.
- Trigger knowledge indexer service once documents selected.

## 3. Discover & Engage Flow
1. From Home, tap "Find Peers" or navigate to Peers tab.
2. Filter/search directory → view peer details.
3. Tap Chat → open Chat tab with selected peer context.
4. Chat shows credit meter and optional tip button (future).

**MQTT Integration**:
- When connecting to provider via broker, show "Connected (MQTT Secure)".
- If direct connection negotiated (libp2p), badge updates to "Direct (P2P)".

## 4. Provider Activation
1. From Home card "My Agent", toggle Provider Mode or use Provider tab.
2. Confirm activation (if first time) with modal summarizing energy/privacy.
3. Provider screen shows live stats; background service runs.
4. Price adjustments via "View Price Settings" → Pricing screen → Save → return to Provider.

**Marketplace Notes**:
- Credits deducted per query via Broker API; show toast confirmation.
- Pricing changes require confirmation snackbar.

## 5. Wallet Management
1. From Home or Wallet tab → Add Credits.
2. Choose Top Up method (Play Billing mock, Redeem Code) → flow ends with confirmation snackbar.
3. Provider earnings accessible via segment; payout triggers modal (test mode message).

**Edge Cases**:
- Low balance: prompt Add Credits before enabling new chat.
- Transaction fetch failure: show retry banner.

## 6. Knowledge Base Maintenance
1. Navigate to Settings → Knowledge Base or Knowledge tab FAB.
2. Import Document → show progress in list.
3. On completion, show snackbar + update embeddings graph.
4. Rebuild Index clears caches and restarts ingestion service.

**RAG Integration**:
- Index metadata stored locally; highlight doc status (Indexed/Pending/Error).
- Provide offline fallback (existing embeddings available even without network).

## 7. Notifications & Alerts
- Provider mode uses foreground notification with quick toggles.
- In-app snackbars inform about credit updates, query events.
- Offline mode triggers persistent banner + optional Android system notification.

## 8. Error & Offline Recovery
- Offline detection disables chat composer; attempt reconnection every 10s.
- Broker unreachable: offer manual broker settings in Connectivity section.
- Knowledge import failure: show bottom sheet with log + "Send diagnostics".

## 9. Analytics (Privacy-first)
- Only local analytics (session counts) stored on device.
- Provide toggle in Settings → Security to clear analytics data.

## 10. Dark Mode Behavior
- Theme listens to system setting, transitions with crossfade.
- Iconography switches to outlined/filled variants optimized for contrast.

## 11. Localization
- Support LTR/RTL layouts; chips and cards mirror correctly.
- Strings externalized for languages captured during onboarding.

## 12. Future Enhancements (Documented for backlog)
- Marketplace reputation badges on peer cards.
- Voice transcription support in chat composer.
- Shared knowledge bundles in Provider mode.
