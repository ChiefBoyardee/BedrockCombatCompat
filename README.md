# BedrockCombat

[![GitHub release](https://img.shields.io/github/v/release/ChiefBoyardee/BedrockCombat)](https://github.com/ChiefBoyardee/BedrockCombat/releases)
[![GitHub license](https://img.shields.io/github/license/ChiefBoyardee/BedrockCombat)](https://github.com/ChiefBoyardee/BedrockCombat/blob/main/LICENSE)
[![Spigot Version](https://img.shields.io/badge/Spigot-1.20.4+-orange)](https://www.spigotmc.org/)
[![Geyser Compatible](https://img.shields.io/badge/Geyser-Compatible-blue)](https://geysermc.org/)

A lightweight Minecraft plugin designed for **cross-platform servers** using **Geyser** and **Floodgate**. Preserves the familiar **controller-friendly combat experience** for Bedrock Edition players while maintaining traditional Java combat for PC players. Perfect for **non-PvP focused servers** that want to ensure all players have an optimal combat experience regardless of their platform.

## Features

- **Controller-Friendly Combat** - Optimized for mobile and console Bedrock players
- **Clean UI** - Hidden attack indicator for Bedrock players (no GUI popup clutter)
- **Platform Balance** - Preserves Java combat mechanics for PC players  
- **Smart Auto-Detection** - Automatically identifies Bedrock players via Floodgate
- **Automatic PvP/PvE Detection** - Fair PvP with Java combat, optimal PvE with platform-specific combat
- **Smart PvP/PvE Detection** - Attack indicator appears during PvP, hidden during PvE for Bedrock players
- **Colored Chat Messages** - Beautiful feedback with platform-specific styling
- **Instant Toggle** - Quick combat mode switching with `/bedrockcombat`
- **Permission System** - Fine-grained control over who can use commands
- **Cross-Platform Ready** - Perfect for Geyser/Floodgate servers
- **Configurable** - Customize PvP detection, timeouts, and behavior

## Quick Start

1. **Ensure** you have [Geyser](https://geysermc.org/) and [Floodgate](https://github.com/GeyserMC/Floodgate) installed for cross-platform support
2. **Download** the latest `BedrockCombat-1.0.0.jar` from [releases](https://github.com/ChiefBoyardee/BedrockCombat/releases)
3. **Place** it in your server's `plugins` folder
4. **Restart** your server
5. **Bedrock players** automatically get controller-friendly combat, **Java players** keep traditional combat
6. **Use** `/bedrockcombat <player>` to manually toggle combat modes if needed

## Commands & Permissions

| Command | Description | Permission |
|---------|-------------|------------|
| `/bedrockcombat` | Show plugin info and commands | None |
| `/bedrockcombat <player>` | Toggle combat mode for a player | `bedrockcombat.use` |
| `/bedrockcombat status` | Show plugin status and statistics | `bedrockcombat.admin` |
| `/bedrockcombat reload` | Reload plugin configuration | `bedrockcombat.admin` |

### Command Aliases
- `/bc` - Short alias for bedrockcombat
- `/bedrockpvp` - Alternative alias
- `/fastcombat` - Legacy alias

### Permissions
- `bedrockcombat.use` - Allows toggling combat modes (default: op)
- `bedrockcombat.admin` - Allows admin commands (status, reload) (default: op)
- `bedrockcombat.*` - Grants all permissions (default: op)

### Usage Examples
```bash
# Show plugin information
/bedrockcombat

# Enable Bedrock combat for a mobile player
/bedrockcombat Steve

# Check plugin status and statistics
/bedrockcombat status

# Use the short alias
/bc Alex

# Reload plugin configuration
/bc reload
```

### **PvP Detection in Action**
```
[Player joins] -> Auto-detected as Bedrock -> Controller-friendly combat enabled
[PvP starts] -> Both players -> Temporary Java combat for fairness
[PvP ends] -> Bedrock player -> Controller-friendly combat restored
[Java player] -> Keeps Java combat throughout
```

## **Core Features**

### **Cross-Platform Combat Optimization**
- **Bedrock Players**: Fast attack speed (4.0) for smooth controller/touch gameplay
- **Clean UI Experience**: Hidden attack indicator popup for Bedrock players
- **Java Players**: Traditional combat speed (1.6) for familiar PC experience
- **Automatic Detection**: Via Floodgate integration or name prefix fallback

## How It Works

BedrockCombat creates an optimal **cross-platform experience** by modifying the `ATTACK_SPEED` attribute based on player platform:

| Platform | Combat Mode | Attack Speed | Cooldown | Best For |
|----------|-------------|--------------|----------|----------|
| **Java Edition** | Traditional | 4.0 | 1.6 seconds | Mouse & Keyboard |
| **Bedrock Edition** | Controller-Friendly | 1024.0 | None | Touch & Controller |

### **Cross-Platform Detection**
- **Floodgate Players**: Automatically detected and given controller-friendly combat
- **Bedrock Prefix**: Players with usernames starting with '.' (Floodgate default) get optimized combat
- **Manual Override**: Server admins can toggle any player's combat mode via commands

### **Why This Matters**
- **Mobile Players**: Touch controls work better without attack cooldowns
- **Console Players**: Controller input feels more responsive with instant combat
- **Java Players**: Preserve the strategic timing-based combat they're used to
- **Non-PvP Servers**: Focus on building and survival without combat disadvantages

### **PvP Balance Notice**
**Important**: Bedrock combat mode provides a significant advantage in PvP scenarios due to the removal of attack cooldowns. To maintain fair gameplay:

- **Automatic PvP Detection**: The plugin detects player-vs-player combat and temporarily reverts all participants to Java combat mechanics
- **Smart Reversion**: After PvP ends (death, distance, or timeout), Bedrock players automatically return to controller-friendly combat
- **Configurable**: Server admins can disable PvP detection if desired for specific game modes

This ensures **fair PvP** while maintaining **optimal PvE** experiences for all platforms.

## Building from Source

### Prerequisites
- Java 8 or higher
- Spigot API JAR (included: `spigot-api-1.21.8-R0.1-20250729.092320-4.jar`)

### Build Options

#### Option 1: Batch Script (Windows)
```batch
.\build.bat
```

#### Option 2: Manual Compilation
```bash
# Compile
javac -cp "spigot-api-1.21.8-R0.1-20250729.092320-4.jar" -d target src/main/java/io/github/chiefboyardee/bedrockcombat/BedrockCombatPlugin.java

# Copy resources
copy src/main/resources/plugin.yml target/

# Create JAR
cd target && jar cf BedrockCombat-1.0.0.jar -C . .
```

#### Option 3: Maven
```bash
mvn clean compile package
```

## Project Structure

```
BedrockCombat/
+-- src/main/java/io/github/chiefboyardee/bedrockcombat/
|   +-- BedrockCombatPlugin.java
+-- src/main/resources/
|   +-- plugin.yml
+-- target/
|   +-- BedrockCombat-1.0.0.jar
+-- build.bat
+-- pom.xml
+-- README.md
+-- LICENSE
+-- CHANGELOG.md
```

## Configuration

The plugin works out of the box with smart defaults. Current settings are built-in:

### **Combat Settings**
- **Bedrock Combat**: 4.0 attack speed (controller-optimized)
- **Java Combat**: 1.6 attack speed (traditional)
- **PvP Timeout**: 10 seconds after combat ends

### **PvP Detection**
- **Auto-Detection**: Enabled by default
- **Floodgate Integration**: Automatic Bedrock player detection
- **Manual Override**: Available via commands
- **Fair PvP**: Temporary Java combat during player vs player

### **Future Configuration Options**
- Configurable attack speeds
- Custom PvP timeout duration
- Per-world settings
- Integration with other plugins
- Config file support

## Troubleshooting

<details>
<summary><strong>Plugin not loading</strong></summary>

- Check server logs for errors
- Ensure you're using Spigot/Paper 1.20.4+
- Verify JAR file is in the `plugins` folder
- Check file permissions
- Ensure Floodgate is installed for cross-platform features
</details>

<details>
<summary><strong>Commands not working</strong></summary>

- Verify player has `bedrockcombat.use` permission
- Check if plugin is enabled: `/plugins`
- Ensure correct command syntax: `/bedrockcombat <player>`
</details>

<details>
<summary><strong>Combat not changing</strong></summary>

- Try rejoining the server
- Check if player is in creative/spectator mode
- Verify the correct player name is being used
- Check server console for errors
- Use `/bedrockcombat status` to see current combat modes
</details>

<details>
<summary><strong>Bedrock players not detected automatically</strong></summary>

- Make sure Floodgate is installed and working
- Check that Bedrock players have the `.` prefix in their names
- Use `/bedrockcombat status` to see detection statistics
- Use manual toggle as a workaround: `/bedrockcombat <player>`
</details>

<details>
<summary><strong>PvP detection not working</strong></summary>

- Verify both players are online and in the same world
- Check console for PvP detection messages
- Use `/bedrockcombat status` to see active PvP sessions
- Ensure the plugin has proper event permissions
</details>

<details>
<summary><strong>Combat doesn't revert after PvP</strong></summary>

- Wait for the 10-second timeout to complete
- Check if the player died (combat reverts immediately)
- Use `/bedrockcombat reload` to reset the plugin state
- Manually toggle the player if needed
</details>

## Contributing

We welcome contributions! Here's how to get started:

1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/amazing-feature`)
3. **Commit** your changes (`git commit -m 'Add amazing feature'`)
4. **Push** to the branch (`git push origin feature/amazing-feature`)
5. **Open** a Pull Request

## Cross-Platform Setup

### Required for Cross-Platform Support
| Plugin | Purpose | Status |
|--------|---------|--------|
| **[Geyser](https://geysermc.org/)** | Bedrock client support | ✅ Required |
| **[Floodgate](https://github.com/GeyserMC/Floodgate)** | Bedrock authentication | ✅ Recommended |

### Why Geyser + Floodgate?
- **Geyser** allows Bedrock Edition players to join Java servers
- **Floodgate** enables Bedrock players to join without Java accounts
- **BedrockCombat** automatically detects these players and optimizes their experience

## Server Compatibility

| Server Software | Version | Cross-Platform | Status |
|----------------|---------|----------------|--------|
| **Paper** | 1.20.4+ | ✅ Geyser Compatible | ✅ Recommended |
| **Spigot** | 1.20.4+ | ✅ Geyser Compatible | ✅ Supported |
| **Purpur** | 1.20.4+ | ✅ Geyser Compatible | ✅ Supported |
| **Bukkit** | 1.20.4+ | ⚠️ Limited Geyser Support | ⚠️ Basic Support |

## Statistics

- **GitHub Stars**: [Star this repo](https://github.com/ChiefBoyardee/BedrockCombat) if you find it useful!
- **Issues**: Report bugs on [GitHub Issues](https://github.com/ChiefBoyardee/BedrockCombat/issues)

## License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

## Perfect For These Server Types

### **Building & Creative Servers**
- Players focus on construction, not combat mechanics
- Mobile players can break blocks efficiently without cooldowns
- Cross-platform building competitions work seamlessly

### **Survival & SMP Servers**
- PvE combat feels natural for all platforms
- Mob fighting works optimally for touch and controller users
- Maintains Java combat feel for PC players who prefer it

### **Family-Friendly Servers**
- Kids on mobile/console get the same experience as PC players
- No combat disadvantages based on platform choice
- Focus on fun and exploration, not combat complexity

## Acknowledgments

- **GeyserMC Team** for making cross-platform Minecraft possible
- **Floodgate Developers** for seamless Bedrock authentication
- **Spigot Team** for the excellent API
- **Cross-Platform Server Admins** for feedback and testing
- **Mobile & Console Players** for inspiring this plugin

## Support

- **Bug Reports**: [GitHub Issues](https://github.com/ChiefBoyardee/BedrockCombat/issues)
- **Discussions**: [GitHub Discussions](https://github.com/ChiefBoyardee/BedrockCombat/discussions)
- **Documentation**: [Wiki](https://github.com/ChiefBoyardee/BedrockCombat/wiki)

---

<div align="center">

**Made with love for the Minecraft community**

[Back to Top](#️-bedrockcombat)

</div>