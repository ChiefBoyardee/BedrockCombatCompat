# Changelog

All notable changes to BedrockCombat will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Planned Features
- Configuration file support
- Multiple combat speed presets
- GUI for combat mode management
- Integration with other combat plugins
- Statistics tracking
- World-specific combat modes

## [1.0.0] - 2024-01-XX

### Added
- **Initial Release** of BedrockCombat plugin
- **Bedrock Combat Mode** - Removes attack cooldown for instant combat
- **Per-Player Toggle System** - Individual combat mode control
- **Smart Auto-Detection** - Automatic Bedrock combat for players with names starting with '.'
- **Colored Chat Messages** - Beautiful feedback with color coding
- **Command System** with aliases:
  - `/bedrockcombat <player>` (main command)
  - `/bc <player>` (short alias)
  - `/fastcombat <player>` (legacy alias)
- **Permission System**:
  - `bedrockcombat.use` - Use the bedrockcombat command
  - `bedrockcombat.others` - Toggle combat mode for other players
- **Lightweight Design** - Under 10KB plugin size
- **Toggle Functionality** - Switch between Java and Bedrock combat modes
- **High Performance** - Minimal server impact
- **Comprehensive Documentation** - README, LICENSE, and build instructions

### Technical Details
- **API Compatibility**: Spigot/Bukkit 1.20.4+
- **Java Version**: 8+ support
- **Package Structure**: `io.github.chiefboyardee.bedrockcombat`
- **Main Class**: `BedrockCombatPlugin`
- **Attack Speed Values**:
  - Java Edition: 4.0 (1.6 second cooldown)
  - Bedrock Edition: 1024.0 (no cooldown)

### Build System
- **Windows Batch Script** (`build.bat`) for easy compilation
- **Maven Support** with proper POM configuration
- **Manual Compilation** instructions
- **IDE Integration** support

### Documentation
- **Professional README** with badges, features, and examples
- **MIT License** for open-source distribution
- **Build Instructions** for multiple platforms
- **Troubleshooting Guide** with common solutions
- **Compatibility Matrix** for server software

---

## Version History Summary

| Version | Release Date | Key Features |
|---------|--------------|--------------|
| **1.0.0** | 2024-01-XX | Initial release with core combat toggle functionality |

---

## Migration Guide

### From FastCombatPlugin to BedrockCombat

If you're upgrading from the original FastCombatPlugin:

1. **Remove** the old `FastCombat-1.0.jar` from your plugins folder
2. **Download** the new `BedrockCombat-1.0.0.jar`
3. **Update** any scripts or documentation that reference the old command names
4. **Note**: Command syntax has changed from `/fastcombat` to `/bedrockcombat`

### Command Changes
| Old Command | New Command | Status |
|-------------|-------------|--------|
| `/fastcombat <player>` | `/bedrockcombat <player>` | Still works as alias |

### Permission Changes
| Old Permission | New Permission | Status |
|----------------|----------------|--------|
| `fastcombat.use` | `bedrockcombat.use` | Update required |
| `fastcombat.others` | `bedrockcombat.others` | Update required |

---

## Contributing

We welcome contributions! Please see our [Contributing Guidelines](CONTRIBUTING.md) for details on:

- **Bug Reports** - How to report issues
- **Feature Requests** - Suggesting new functionality
- **Code Contributions** - Pull request process
- **Documentation** - Improving guides and examples

---

## Support

- **Issues**: [GitHub Issues](https://github.com/ChiefBoyardee/BedrockCombat/issues)
- **Discussions**: [GitHub Discussions](https://github.com/ChiefBoyardee/BedrockCombat/discussions)
- **Wiki**: [Project Wiki](https://github.com/ChiefBoyardee/BedrockCombat/wiki)

---

*This changelog is automatically updated with each release. For the latest changes, see the [Unreleased] section above.*