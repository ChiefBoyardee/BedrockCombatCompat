package io.github.chiefboyardee.bedrockcombat.pvp;

import io.github.chiefboyardee.bedrockcombat.config.ConfigManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Simplified PvP detection system for BedrockCombatCompat
 * Tracks player combat states and PvP sessions
 */
public class PvPDetectionSystem {
    
    private final JavaPlugin plugin;
    private final ConfigManager configManager;
    private final Map<UUID, Long> pvpTimestamps;
    private final Map<UUID, UUID> lastAttacker;
    private boolean enabled;
    
    public PvPDetectionSystem(JavaPlugin plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.pvpTimestamps = new HashMap<>();
        this.lastAttacker = new HashMap<>();
    }
    
    /**
     * Initializes the PvP detection system
     */
    public void initialize() {
        this.enabled = configManager.isPvpDetectionEnabled();
        
        if (enabled) {
            plugin.getLogger().info("PvP detection system enabled");
        } else {
            plugin.getLogger().info("PvP detection system disabled");
        }
    }
    
    /**
     * Handles a PvP event between two players
     */
    public void handlePvPEvent(Player attacker, Player victim) {
        recordPvPEvent(attacker, victim);
    }
    
    /**
     * Records a PvP event between two players
     */
    public void recordPvPEvent(Player attacker, Player victim) {
        if (!enabled) return;
        
        long currentTime = System.currentTimeMillis();
        
        // Record PvP timestamp for both players
        pvpTimestamps.put(attacker.getUniqueId(), currentTime);
        pvpTimestamps.put(victim.getUniqueId(), currentTime);
        
        // Track who attacked whom
        lastAttacker.put(victim.getUniqueId(), attacker.getUniqueId());
        
        plugin.getLogger().fine("PvP event recorded: " + attacker.getName() + " -> " + victim.getName());
    }
    
    /**
     * Checks if a player is currently in PvP
     */
    public boolean isInPvP(Player player) {
        if (!enabled) return false;
        
        Long lastPvpTime = pvpTimestamps.get(player.getUniqueId());
        if (lastPvpTime == null) return false;
        
        long pvpTimeout = configManager.getPvpTimeout() * 1000L; // Convert to milliseconds
        return (System.currentTimeMillis() - lastPvpTime) < pvpTimeout;
    }
    
    /**
     * Gets the last attacker of a player
     */
    public UUID getLastAttacker(Player player) {
        return lastAttacker.get(player.getUniqueId());
    }
    
    /**
     * Removes a player from PvP state (e.g., when they log out)
     */
    public void removeFromPvP(Player player) {
        UUID playerId = player.getUniqueId();
        pvpTimestamps.remove(playerId);
        lastAttacker.remove(playerId);
        
        // Also remove this player as an attacker for others
        lastAttacker.entrySet().removeIf(entry -> entry.getValue().equals(playerId));
    }
    
    /**
     * Gets the number of players currently in PvP
     */
    public int getPlayersInPvPCount() {
        if (!enabled) return 0;
        
        long currentTime = System.currentTimeMillis();
        long pvpTimeout = configManager.getPvpTimeout() * 1000L;
        
        return (int) pvpTimestamps.entrySet().stream()
                .filter(entry -> (currentTime - entry.getValue()) < pvpTimeout)
                .count();
    }
    
    /**
     * Cleans up expired PvP entries
     */
    public void cleanupExpiredEntries() {
        if (!enabled) return;
        
        long currentTime = System.currentTimeMillis();
        long pvpTimeout = configManager.getPvpTimeout() * 1000L;
        
        pvpTimestamps.entrySet().removeIf(entry -> 
            (currentTime - entry.getValue()) >= pvpTimeout);
        
        // Clean up last attacker entries for expired PvP states
        lastAttacker.entrySet().removeIf(entry -> 
            !pvpTimestamps.containsKey(entry.getKey()));
    }
    
    /**
     * Checks if PvP detection is enabled
     */
    public boolean isEnabled() {
        return enabled;
    }
    
    /**
     * Shuts down the PvP detection system
     */
    public void shutdown() {
        pvpTimestamps.clear();
        lastAttacker.clear();
        plugin.getLogger().info("PvP detection system shutdown");
    }
}