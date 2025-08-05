package io.github.chiefboyardee.bedrockcombat.ui;

import io.github.chiefboyardee.bedrockcombat.BedrockCombatPlugin;
import io.github.chiefboyardee.bedrockcombat.config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages action bar notifications for combat status display
 * Provides real-time feedback on combat mode, PvP status, and transitions
 */
public class ActionBarManager {
    
    private final BedrockCombatPlugin plugin;
    private final ConfigManager configManager;
    
    // Action bar update task
    private BukkitTask updateTask;
    
    // Player-specific action bar states
    private final Map<UUID, ActionBarState> playerStates = new ConcurrentHashMap<>();
    
    // Reflection method for sending action bar (for compatibility)
    private Method sendActionBarMethod;
    private boolean actionBarSupported = false;
    
    public ActionBarManager(BedrockCombatPlugin plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        initializeActionBarSupport();
    }
    
    /**
     * Initialize action bar support using reflection for compatibility
     */
    private void initializeActionBarSupport() {
        try {
            // Try to find the sendActionBar method
            sendActionBarMethod = Player.class.getMethod("sendActionBar", String.class);
            actionBarSupported = true;
            plugin.getLogger().info("Action bar support detected and enabled");
        } catch (NoSuchMethodException e) {
            // Fallback: try with net.md_5.bungee.api.ChatMessageType
            try {
                Class<?> chatMessageType = Class.forName("net.md_5.bungee.api.ChatMessageType");
                Class<?> textComponent = Class.forName("net.md_5.bungee.api.chat.TextComponent");
                sendActionBarMethod = Player.class.getMethod("spigot");
                actionBarSupported = true;
                plugin.getLogger().info("Action bar support detected via Spigot API");
            } catch (Exception ex) {
                plugin.getLogger().warning("Action bar not supported on this server version - using chat messages");
                actionBarSupported = false;
            }
        }
    }
    
    /**
     * Start the action bar update system
     */
    public void start() {
        if (!configManager.isActionBarEnabled()) {
            return;
        }
        
        int updateInterval = configManager.getActionBarUpdateInterval();
        
        updateTask = new BukkitRunnable() {
            @Override
            public void run() {
                updateAllActionBars();
            }
        }.runTaskTimer(plugin, 0L, updateInterval);
        
        plugin.getLogger().info("Action bar system started with " + updateInterval + " tick interval");
    }
    
    /**
     * Stop the action bar update system
     */
    public void stop() {
        if (updateTask != null && !updateTask.isCancelled()) {
            updateTask.cancel();
        }
        playerStates.clear();
        plugin.getLogger().info("Action bar system stopped");
    }
    
    /**
     * Shuts down the action bar manager
     */
    public void shutdown() {
        stop();
    }
    
