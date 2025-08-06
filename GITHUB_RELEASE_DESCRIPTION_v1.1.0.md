🎮 **BedrockCombatCompat v1.1.0** - Action Bar Status Display

Major feature update! Now with real-time visual combat status directly in your action bar!

## 🆕 **What's New in v1.1.0**
- 📊 **Action Bar Status Display** - Real-time combat and PvP status notifications
- ⚡ **Live Updates** - See your combat mode and PvP status instantly
- 🎨 **Customizable Messages** - Configure action bar text and update intervals
- 🔄 **Cross-Version Compatible** - Works across all supported Minecraft versions
- ⚙️ **Enhanced Configuration** - New options for action bar customization

## ✨ **New Features**

### 📊 Action Bar Status Display
- **Real-time Combat Mode Indicator** - Shows when Bedrock combat mode is active
- **PvP Status Notifications** - Displays current PvP engagement status
- **Configurable Update Intervals** - Customize how often the action bar updates
- **Smart Display Logic** - Only shows relevant information when needed

### 🎛️ Enhanced Configuration Options
```yaml
# New Action Bar Settings
action-bar:
  enabled: true
  update-interval: 20  # ticks (1 second)
  show-combat-mode: true
  show-pvp-status: true
  message: "&6Combat: &e{combat_mode} &8| &cPvP: &e{pvp_status}"
```

## 🔧 **Technical Improvements**
- **New ActionBarManager Class** - Centralized action bar management
- **Reflection-Based Compatibility** - Seamless cross-version support
- **Performance Optimized** - Efficient update scheduling
- **Clean Integration** - Seamlessly integrates with existing systems

## 📋 **Complete Feature Set**
| Feature | Java Players | Bedrock Players |
|---------|--------------|-----------------|
| Attack Cooldown | ✅ Traditional | ❌ Removed |
| Attack Speed | ⚖️ Standard | ⚡ Optimized |
| PvP Detection | ✅ Monitored | ✅ Monitored |
| **Action Bar Status** | ✅ **Real-time** | ✅ **Real-time** |

## 🎯 **Perfect For**
- Survival servers with mixed player bases
- PvP servers requiring fair combat balance
- Cross-platform communities wanting visual feedback
- Servers needing enhanced user experience

## 🔧 **Requirements**
- Spigot/Paper 1.21+
- Java 17+
- Geyser + Floodgate (for cross-platform)

## 📦 **Installation**
1. Download `BedrockCombat-1.1.0.jar`
2. Place in `plugins/` folder
3. Restart server
4. Configure action bar settings in `config.yml`
5. Enjoy enhanced combat experience!

## 🎮 **Commands**
- `/bedrockcombat help` - Show help menu
- `/bedrockcombat status` - Check combat status
- `/bedrockcombat reload` - Reload configuration
- `/bedrockcombat toggle <player>` - Toggle Bedrock combat for a player

## 📈 **Upgrade from v1.0.x**
- **Automatic Configuration Migration** - Existing settings preserved
- **New Action Bar Options** - Added automatically to config
- **Backward Compatible** - No breaking changes
- **Seamless Upgrade** - Just replace the JAR file

## 🐛 **Bug Fixes & Improvements**
- Enhanced error handling for action bar updates
- Improved performance with optimized update scheduling
- Better integration with existing plugin systems
- More robust cross-version compatibility

## 🔄 **Version History**
- **v1.1.0** - Action Bar Status Display with real-time notifications
- **v1.0.3** - Enhanced PvP detection with swing-based cooldown
- **v1.0.2** - Version consistency fixes
- **v1.0.1** - Compilation fixes and improved Floodgate integration
- **v1.0.0** - Initial release with core combat optimization

---

**🌟 Production Ready!** Thoroughly tested with enhanced user experience and visual feedback.

**📊 Download Stats:** Join thousands of servers already using BedrockCombatCompat!

**Made with ❤️ for the Minecraft community**

---

### 📝 **Full Changelog**
See [CHANGELOG.md](CHANGELOG.md) for detailed technical changes and development notes.

### 🤝 **Contributing**
Interested in contributing? Check out [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines.

### 📋 **Roadmap**
See [FEATURE_ROADMAP.md](FEATURE_ROADMAP.md) for upcoming features and improvements.