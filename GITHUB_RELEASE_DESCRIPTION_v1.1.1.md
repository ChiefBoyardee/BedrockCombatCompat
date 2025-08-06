# BedrockCombatCompat v1.1.1 - Color Code Fix

## 🐛 Critical Bug Fix

This patch release resolves a display issue where Minecraft color codes were appearing as "weird A things" (section symbols §) in chat messages instead of properly colored text.

### What's Fixed:
- **Color Code Display Issue**: All plugin messages now display with proper colors
- **Message Formatting**: Standardized color code handling across all features
- **Text Rendering**: Improved compatibility with different Minecraft versions

### Technical Details:
- Replaced manual color code conversion with Bukkit's official `ChatColor.translateAlternateColorCodes()`
- Enhanced error handling for color code translation
- Better text processing for action bar messages and notifications

## 📦 Installation

1. Download `bedrock-combat-compat-1.1.1.jar`
2. Place in your server's `plugins/` folder
3. Restart your server or use `/reload` (if supported)

## 🔄 Upgrading from v1.1.0

This is a seamless upgrade - simply replace the JAR file. No configuration changes required.

## 🎯 Compatibility

- **Minecraft**: 1.8.8 - 1.21+
- **Server**: Spigot, Paper, or compatible forks
- **Java**: 8+
- **Dependencies**: Floodgate (optional)

---

**Full Changelog**: https://github.com/ChiefBoyardee/BedrockCombatCompat/compare/v1.1.0...v1.1.1