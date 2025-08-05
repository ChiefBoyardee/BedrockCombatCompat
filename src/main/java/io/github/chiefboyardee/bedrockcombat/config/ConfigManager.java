package io.github.chiefboyardee.bedrockcombat.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Manages all configuration options for BedrockCombatCompat
 * Provides centralized access to config values with validation and defaults
 */
public class ConfigManager {
    
    private final JavaPlugin plugin;
    private FileConfiguration config;
    
    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Loads the configuration from config.yml
     */
    public void loadConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        this.config = plugin.getConfig();
        validateConfig();
    }
    
    /**
     * Saves the current configuration to config.yml
     */
    public void saveConfig() {
        plugin.saveConfig();
    }
    
    /**
     * Reloads the configuration from disk
     */
    public void reloadConfig() {
        plugin.reloadConfig();
        this.config = plugin.getConfig();
        validateConfig();
    }
    
    // General Settings
    public boolean isPluginEnabled() {
        return config.getBoolean("general.enabled", true);
    }
    
    public boolean isDebugMode() {
        return config.getBoolean("general.debug", false);
    }
    
    public String getLanguage() {
        return config.getString("general.language", "en");
    }
    
    public boolean isMetricsEnabled() {
        return config.getBoolean("general.metrics", true);
    }
    
    public boolean isUpdateCheckerEnabled() {
        return config.getBoolean("general.update-checker", true);
    }
    
    // Combat Settings
    public double getBedrockAttackSpeed() {
        return config.getDouble("combat.bedrock-attack-speed", 1024.0);
    }
    
    public double getJavaAttackSpeed() {
        return config.getDouble("combat.java-attack-speed", 4.0);
    }
    
    public String getBedrockCombatMode() {
        return config.getString("combat.bedrock-mode", "fast");
    }
    
    public String getJavaCombatMode() {
        return config.getString("combat.java-mode", "default");
    }
    
    public boolean isCustomAttackSpeedEnabled() {
        return config.getBoolean("combat.custom-attack-speeds.enabled", false);
    }
    
    // Player Detection Settings
    public boolean isPlayerDetectionEnabled() {
        return config.getBoolean("detection.enabled", true);
    }
    
    public boolean isFloodgateDetectionEnabled() {
        return config.getBoolean("detection.floodgate.enabled", true);
    }
    
    public String getFloodgatePrefix() {
        return config.getString("detection.floodgate.prefix", ".");
    }
    
    public boolean isManualDetectionEnabled() {
        return config.getBoolean("detection.manual.enabled", true);
    }
    
    public List<String> getManualBedrockPlayers() {
        return config.getStringList("detection.manual.bedrock-players");
    }
    
    public String getBedrockPrefix() {
        return config.getString("detection.bedrock-prefix", ".");
    }
    
    public boolean isCustomRulesEnabled() {
        return config.getBoolean("detection.custom-rules.enabled", false);
    }
    
    // PvP Settings
    public boolean isPvpDetectionEnabled() {
        return config.getBoolean("pvp.detection.enabled", true);
    }
    
    public boolean isPvpBalanceEnabled() {
        return config.getBoolean("pvp.balance.enabled", true);
    }
    
    public String getPvpMode() {
        return config.getString("pvp.balance.mode", "java");
    }
    
    public int getPvpTimeout() {
        return config.getInt("pvp.timeout", 10);
    }
    
    public boolean isPvpNotificationsEnabled() {
        return config.getBoolean("pvp.notifications.enabled", true);
    }
    
    // World Settings
    public boolean isPvpEnabledInWorld(String worldName) {
        ConfigurationSection worldSection = config.getConfigurationSection("worlds." + worldName);
        if (worldSection != null) {
            return worldSection.getBoolean("pvp-enabled", true);
        }
        return config.getBoolean("worlds.default.pvp-enabled", true);
    }
    
    public Set<String> getEnabledWorlds() {
        ConfigurationSection worldsSection = config.getConfigurationSection("worlds");
        if (worldsSection != null) {
            return worldsSection.getKeys(false);
        }
        return Set.of();
    }
    
    // Integration Settings
    public boolean isWorldGuardEnabled() {
        return config.getBoolean("integrations.worldguard.enabled", true);
    }
    
    public boolean isEssentialsEnabled() {
        return config.getBoolean("integrations.essentials.enabled", true);
    }
    
    public boolean isPlaceholderAPIEnabled() {
        return config.getBoolean("integrations.placeholderapi.enabled", true);
    }
    
    public boolean isDiscordEnabled() {
        return config.getBoolean("integrations.discord.enabled", false);
    }
    
    public String getDiscordWebhook() {
        return config.getString("integrations.discord.webhook", "");
    }
    
    // UI Settings
    public boolean isWelcomeMessageEnabled() {
        return config.getBoolean("ui.messages.welcome.enabled", true);
    }
    
    public String getWelcomeMessage() {
        return config.getString("ui.messages.welcome.text", "&a&lBedrockCombat &7> &aController-friendly combat enabled for {player}!");
    }
    
    public boolean isActionBarEnabled() {
        return config.getBoolean("ui.action-bar.enabled", true);
    }
    
    public String getActionBarMessage() {
        return config.getString("ui.action-bar.text", "&7Combat Mode: &e{mode}");
    }
    
    public boolean isBossBarEnabled() {
        return config.getBoolean("ui.boss-bar.enabled", false);
    }
    
    public int getActionBarUpdateInterval() {
        return config.getInt("ui.actionbar.update-interval", 20);
    }
    
    public boolean showCombatModeInActionBar() {
        return config.getBoolean("ui.actionbar.show-combat-mode", true);
    }
    
    public boolean showPvPStatusInActionBar() {
        return config.getBoolean("ui.actionbar.show-pvp-status", true);
    }
    
    // Performance Settings
    public boolean isPerformanceMonitoringEnabled() {
        return config.getBoolean("performance.monitoring.enabled", true);
    }
    
    public int getPerformanceCheckInterval() {
        return config.getInt("performance.monitoring.check-interval", 60);
    }
    
    public boolean isAsyncProcessingEnabled() {
        return config.getBoolean("performance.async-processing", true);
    }
    
    public boolean isCachingEnabled() {
        return config.getBoolean("performance.caching.enabled", true);
    }
    
    public int getCacheSize() {
        return config.getInt("performance.caching.max-size", 1000);
    }
    
    // Advanced Settings
    public boolean isExperimentalFeaturesEnabled() {
        return config.getBoolean("advanced.experimental.enabled", false);
    }
    
    public boolean isAPIEnabled() {
        return config.getBoolean("advanced.api.enabled", true);
    }
    
    public boolean isDatabaseEnabled() {
        return config.getBoolean("advanced.database.enabled", false);
    }
    
    public String getDatabaseType() {
        return config.getString("advanced.database.type", "sqlite");
    }
    
    public String getDatabaseHost() {
        return config.getString("advanced.database.host", "localhost");
    }
    
    public int getDatabasePort() {
        return config.getInt("advanced.database.port", 3306);
    }
    
    public String getDatabaseName() {
        return config.getString("advanced.database.database", "bedrockcombat");
    }
    
    public String getDatabaseUsername() {
        return config.getString("advanced.database.username", "");
    }
    
    public String getDatabasePassword() {
        return config.getString("advanced.database.password", "");
    }
    
    // Compatibility Settings
    public String getMinecraftVersion() {
        return config.getString("compatibility.minecraft-version", "auto");
    }
    
    public boolean isLegacySupport() {
        return config.getBoolean("compatibility.legacy-support", false);
    }
    
    // Utility Methods
    public void setValue(String path, Object value) {
        config.set(path, value);
        saveConfig();
    }
    
    public Object getValue(String path) {
        return config.get(path);
    }
    
    public Object getValue(String path, Object defaultValue) {
        return config.get(path, defaultValue);
    }
    
    public ConfigurationSection getSection(String path) {
        return config.getConfigurationSection(path);
    }
    
    public boolean hasPath(String path) {
        return config.contains(path);
    }
    
    /**
     * Validates the configuration and sets defaults for missing values
     */
    private void validateConfig() {
        boolean needsSave = false;
        
        // Check for required sections and add defaults if missing
        if (!config.contains("general")) {
            config.createSection("general");
            needsSave = true;
        }
        
        if (!config.contains("combat")) {
            config.createSection("combat");
            needsSave = true;
        }
        
        if (!config.contains("detection")) {
            config.createSection("detection");
            needsSave = true;
        }
        
        if (!config.contains("pvp")) {
            config.createSection("pvp");
            needsSave = true;
        }
        
        // Validate critical values
        if (getBedrockAttackSpeed() <= 0) {
            setValue("combat.bedrock-attack-speed", 1024.0);
            needsSave = true;
        }
        
        if (getJavaAttackSpeed() <= 0) {
            setValue("combat.java-attack-speed", 4.0);
            needsSave = true;
        }
        
        if (getPvpTimeout() < 0) {
            setValue("pvp.timeout", 10);
            needsSave = true;
        }
        
        if (needsSave) {
            saveConfig();
            plugin.getLogger().info("Configuration validated and updated with missing defaults");
        }
    }
    
    /**
     * Migrates old configuration format to new format
     */
    public void migrateConfig() {
        boolean needsMigration = false;
        
        // Check for old config keys and migrate them
        if (config.contains("pvpDetectionEnabled")) {
            setValue("pvp.detection.enabled", config.getBoolean("pvpDetectionEnabled"));
            config.set("pvpDetectionEnabled", null);
            needsMigration = true;
        }
        
        if (config.contains("pvpTimeoutSeconds")) {
            setValue("pvp.timeout", config.getInt("pvpTimeoutSeconds"));
            config.set("pvpTimeoutSeconds", null);
            needsMigration = true;
        }
        
        if (needsMigration) {
            saveConfig();
            plugin.getLogger().info("Configuration migrated to new format");
        }
    }
}