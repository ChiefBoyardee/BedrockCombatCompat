package io.github.chiefboyardee.bedrockcombat.integrations;

import io.github.chiefboyardee.bedrockcombat.config.ConfigManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Simplified integration manager for BedrockCombatCompat
 * Handles integration with other plugins
 */
public class IntegrationManager {
    
    private final JavaPlugin plugin;
    private final ConfigManager configManager;
    private boolean floodgateEnabled;
    
    public IntegrationManager(JavaPlugin plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }
    
    /**
     * Initializes all integrations
     */
    public void initialize() {
        // Check for Floodgate
        floodgateEnabled = plugin.getServer().getPluginManager().getPlugin("floodgate") != null;
        
        if (floodgateEnabled) {
            plugin.getLogger().info("Floodgate integration enabled");
        } else {
            plugin.getLogger().info("Floodgate not found - using fallback detection");
        }
    }
    
    /**
     * Checks if a player is a Bedrock player using available integrations
     */
    public boolean isBedrockPlayer(Player player) {
        if (floodgateEnabled) {
            try {
                // Use reflection to avoid compile-time dependency on Floodgate API
                Class<?> floodgateApiClass = Class.forName("org.geysermc.floodgate.api.FloodgateApi");
                Object apiInstance = floodgateApiClass.getMethod("getInstance").invoke(null);
                Boolean isFloodgatePlayer = (Boolean) floodgateApiClass
                    .getMethod("isFloodgatePlayer", java.util.UUID.class)
                    .invoke(apiInstance, player.getUniqueId());
                return isFloodgatePlayer != null && isFloodgatePlayer;
            } catch (Exception e) {
                plugin.getLogger().warning("Failed to check Floodgate status for " + player.getName() + ": " + e.getMessage());
                // Fall through to prefix detection
            }
        }
        
        // Fallback to prefix detection
        String prefix = configManager.getFloodgatePrefix();
        return player.getName().startsWith(prefix);
    }
    
    /**
     * Disables all integrations
     */
    public void disable() {
        plugin.getLogger().info("Integration manager disabled");
    }
    
    /**
     * Reloads all integrations
     */
    public void reload() {
        disable();
        initialize();
    }
    
    /**
     * Checks if Floodgate integration is available
     */
    public boolean isFloodgateEnabled() {
        return floodgateEnabled;
    }
}