# BedrockCombatCompat v1.1.0

## 🆕 New Features
- **Action Bar Status Display** - Real-time combat mode and PvP status indicators
- **Smart Notifications** - Instant alerts for combat mode changes and PvP events
- **Enhanced Configuration** - New action bar customization options

## ⚙️ Configuration Updates
- `action-bar.enabled` - Toggle action bar display
- `action-bar.show-combat-mode` - Show current combat mode (Bedrock/Java)
- `action-bar.show-pvp-status` - Display PvP mode and timeout countdown
- `action-bar.update-interval` - Customize refresh rate (default: 20 ticks)

## 🔧 Technical Improvements
- Cross-version action bar compatibility (1.8-1.21+)
- Optimized performance with smart update scheduling
- Better error handling and graceful fallbacks
- Memory-efficient player state management

## 🐛 Bug Fixes
- **Fixed Color Code Display Issue** - Resolved "weird A things" (section symbols) appearing in chat messages
- Proper color code handling using Bukkit's ChatColor.translateAlternateColorCodes()
- All messages now display with correct colors instead of raw section symbols

## 📋 Requirements
- **Minecraft**: 1.8.8 - 1.21+
- **Server**: Spigot, Paper, or compatible forks
- **Java**: 8+

## 🚀 Upgrade Notes
- Fully backward compatible with v1.0.0
- New configuration options auto-generate on first load
- No breaking changes to existing functionality

---
*For detailed installation and configuration instructions, visit the [GitHub repository](https://github.com/ChiefBoyardee/BedrockCombatCompat).*