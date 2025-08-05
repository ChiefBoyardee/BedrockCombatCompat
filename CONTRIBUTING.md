# Contributing to BedrockCombatCompat

Thank you for your interest in contributing to BedrockCombatCompat! We welcome contributions from the community and are grateful for any help you can provide.

## Table of Contents

- [Code of Conduct](#code-of-conduct)
- [Getting Started](#getting-started)
- [How to Contribute](#how-to-contribute)
- [Development Setup](#development-setup)
- [Coding Standards](#coding-standards)
- [Testing](#testing)
- [Submitting Changes](#submitting-changes)
- [Reporting Issues](#reporting-issues)
- [Feature Requests](#feature-requests)

## Code of Conduct

This project adheres to a code of conduct that we expect all contributors to follow:

- **Be respectful** and inclusive in all interactions
- **Be constructive** when providing feedback
- **Be patient** with new contributors
- **Focus on the code**, not the person
- **Help others learn** and grow

## Getting Started

### Prerequisites

- **Java 8+** (Java 17+ recommended)
- **Git** for version control
- **IDE** (IntelliJ IDEA, Eclipse, or VS Code recommended)
- **Minecraft Server** (Spigot/Paper 1.20.4+) for testing

### Fork and Clone

1. **Fork** the repository on GitHub
2. **Clone** your fork locally:
   ```bash
   git clone https://github.com/YOUR_USERNAME/BedrockCombatCompat.git
   cd BedrockCombatCompat
   ```
3. **Add upstream** remote:
   ```bash
   git remote add upstream https://github.com/ChiefBoyardee/BedrockCombatCompat.git
   ```

## How to Contribute

### Types of Contributions

We welcome several types of contributions:

- **Bug fixes** - Fix issues and improve stability
- **New features** - Add functionality that benefits users
- **Documentation** - Improve guides, examples, and code comments
- **Testing** - Add tests and improve test coverage
- **Code quality** - Refactoring and optimization
- **Translations** - Localization support (future feature)

### Contribution Workflow

1. **Check existing issues** to avoid duplicates
2. **Create an issue** for discussion (for major changes)
3. **Create a branch** for your work
4. **Make your changes** following our standards
5. **Test thoroughly** on a test server
6. **Submit a pull request** with clear description

## Development Setup

### Building the Project

#### Option 1: Windows Batch Script
```batch
.\build.bat
```

#### Option 2: Maven
```bash
mvn clean compile package
```

#### Option 3: Manual Compilation
```bash
javac -cp "spigot-api-1.21.8-R0.1-20250729.092320-4.jar" -d target src/main/java/io/github/chiefboyardee/bedrockcombat/BedrockCombatPlugin.java
copy src/main/resources/plugin.yml target/
cd target && jar cf BedrockCombat-1.0.0.jar -C . .
```

### IDE Setup

#### IntelliJ IDEA
1. **Import** project as Maven project
2. **Add** Spigot API JAR to classpath
3. **Set** Java SDK to 8+
4. **Configure** run configuration for testing

#### Eclipse
1. **Import** as existing Maven project
2. **Add** Spigot API to build path
3. **Set** compiler compliance to 1.8+

## Coding Standards

### Java Code Style

- **Indentation**: 4 spaces (no tabs)
- **Line length**: 120 characters maximum
- **Naming conventions**:
  - Classes: `PascalCase`
  - Methods/Variables: `camelCase`
  - Constants: `UPPER_SNAKE_CASE`
  - Packages: `lowercase.with.dots`

### Code Quality Guidelines

```java
// Good: Clear, documented, and well-structured
/**
 * Toggles combat mode for the specified player.
 * 
 * @param player The player to toggle combat mode for
 * @return true if combat mode was enabled, false if disabled
 */
public boolean toggleCombatMode(Player player) {
    if (player == null) {
        return false;
    }
    
    AttributeInstance attribute = player.getAttribute(Attribute.ATTACK_SPEED);
    if (attribute == null) {
        return false;
    }
    
    boolean isBedrockMode = attribute.getBaseValue() > JAVA_ATTACK_SPEED;
    double newSpeed = isBedrockMode ? JAVA_ATTACK_SPEED : BEDROCK_ATTACK_SPEED;
    attribute.setBaseValue(newSpeed);
    
    return !isBedrockMode;
}

// Bad: Unclear, undocumented, and hard to maintain
public boolean toggle(Player p) {
    p.getAttribute(Attribute.ATTACK_SPEED).setBaseValue(
        p.getAttribute(Attribute.ATTACK_SPEED).getBaseValue() > 4.0 ? 4.0 : 1024.0
    );
    return true;
}
```

### Documentation Standards

- **JavaDoc** for all public methods and classes
- **Inline comments** for complex logic
- **README updates** for new features
- **Changelog entries** for all changes

## Testing

### Manual Testing

1. **Build** the plugin using build script
2. **Start** a test server (Spigot/Paper 1.20.4+)
3. **Install** the plugin in `plugins/` folder
4. **Test** all functionality:
   - Player join events
   - Command execution
   - Permission checks
   - Combat mode toggling
   - Auto-detection for Bedrock players

### Test Cases to Verify

- Plugin loads without errors
- Commands work with proper permissions
- Auto-detection works for players with names starting with '.'
- Combat mode toggles correctly
- Chat messages display properly
- No console errors during operation

## Submitting Changes

### Pull Request Process

1. **Create a branch** from `main`:
   ```bash
   git checkout -b feature/your-feature-name
   ```

2. **Make your changes** following coding standards

3. **Commit** with clear messages:
   ```bash
   git commit -m "feat: add combat mode persistence across server restarts"
   ```

4. **Push** to your fork:
   ```bash
   git push origin feature/your-feature-name
   ```

5. **Create** a pull request on GitHub

### Commit Message Format

We use conventional commits for clear history:

```
type(scope): description

[optional body]

[optional footer]
```

**Types:**
- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation changes
- `style`: Code style changes
- `refactor`: Code refactoring
- `test`: Adding tests
- `chore`: Maintenance tasks

**Examples:**
```
feat: add configuration file support
fix: resolve combat mode not persisting on server restart
docs: update README with new configuration options
style: format code according to style guide
```

### Pull Request Guidelines

- **Clear title** describing the change
- **Detailed description** of what was changed and why
- **Link related issues** using `Fixes #123` or `Closes #123`
- **Include screenshots** for UI changes
- **Test instructions** for reviewers
- **Update documentation** if needed

## Reporting Issues

### Before Reporting

1. **Search existing issues** to avoid duplicates
2. **Test on latest version** to ensure issue still exists
3. **Gather information** about your environment

### Issue Template

```markdown
**Bug Description**
A clear description of what the bug is.

**Steps to Reproduce**
1. Go to '...'
2. Click on '....'
3. Scroll down to '....'
4. See error

**Expected Behavior**
What you expected to happen.

**Screenshots**
If applicable, add screenshots.

**Environment:**
- Server Software: [e.g. Paper 1.20.4]
- Plugin Version: [e.g. 1.0.0]
- Java Version: [e.g. 17]
- Other Plugins: [list relevant plugins]

**Additional Context**
Any other context about the problem.
```

## Feature Requests

### Suggesting Features

1. **Check existing issues** for similar requests
2. **Create detailed proposal** with use cases
3. **Consider implementation complexity**
4. **Be open to discussion** and feedback

### Feature Request Template

```markdown
**Feature Description**
A clear description of the feature you'd like to see.

**Use Case**
Describe the problem this feature would solve.

**Proposed Solution**
How you envision this feature working.

**Alternatives Considered**
Other solutions you've considered.

**Additional Context**
Any other context or screenshots.
```

## Recognition

Contributors will be recognized in:

- **README.md** acknowledgments section
- **CHANGELOG.md** for significant contributions
- **GitHub releases** notes
- **Plugin credits** in future versions

## Getting Help

- **GitHub Discussions** for questions and ideas
- **GitHub Issues** for bugs and feature requests
- **Wiki** for detailed documentation

## License

By contributing to BedrockCombat, you agree that your contributions will be licensed under the MIT License.

---

Thank you for contributing to BedrockCombat! Your help makes this project better for everyone.