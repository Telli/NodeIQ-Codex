# Component Library

Reusable components align to Material 3 and are intended for Jetpack Compose implementation. Each component lists variants, states, and engineering notes.

## 1. Action Cards
- **Description**: Rounded cards used on Home Dashboard.
- **Variants**:
  - Metric (Peers Online, Wallet Balance).
  - Status (My Agent state).
  - Activity (Recent Chats).
- **States**: Default, Hover (web preview), Pressed, Disabled (offline).
- **Notes**: Use `CardDefaults.cardElevation(4.dp)` for light, increase to 8dp on pressed.

## 2. Status Chips
- **Usage**: Transport mode, connection states, document status.
- **Variants**: Success (green), Info (blue), Warning (amber), Error (red).
- **Specs**: 12dp radius, 8dp horizontal padding, icon optional (leading).
- **Behavior**: Animates color change on status updates over 250ms.

## 3. Peer Card
- **Structure**: Avatar, name, rating, skill tags, pricing label, Chat button.
- **States**: Default, Hover, Pressed, Offline (grayscale avatar, disabled button).
- **Interactions**: Tapping opens chat preview modal before full chat.
- **Engineering**: Compose `Card` with slot APIs for header and footer.

## 4. Message Bubble
- **Variants**: User, Peer, System.
- **Features**: Support Markdown (bold, italics, inline code), attachments placeholder.
- **States**: Sending (opacity 0.6), Sent, Error (red border with retry icon).
- **Animation**: Streaming tokens fade in using `AnimatedContent`.

## 5. Token Stream Loader
- **Description**: Inline loader showing three pulsing dots with gradient.
- **Usage**: Chat streaming, knowledge indexing.
- **Implementation**: Compose `InfiniteTransition` with scale animation 0.8–1.1.

## 6. Chart Mini Card
- **Usage**: Provider Mode queries per hour.
- **Data**: Accepts list of hourly counts (0–20).
- **Visuals**: Bars use `colorSecondary`, highlight current hour.
- **Accessibility**: Provide content description summarizing trend.

## 7. Wallet Transaction Row
- **Fields**: Icon (credit/debit), title, subtitle (date & counterparty), amount.
- **Color Coding**: Positive green, negative slate.
- **Swipe Actions**: Optional to reveal "View details".

## 8. Form Fields
- **Types**: Text, numeric, slider, toggle, multi-select chips.
- **Validation Messaging**: Inline helper text, colorError border.
- **Keyboard Actions**: Next/Done mapping for onboarding forms.

## 9. Bottom Navigation Bar
- **Items**: 5 fixed items with labels.
- **States**: Selected (filled icon, label), Unselected (outline icon), Badge (optional count for unread chats).
- **Animation**: `NavigationBarItemDefaults` with spring effect.

## 10. FABs
- **Variants**: Large (56dp) for wallet, Small (48dp) for knowledge import.
- **Elevation**: 6dp default, 12dp on scroll up.
- **Contextual Behavior**: Hide on scroll down, reappear when user scrolls to top.

## 11. Dialogs & Bottom Sheets
- **Scenarios**: Learn More (onboarding), Register Agent, Low credits, Reset app.
- **Content**: Title, description, primary & secondary actions, optional info icon.
- **Behavior**: Dismissible unless destructive action.

## 12. Notifications
- **In-App Snackbar**: Compose `SnackbarHost` with custom colors matching status.
- **System Notification**: Provider mode with quick action buttons (Pause, View Logs).

## 13. Empty States
- **Components**: Illustration, title, subtitle, action button.
- **Examples**: No peers, No transactions, No documents.
- **Tone**: Encouraging, empathetic.

## 14. Offline Banner
- **Description**: Persistent top banner when network unavailable.
- **Content**: Icon (cloud off), text "Waiting for connection…", action "Retry".
- **Behavior**: Slide in/out animation, accessible (announces via TalkBack).

## 15. Price Slider Control
- **Fields**: Title, slider, numeric text input.
- **Validation**: Sync both inputs, show helper text for conversions.
- **Notes**: Use `remember` state to avoid recomposition jitter.

## 16. Knowledge Document Item
- **Fields**: Icon (PDF/Text), title, subtitle, status chip, overflow menu.
- **Actions**: Rebuild, Delete (with confirmation), View metadata.

## 17. Credits Meter Badge
- **Usage**: Chat metadata showing credits consumed per response.
- **Style**: Capsule with `colorSurfaceVariant` background, Label Small text.
- **Behavior**: Animates count increment.
