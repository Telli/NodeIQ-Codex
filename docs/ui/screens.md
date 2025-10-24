# Screen Specifications

Each section outlines content structure, interactions, and edge-case states for the NodeIQ app.

## 1. Onboarding Flow

### 1.1 Welcome
- **Hero**: NodeIQ logo (animated node connections).
- **Title**: "Welcome to NodeIQ" (Display Large).
- **Subtitle**: "Connect. Learn. Share — securely and directly." (Body Large, colorOnSurfaceVariant).
- **Primary CTA**: `FilledButton` — "Get Started".
- **Secondary CTA**: `OutlinedButton` — "Learn More" → opens info bottom sheet with product overview.
- **Background**: Subtle particle animation that respects reduced motion.

### 1.2 Identity Setup
- **Form Fields**:
  - Text field: Name (auto-capitalization on).
  - Text field: Role/Profession (suggestions using chips).
  - Multi-select chips: Languages (English, Spanish, Hindi, Swahili, Other).
- **Illustration**: Side panel with diverse user avatars.
- **CTA**: "Continue" (disabled until name + at least one language).
- **Validation**: Inline messaging for missing entries.

### 1.3 Knowledge Setup
- **Import Tile**: Large card with icon (description) and `FilledTonalButton` "Import Local Knowledge Base".
- **Progress**: Determinate progress bar showing indexing progress. States:
  - Idle (0%).
  - Indexing with ETA estimate.
  - Error (show retry button + error summary).
- **Info Note**: `AssistChip` icon info with text "Your data stays private — stored on your device.".
- **CTA**: "Finish Setup" (enabled when indexing complete).

### 1.4 Confirmation
- **Message**: "Setup Complete — You’re Ready to Connect!" with confetti animation.
- **CTA**: "Start Using NodeIQ" (navigates to Home Dashboard).

## 2. Home Dashboard
- **Header**: "Hello, [Name] 👋" with dynamic greeting (morning/afternoon).
- **Status Chip**: indicates transport (🟢 Connected MQTT Secure / 🔵 P2P Mode / 🔴 Offline).
- **Card Grid** (2-column):
  1. Peers Online — shows count + mini avatars.
  2. My Agent — status toggle, quick link to Provider Mode.
  3. Wallet Balance — balance + button "Add Credits".
  4. Recent Chats — last 2 peers with timestamp.
- **Quick Actions**: Horizontal list of `AssistChip`s: Find Peers, Go Provider Mode, Add Credits.
- **Alerts**: Inline banner for low credits (<5) or offline.

## 3. Peers Directory
- **Search Bar**: Collapsible top app bar with search field placeholder "Search by agent or skill...".
- **Filter Row**: Scrollable chips: Mechanic, Health, Agriculture, Education, Legal, Custom.
- **Peer Cards**:
  - Avatar (status ring color-coded).
  - Name + rating (⭐ 4.8).
  - Skill tags (max 3, overflow with "+2").
  - Pricing: "0.015 credits / 1k tokens" or "Free".
  - Action: `FilledButton` "Chat".
- **Empty State**: illustration + "No peers match your filters" with CTA "Reset filters".
- **FAB**: "Register My Agent" → opens modal form (agent name, description, pricing shortcut).

## 4. Chat Interface
- **Top App Bar**: Back button, peer name, status dot, transport badge.
- **Message List**:
  - Bubbles with tail (rounded 20dp). User messages align right, peer/AI left.
  - Streaming animation: shimmer line for incoming tokens.
  - Metadata row: timestamp + credit consumption note (Label Small).
  - System notices (e.g., "Switched to P2P Mode") styled as center-aligned chips.
- **Footer Composer**:
  - Text input with placeholder "Ask your question..." and support for multi-line (max 4).
  - Icons: mic (voice), attachment (disabled), send (enabled when text or voice ready).
  - Credit Estimate: inline label showing estimated cost.
- **Info Banner**: anchored above composer with contextual tips.
- **Edge Cases**:
  - Offline: composer disabled, show "Waiting for connection…" banner.
  - Low credits: highlight Add Credits CTA.

