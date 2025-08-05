package io.github.chiefboyardee.bedrockcombat.commands;

import io.github.chiefboyardee.bedrockcombat.BedrockCombatPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Simplified command handler for BedrockCombatCompat
 * Handles plugin configuration and management commands
 */
public class ConfigCommand implements CommandExecutor, TabCompleter {
    
    private final BedrockCombatPlugin plugin;
    
    public ConfigCommand(BedrockCombatPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("bedrockcombat.admin")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            return true;
        }
        
        if (args.length == 0) {
            sendHelpMessage(sender);
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "reload":
                handleReload(sender);
                break;
            case "status":
                handleStatus(sender);
                break;
            case "performance":
                handlePerformance(sender);
                break;
            case "help":
                sendHelpMessage(sender);
                break;
            default:
                sender.sendMessage(ChatColor.RED + "Unknown subcommand. Use '/bedrockcombat help' for available commands.");
                break;
        }
        
        return true;
    }
    
    private void handleReload(CommandSender sender) {
        try {
            plugin.getConfigManager().reloadConfig();
            plugin.getIntegrationManager().reload();
            sender.sendMessage(ChatColor.GREEN + "Configuration reloaded successfully!");
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "Failed to reload configuration: " + e.getMessage());
        }
    }
    
    private void handleStatus(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "=== BedrockCombatCompat Status ===");
        sender.sendMessage(ChatColor.YELLOW + "Plugin Version: " + plugin.getDescription().getVersion());
        sender.sendMessage(ChatColor.YELLOW + "Bedrock Players Online: " + plugin.getBedrockPlayers().size());
        sender.sendMessage(ChatColor.YELLOW + "Players in PvP: " + plugin.getPlayersInPvP().size());
        sender.sendMessage(ChatColor.YELLOW + "PvP Detection: " + (plugin.isPvpDetectionEnabled() ? "Enabled" : "Disabled"));
        sender.sendMessage(ChatColor.YELLOW + "Database: " + (plugin.getDatabaseManager().isEnabled() ? "Enabled" : "Disabled"));
        sender.sendMessage(ChatColor.YELLOW + "Performance Monitor: " + (plugin.getPerformanceMonitor().isEnabled() ? "Enabled" : "Disabled"));
        sender.sendMessage(ChatColor.YELLOW + "Floodgate Integration: " + (plugin.getIntegrationManager().isFloodgateEnabled() ? "Available" : "Not Available"));
    }
    
    private void handlePerformance(CommandSender sender) {
        if (!plugin.getPerformanceMonitor().isEnabled()) {
            sender.sendMessage(ChatColor.RED + "Performance monitoring is disabled!");
            return;
        }
        
        String report = plugin.getPerformanceMonitor().generateReport();
        String[] lines = report.split("\n");
        for (String line : lines) {
            sender.sendMessage(ChatColor.AQUA + line);
        }
    }
    
    private void sendHelpMessage(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "=== BedrockCombatCompat Commands ===");
        sender.sendMessage(ChatColor.YELLOW + "/bedrockcombat reload" + ChatColor.WHITE + " - Reload the plugin configuration");
        sender.sendMessage(ChatColor.YELLOW + "/bedrockcombat status" + ChatColor.WHITE + " - Show plugin status");
        sender.sendMessage(ChatColor.YELLOW + "/bedrockcombat performance" + ChatColor.WHITE + " - Show performance statistics");
        sender.sendMessage(ChatColor.YELLOW + "/bedrockcombat help" + ChatColor.WHITE + " - Show this help message");
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!sender.hasPermission("bedrockcombat.admin")) {
            return new ArrayList<>();
        }
        
        if (args.length == 1) {
            List<String> subcommands = Arrays.asList("reload", "status", "performance", "help");
            List<String> completions = new ArrayList<>();
            
            for (String subcommand : subcommands) {
                if (subcommand.toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(subcommand);
                }
            }
            
            return completions;
        }
        
        return new ArrayList<>();
    }
}