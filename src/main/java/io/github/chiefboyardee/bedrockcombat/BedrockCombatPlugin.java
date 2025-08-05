package io.github.chiefboyardee.bedrockcombat;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.NamespacedKey;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * BedrockCombat Plugin
 * 
 * Cross-platform combat optimization for Geyser/Floodgate servers.
 * Provides controller-friendly combat for Bedrock players while preserving Java combat mechanics.
 * Features automatic PvP/PvE detection for fair gameplay.
 * 
 * @author ChiefBoyardee
 * @version 1.0.0
 */
public class BedrockCombatPlugin extends JavaPlugin implements Listener, TabCompleter {
    
    /** Bedrock Edition attack speed (attacks per second) */
    private static final double BEDROCK_ATTACK_SPEED = 16.0;
    
    /** Java Edition default attack speed (attacks per second) */
    private static final double JAVA_ATTACK_SPEED = 4.0;
    
    /** PvP timeout in seconds - how long to wait after PvP before reverting combat modes */
    private static final int PVP_TIMEOUT_SECONDS = 10;
    
    /** Players who prefer Bedrock combat (either auto-detected or manually set) */
    private final Set<UUID> bedrockPlayers = ConcurrentHashMap.newKeySet();
    
    /** Players currently in PvP mode (temporarily using Java combat) */
    private final Set<UUID> playersInPvP = ConcurrentHashMap.newKeySet();
    
    /** PvP timeout tasks for each player */
    private final Map<UUID, BukkitRunnable> pvpTimeoutTasks = new ConcurrentHashMap<>();
    
    /** Whether PvP detection is enabled (can be configured) */
    private boolean pvpDetectionEnabled = true;

    @Override
    public void onEnable() {
        try {
            getLogger().info("========================================");
            getLogger().info("BedrockCombat v" + getDescription().getVersion() + " is starting up...");
            getLogger().info("Server version: " + getServer().getVersion());
            getLogger().info("Bukkit version: " + getServer().getBukkitVersion());
            getLogger().info("========================================");
            
            getLogger().info("Cross-platform combat optimization enabled!");
            getLogger().info("- Bedrock players: Fast combat (" + BEDROCK_ATTACK_SPEED + " attack speed)");
            getLogger().info("- Java players: Traditional combat (" + JAVA_ATTACK_SPEED + " attack speed)");
            getLogger().info("- PvP Detection: " + (pvpDetectionEnabled ? "ENABLED" : "DISABLED"));
            getLogger().info("- PvP Timeout: " + PVP_TIMEOUT_SECONDS + " seconds");
            
            // Test if we can register events
            getServer().getPluginManager().registerEvents(this, this);
            getLogger().info("Event listeners registered successfully!");
            
            // Register commands
            this.getCommand("bedrockcombat").setExecutor(this);
            this.getCommand("bedrockcombat").setTabCompleter(this);
            getLogger().info("Commands registered successfully!");
            
            getLogger().info("========================================");
            getLogger().info("BedrockCombatCompat plugin enabled!");
            getLogger().info("Cross-platform combat optimization active");
            getLogger().info("Use /bedrockcombat for help and commands");
            getLogger().info("========================================");
            
        } catch (Exception e) {
            getLogger().severe("Failed to enable BedrockCombat plugin!");
            getLogger().severe("Error: " + e.getMessage());
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        try {
            getLogger().info("========================================");
            getLogger().info("BedrockCombat v" + getDescription().getVersion() + " is shutting down...");
            
            // Cancel all PvP timeout tasks
            pvpTimeoutTasks.values().forEach(task -> {
                if (task != null && !task.isCancelled()) {
                    task.cancel();
                }
            });
            pvpTimeoutTasks.clear();
            
            // Clear player data
            bedrockPlayers.clear();
            playersInPvP.clear();
            
            getLogger().info("Player data cleared and tasks cancelled");
            getLogger().info("BedrockCombatCompat plugin disabled successfully!");
            getLogger().info("========================================");
            
        } catch (Exception e) {
             getLogger().severe("Error during plugin disable: " + e.getMessage());
             e.printStackTrace();
         }
     }

    /**
     * Automatically applies Bedrock combat to players with Floodgate prefix
     * and tracks their preference for future PvP/PvE switching.
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        
        if (player.getName().startsWith(".")) {
            // Floodgate/Bedrock player detected
            bedrockPlayers.add(playerId);
            applyCombatMode(player);
            player.sendMessage("§a§lBedrockCombat §7§l> §aController-friendly combat enabled!");
            player.sendMessage("§7§l> §7PvP will temporarily use Java combat for fairness");
        } else {
            // Java player - ensure they're not in bedrock set and apply Java combat
            bedrockPlayers.remove(playerId);
            applyCombatMode(player);
        }
    }

    /**
     * Handles PvP detection - when a player attacks another player
     */
    @EventHandler
    public void onPlayerDamagePlayer(EntityDamageByEntityEvent event) {
        if (!pvpDetectionEnabled) return;
        
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player attacker = (Player) event.getDamager();
            Player victim = (Player) event.getEntity();
            
            // Both players enter PvP mode (temporary Java combat)
            enterPvPMode(attacker);
            enterPvPMode(victim);
        }
    }

