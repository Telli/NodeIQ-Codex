# Style Guide

This guide codifies the design system for the NodeIQ Android application. All tokens follow Material 3 and map to Jetpack Compose theme slots for light and dark modes.

## Color Tokens

| Token | Light | Dark | Usage |
| --- | --- | --- | --- |
| `colorPrimary` | `#3B82F6` | `#60A5FA` | Primary actions, highlights, FAB |
| `colorOnPrimary` | `#FFFFFF` | `#0B1120` | Icons/text on primary |
| `colorSecondary` | `#10B981` | `#34D399` | Accents, success states, charts |
| `colorOnSecondary` | `#FFFFFF` | `#052E16` | Icons/text on secondary |
| `colorSurface` | `#F9FAFB` | `#111827` | Screen background |
| `colorSurfaceVariant` | `#EFF6FF` | `#1F2937` | Cards, bottom sheets |
| `colorOnSurface` | `#1F2937` | `#E5E7EB` | Primary body text |
| `colorOnSurfaceVariant` | `#4B5563` | `#9CA3AF` | Secondary text |
| `colorOutline` | `#D1D5DB` | `#374151` | Dividers, outlines |
| `colorSuccess` | `#10B981` | `#34D399` | Positive feedback |
| `colorWarning` | `#F59E0B` | `#FBBF24` | Low credits, offline alerts |
| `colorError` | `#EF4444` | `#F87171` | Errors, broker issues |

### Gradients & Elevation
- **Connection Pulse Gradient:** `#3B82F6` → `#0EA5E9` at 45° for subtle status animations.
- **Provider Mode Background:** Use tonal elevation level 2 with `colorSurfaceVariant` and blur overlay at 12%.

## Typography (Inter)

| Style | Size | Weight | Line Height | Usage |
| --- | --- | --- | --- | --- |
| Display Large | 32 sp | SemiBold | 40 sp | Onboarding hero titles |
| Headline Medium | 24 sp | SemiBold | 32 sp | Section headers |
| Title Medium | 20 sp | Medium | 28 sp | Card titles, dialogs |
| Title Small | 18 sp | Medium | 24 sp | Navigation bar labels |
| Body Large | 16 sp | Regular | 24 sp | Primary copy |
| Body Medium | 14 sp | Regular | 20 sp | Secondary text, chips |
| Label Large | 14 sp | Medium | 20 sp | Buttons, filters |
| Label Small | 12 sp | Medium | 16 sp | Status chips, metadata |

### Text Scaling
- Support dynamic type up to 200%.
- Card layouts wrap gracefully; chips stack vertically beyond 2 rows.

## Iconography
- Material Symbols Rounded, 24dp default, 20dp for dense chips.
- Minimum touch target 48dp with 8dp internal padding.

## Layout & Spacing
- Base grid: 8dp increments.
- Safe area: respect 16dp horizontal padding on phones, 24dp on tablets.
- Card corner radius: 16dp primary, 12dp secondary elements.
- Bottom sheet handle inset: 32dp.

## Imagery & Illustration
- Onboarding uses inclusive illustrations (pastel backgrounds, diverse characters).
- Knowledge base graph uses simple line/bar combo with colorSecondary.

## Accessibility
- Minimum contrast ratio 4.5:1 for text.
- Provide descriptive content descriptions for icons.
- Support reduced motion: disable pulse animations in accessibility settings.

## Motion
- Default easing: `standardDecelerate` for screen transitions.
- Token streaming uses fade-in at 150ms intervals.
- Balance updates use scale+fade animation over 400ms.

## Sound & Haptics
- Light haptic feedback on critical actions (send message, toggle provider mode).
- Optional notification chime when new peer connects (respect system settings).