## 5. Provider Mode
- **Header**: "Provider Mode" with switch for ON/OFF.
- **Status Card**:
  - Large icon (🧠).
  - Status text: "Listening for queries…" or "Paused".
  - Agent ID, queries served, avg response time.
- **Controls**:
  - Button: "View Price Settings" → navigates to Pricing screen.
  - Button: "View Logs" (future placeholder).
- **Chart Card**: Mini bar chart for queries served by hour (24h).
- **Footer**: persistent note "Running securely in background — visible in notifications.".
- **Background Service Indicator**: highlight when minimized.

## 6. Wallet & Marketplace
- **Header**: "Your Wallet" with segmented control for `Wallet` / `Earnings`.
- **Balance Card**: Balance amount, "Add Credits" primary button, secondary "View Transactions".
- **Transaction List**: Date, description (e.g., "Chat with Dr. Rivera"), amount with +/- color coding.
- **Button Grid**: Top Up via Play Billing, Redeem Code, Transfer Credits (future disabled).
- **Provider Earnings Section**: Stats for credits earned, total queries served, `FilledButton` "Request Payout" (disabled in test mode with tooltip).
- **Info Banner**: "All payments are managed through Broker API (test mode).".
- **Animation**: Balance card pulses when credit added.

## 7. Settings
- **Sections**: Each as cards with chevrons.
  - Profile: Name, Agent ID, Skills, Languages (editable forms).
  - Connectivity: MQTT host, port, libp2p toggle (disabled tooltip "Coming soon").
  - Knowledge Base: Add Document, Rebuild Index, Clear Cache (with confirmations).
  - Security: Manage certificates, reset app (destructive action with warning dialog).
  - About: Version, privacy, open-source licenses.
- **Support Links**: "Help Center", "Report a Bug" (email intent).

## 8. Knowledge Base
- **Header**: "Knowledge Base" with FAB "Import Document".
- **Upload Section**: Card with upload icon, supported file types.
- **Document List**: Each item shows filename, size, status chip (Indexed/Pending/Error), last updated.
- **Graph View**: optional toggle to show embeddings summary (bar graph: docs vs embeddings count, vector size label).
- **Info Banner**: "Your knowledge base runs fully offline on-device.".
- **Empty State**: Encourage import with simple illustration.

## 9. Pricing Screen
- **Header**: "Set Your Price".
- **Sliders**:
  - Price per 1k tokens (0.001–0.05) with numeric input sync.
  - Minimum query price (0.001–0.02).
- **Preview Card**: Example calculation ("A 50-token answer ≈ 0.003 credits").
- **CTA**: `FilledButton` "Save Price" (disabled until value changed).
- **Validation**: Show warning if minimum price > per 1k tokens estimate.

## 10. Notifications & Alerts
- **Snackbar Style**: Rounded corners, high contrast text.
- **Examples**:
  - "Query received from peer." (action: View).
  - "Response sent successfully." (auto dismiss).
  - "Wallet updated (+10 credits)." (action: View wallet).
  - "Provider mode paused." (action: Resume).
  - "Knowledge base indexed successfully.".
- **System Tray**: Provider mode persistent notification with quick actions.

## 11. Navigation Components
- **Bottom Navigation**: 5 items (Home, Peers, Chat, Provider, Wallet). Active tab uses filled icon + label, inactive outlines.
- **FABs**:
  - Wallet screen: Add Credits.
  - Knowledge Base: Import Knowledge.
- **Navigation Drawer** (optional): Settings, Help, About NodeIQ.
- **Transitions**: Crossfade between tabs, slide for onboarding progression.

## 12. Edge Cases & Offline
- Offline state displays blue banner: "Waiting for connection…" with retry.
- Broker unreachable: show error dialog with troubleshooting link.
- Low credit warning: appears when balance < 2 credits with CTA to add credits.

## 13. Dark Mode Notes
- Auto switch using system theme.
- Surface backgrounds shift to `#111827`, maintain contrast by lightening text and chip backgrounds.