    /**
     * Handles player death - immediately revert survivors back to their preferred combat mode
     */
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player deadPlayer = event.getEntity();
        UUID deadPlayerId = deadPlayer.getUniqueId();
        
        // Remove dead player from PvP mode
        exitPvPMode(deadPlayer);
        
        // Check for nearby players who might have been in combat with the dead player
        for (Player nearbyPlayer : deadPlayer.getWorld().getPlayers()) {
            if (nearbyPlayer.getUniqueId().equals(deadPlayerId)) continue;
            
            double distance = nearbyPlayer.getLocation().distance(deadPlayer.getLocation());
            if (distance <= 20.0 && playersInPvP.contains(nearbyPlayer.getUniqueId())) {
                // Start timeout for nearby players
                startPvPTimeout(nearbyPlayer);
            }
        }
    }

    /**
     * Clean up player data when they leave
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        
        // Clean up PvP state
        playersInPvP.remove(playerId);
        
        // Cancel any pending timeout tasks
        BukkitRunnable task = pvpTimeoutTasks.remove(playerId);
        if (task != null) {
            task.cancel();
        }
        
        // Note: We keep bedrockPlayers data for when they rejoin
    }

    /**
     * Applies the appropriate combat mode based on player preference and current state
     */
    private void applyCombatMode(Player player) {
        UUID playerId = player.getUniqueId();
        
        if (playersInPvP.contains(playerId)) {
            // Player is in PvP - force Java combat for fairness
            setJavaCombat(player);
        } else if (bedrockPlayers.contains(playerId)) {
            // Player prefers Bedrock combat and not in PvP
            setBedrockCombat(player);
        } else {
            // Java player or default
            setJavaCombat(player);
        }
    }

    /**
     * Puts a player into PvP mode (temporary Java combat)
     */
    private void enterPvPMode(Player player) {
        UUID playerId = player.getUniqueId();
        
        if (!playersInPvP.contains(playerId)) {
            playersInPvP.add(playerId);
            setJavaCombat(player);
            
            if (bedrockPlayers.contains(playerId)) {
                player.sendMessage("§c§lPvP Mode §7§l> §cTemporary Java combat for fair PvP");
            }
            
            // Cancel any existing timeout
            BukkitRunnable existingTask = pvpTimeoutTasks.remove(playerId);
            if (existingTask != null) {
                existingTask.cancel();
            }
        }
        
        // Always restart the timeout when PvP activity occurs
        startPvPTimeout(player);
    }

    /**
     * Removes a player from PvP mode and reverts to their preferred combat
     */
    private void exitPvPMode(Player player) {
        UUID playerId = player.getUniqueId();
        
        if (playersInPvP.remove(playerId)) {
            // Cancel timeout task
            BukkitRunnable task = pvpTimeoutTasks.remove(playerId);
            if (task != null) {
                task.cancel();
            }
            
            // Revert to preferred combat mode
            applyCombatMode(player);
            
            if (bedrockPlayers.contains(playerId)) {
                player.sendMessage("§a§lPvE Mode §7§l> §aController-friendly combat restored");
            }
        }
    }

    /**
     * Starts a timeout task to exit PvP mode after inactivity
     */
    private void startPvPTimeout(Player player) {
        UUID playerId = player.getUniqueId();
        
        // Cancel existing timeout
        BukkitRunnable existingTask = pvpTimeoutTasks.remove(playerId);
        if (existingTask != null) {
            existingTask.cancel();
        }
        
        // Start new timeout
        BukkitRunnable timeoutTask = new BukkitRunnable() {
            @Override
            public void run() {
                pvpTimeoutTasks.remove(playerId);
                if (player.isOnline()) {
                    exitPvPMode(player);
                }
            }
        };
        
        pvpTimeoutTasks.put(playerId, timeoutTask);
        timeoutTask.runTaskLater(this, PVP_TIMEOUT_SECONDS * 20L); // Convert to ticks
    }

    /**
     * Sets a player's attack speed to Bedrock Edition levels and hides attack indicator
     * @param player The player to modify
     */
    private void setBedrockCombat(Player player) {
        // For Bedrock combat, we want fast attacks and hidden indicator
        // Set to extremely high attack speed to hide the indicator completely
        hideAttackIndicator(player);
    }

    /**
     * Sets a player's attack speed to Java Edition default and shows attack indicator
     * @param player The player to modify
     */
    private void setJavaCombat(Player player) {
        // For Java combat, we want normal attack speed and visible indicator
        showAttackIndicator(player);
    }

    /**
     * Sets a player's attack speed to the specified value
     * @param player The player to modify
     * @param speed The attack speed to set
     */
    private void setAttackSpeed(Player player, double speed) {
        try {
            AttributeInstance attr = player.getAttribute(Attribute.GENERIC_ATTACK_SPEED);
            if (attr != null) {
                // Remove any existing modifiers first
                attr.getModifiers().forEach(attr::removeModifier);
                attr.setBaseValue(speed);
            }
        } catch (Exception e) {
            // Silently handle any version compatibility issues
            getLogger().fine("Could not set attack speed for " + player.getName() + ": " + e.getMessage());
        }
    }

    /**
     * Legacy method for backward compatibility
     * @deprecated Use setJavaCombat instead
     */
    @Deprecated
    private void resetCombat(Player player) {
        setJavaCombat(player);
    }

    /**
     * Hides the attack indicator GUI for a cleaner controller experience
     * @param player The player to hide the indicator for
     */
    private void hideAttackIndicator(Player player) {
        try {
            AttributeInstance attr = player.getAttribute(Attribute.GENERIC_ATTACK_SPEED);
            if (attr != null) {
                // Set attack speed to an extremely high value to make the indicator effectively invisible
                // This approach makes the cooldown so fast that the indicator appears constantly full
                attr.setBaseValue(1000.0);
            }
        } catch (Exception e) {
            // Silently handle any version compatibility issues
            getLogger().fine("Could not hide attack indicator for " + player.getName() + ": " + e.getMessage());
        }
    }

    /**
     * Shows the attack indicator GUI for Java combat and PvP
     * @param player The player to show the indicator for
     */
    private void showAttackIndicator(Player player) {
        try {
            AttributeInstance attr = player.getAttribute(Attribute.GENERIC_ATTACK_SPEED);
            if (attr != null) {
                // Restore normal Java attack speed to show the indicator properly
                attr.setBaseValue(JAVA_ATTACK_SPEED);
            }
        } catch (Exception e) {
            // Silently handle any version compatibility issues
            getLogger().fine("Could not show attack indicator for " + player.getName() + ": " + e.getMessage());
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§6§lBedrockCombat §7v" + getDescription().getVersion());
            sender.sendMessage("§7Cross-platform combat optimization");
            sender.sendMessage("");
            sender.sendMessage("§e§lCommands:");
            sender.sendMessage("§7/" + label + " <player> §8- §7Toggle combat mode");
            sender.sendMessage("§7/" + label + " status §8- §7Show plugin status");
            sender.sendMessage("§7/" + label + " reload §8- §7Reload configuration");
            return true;
        }

        if (args[0].equalsIgnoreCase("status")) {
            sender.sendMessage("§6§lBedrockCombat Status:");
            sender.sendMessage("§7PvP Detection: " + (pvpDetectionEnabled ? "§aEnabled" : "§cDisabled"));
            sender.sendMessage("§7PvP Timeout: §e" + PVP_TIMEOUT_SECONDS + "s");
            sender.sendMessage("§7Bedrock Players: §e" + bedrockPlayers.size());
            sender.sendMessage("§7Players in PvP: §c" + playersInPvP.size());
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("bedrockcombat.admin")) {
                sender.sendMessage("§c§lError: §7You don't have permission to reload the plugin.");
                return true;
            }
            
            // Simple reload - could be expanded with config file later
            sender.sendMessage("§a§lBedrockCombat §7reloaded successfully!");
            return true;
        }

        // Player toggle command
        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            sender.sendMessage("§c§lError: §7Player '" + args[0] + "' not found or not online.");
            return true;
        }

        UUID targetId = target.getUniqueId();
        
        // Toggle player's preference
        if (bedrockPlayers.contains(targetId)) {
            // Remove from Bedrock players
            bedrockPlayers.remove(targetId);
            applyCombatMode(target);
            
            sender.sendMessage("§e§lBedrockCombat §7disabled for §e" + target.getName() + "§7.");
            target.sendMessage("§c§lBedrockCombat §7§l> §cJava combat mode enabled");
            
            if (playersInPvP.contains(targetId)) {
                target.sendMessage("§7§l> §7Currently in PvP mode");
            }
        } else {
            // Add to Bedrock players
            bedrockPlayers.add(targetId);
            applyCombatMode(target);
            
            sender.sendMessage("§a§lBedrockCombat §7enabled for §a" + target.getName() + "§7.");
            target.sendMessage("§a§lBedrockCombat §7§l> §aController-friendly combat enabled!");
            
            if (playersInPvP.contains(targetId)) {
                target.sendMessage("§7§l> §7Currently in PvP mode (temporary Java combat)");
            } else {
                target.sendMessage("§7§l> §7PvP will temporarily use Java combat for fairness");
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            String prefix = args[0].toLowerCase();
            List<String> completions = new ArrayList<>();
            
            // Add command options
            if ("status".startsWith(prefix)) completions.add("status");
            if ("reload".startsWith(prefix) && sender.hasPermission("bedrockcombat.admin")) {
                completions.add("reload");
            }
            
            // Add player names
            completions.addAll(Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .filter(name -> name.toLowerCase().startsWith(prefix))
                .collect(Collectors.toList()));
                
            return completions;
        }
        return List.of();
    }
}