    /**
     * Update action bars for all online players
     */
    private void updateAllActionBars() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            updatePlayerActionBar(player);
        }
    }
    
    /**
     * Update action bar for a specific player
     */
    public void updatePlayerActionBar(Player player) {
        if (!configManager.isActionBarEnabled()) {
            return;
        }
        
        UUID playerId = player.getUniqueId();
        ActionBarState state = getOrCreatePlayerState(playerId);
        
        // Determine current combat mode
        String combatMode = plugin.getIntegrationManager().isBedrockPlayer(player) ? "Bedrock" : "Java";
        boolean inPvP = plugin.getPvPDetectionSystem().isInPvP(player);
        
        // Build action bar message
        String message = buildActionBarMessage(combatMode, inPvP, state);
        
        // Send action bar
        sendActionBar(player, message);
        
        // Update state
        state.lastCombatMode = combatMode;
        state.lastPvPStatus = inPvP;
    }
    
    /**
     * Build the action bar message based on current state
     */
    private String buildActionBarMessage(String combatMode, boolean inPvP, ActionBarState state) {
        StringBuilder message = new StringBuilder();
        
        // Combat mode indicator
        if (configManager.showCombatModeInActionBar()) {
            String modeColor = "Bedrock".equals(combatMode) ? "§a" : "§b";
            message.append("§7Combat: ").append(modeColor).append(combatMode).append(" ");
        }
        
        // PvP status indicator
        if (configManager.showPvPStatusInActionBar() && inPvP) {
            message.append("§c⚔ PvP Mode §c");
            
            // Add PvP timeout countdown if available
            int timeoutSeconds = plugin.getConfigManager().getPvpTimeout();
            if (timeoutSeconds > 0) {
                message.append(" (").append(timeoutSeconds).append("s)");
            }
        }
        
        // Mode transition indicator
        if (!combatMode.equals(state.lastCombatMode)) {
            message.append(" §e→ ").append(combatMode);
        }
        
        // Clean up and apply color codes
        String result = message.toString().trim();
        if (result.isEmpty()) {
            result = "§7Combat Ready";
        }
        
        return result.replace("&", "§");
    }
    
    /**
     * Send action bar message to player
     */
    private void sendActionBar(Player player, String message) {
        if (!actionBarSupported) {
            // Fallback to chat message (less intrusive)
            return;
        }
        
        try {
            if (sendActionBarMethod.getName().equals("sendActionBar")) {
                // Direct method call
                sendActionBarMethod.invoke(player, message);
            } else {
                // Spigot API method
                Object spigot = sendActionBarMethod.invoke(player);
                Method sendMessage = spigot.getClass().getMethod("sendMessage", 
                    Class.forName("net.md_5.bungee.api.ChatMessageType"),
                    Class.forName("net.md_5.bungee.api.chat.BaseComponent"));
                
                Object chatMessageType = Class.forName("net.md_5.bungee.api.ChatMessageType")
                    .getField("ACTION_BAR").get(null);
                Object textComponent = Class.forName("net.md_5.bungee.api.chat.TextComponent")
                    .getConstructor(String.class).newInstance(message);
                
                sendMessage.invoke(spigot, chatMessageType, textComponent);
            }
        } catch (Exception e) {
            // Silently fail - action bar is not critical
            plugin.getLogger().fine("Failed to send action bar: " + e.getMessage());
        }
    }
    
    /**
     * Notify about combat mode change
     */
    public void notifyCombatModeChange(Player player, String newMode) {
        if (!configManager.isActionBarEnabled()) {
            return;
        }
        
        String message = "§e⚡ Combat Mode: §f" + newMode;
        sendActionBar(player, message);
        
        // Schedule immediate update
        Bukkit.getScheduler().runTaskLater(plugin, () -> updatePlayerActionBar(player), 40L); // 2 seconds
    }
    
    /**
     * Notify about PvP mode entry
     */
    public void notifyPvPEntry(Player player) {
        if (!configManager.isActionBarEnabled()) {
            return;
        }
        
        String message = "§c⚔ Entering PvP Mode!";
        sendActionBar(player, message);
        
        // Schedule immediate update
        Bukkit.getScheduler().runTaskLater(plugin, () -> updatePlayerActionBar(player), 20L); // 1 second
    }
    
    /**
     * Notify about PvP mode exit
     */
    public void notifyPvPExit(Player player) {
        if (!configManager.isActionBarEnabled()) {
            return;
        }
        
        String message = "§a✓ PvP Mode Ended";
        sendActionBar(player, message);
        
        // Schedule immediate update
        Bukkit.getScheduler().runTaskLater(plugin, () -> updatePlayerActionBar(player), 20L); // 1 second
    }
    
    /**
     * Removes a player from action bar updates
     */
    public void removePlayer(UUID playerId) {
        playerStates.remove(playerId);
    }
    
    /**
     * Starts action bar updates for a player
     */
    public void startActionBarUpdates(Player player) {
        if (configManager.isActionBarEnabled()) {
            updatePlayerActionBar(player);
        }
    }
    
    /**
     * Stops action bar updates for a player
     */
    public void stopActionBarUpdates(Player player) {
        removePlayer(player.getUniqueId());
    }
    
    /**
     * Updates the player's status and refreshes their action bar
     */
    public void updatePlayerStatus(Player player) {
        if (configManager.isActionBarEnabled()) {
            updatePlayerActionBar(player);
        }
    }
    
    /**
     * Get or create player state
     */
    private ActionBarState getOrCreatePlayerState(UUID playerId) {
        return playerStates.computeIfAbsent(playerId, k -> new ActionBarState());
    }
    
    /**
     * Player-specific action bar state
     */
    private static class ActionBarState {
        String lastCombatMode = "";
        boolean lastPvPStatus = false;
        long lastUpdate = 0;
    }
}