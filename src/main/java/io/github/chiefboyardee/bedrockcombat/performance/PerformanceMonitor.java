package io.github.chiefboyardee.bedrockcombat.performance;

import io.github.chiefboyardee.bedrockcombat.config.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

/**
 * Simplified performance monitor for BedrockCombatCompat
 * Tracks basic performance metrics
 */
public class PerformanceMonitor {
    
    private final JavaPlugin plugin;
    private final ConfigManager configManager;
    private final Map<String, Integer> operationCounts;
    private boolean enabled;
    private long startTime;
    
    public PerformanceMonitor(JavaPlugin plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.operationCounts = new HashMap<>();
        this.startTime = System.currentTimeMillis();
    }
    
    /**
     * Initializes performance monitoring
     */
    public void initialize() {
        this.enabled = configManager.isPerformanceMonitoringEnabled();
        
        if (enabled) {
            plugin.getLogger().info("Performance monitoring enabled");
        } else {
            plugin.getLogger().info("Performance monitoring disabled");
        }
    }
    
    /**
     * Records an operation for performance tracking
     */
    public void recordOperation(String operationType) {
        if (!enabled) return;
        
        operationCounts.merge(operationType, 1, Integer::sum);
    }
    
    /**
     * Gets the count for a specific operation type
     */
    public int getOperationCount(String operationType) {
        return operationCounts.getOrDefault(operationType, 0);
    }
    
    /**
     * Gets all operation counts
     */
    public Map<String, Integer> getAllOperationCounts() {
        return new HashMap<>(operationCounts);
    }
    
    /**
     * Gets uptime in milliseconds
     */
    public long getUptime() {
        return System.currentTimeMillis() - startTime;
    }
    
    /**
     * Generates a simple performance report
     */
    public String generateReport() {
        if (!enabled) {
            return "Performance monitoring is disabled";
        }
        
        StringBuilder report = new StringBuilder();
        report.append("=== Performance Report ===\n");
        report.append("Uptime: ").append(getUptime() / 1000).append(" seconds\n");
        report.append("Operations:\n");
        
        for (Map.Entry<String, Integer> entry : operationCounts.entrySet()) {
            report.append("  ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        
        return report.toString();
    }
    
    /**
     * Shuts down performance monitoring
     */
    public void shutdown() {
        if (enabled) {
            plugin.getLogger().info("Performance monitoring shutdown");
            plugin.getLogger().info("Final stats: " + operationCounts.size() + " operation types tracked");
        }
    }
    
    /**
     * Checks if performance monitoring is enabled
     */
    public boolean isEnabled() {
        return enabled;
    }
}