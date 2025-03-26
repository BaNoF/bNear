// BNear.java
package ru.mine.bnear;

import org.bukkit.plugin.java.JavaPlugin;
import ru.mine.bnear.сommand.CommandNear;

public final class BNear extends JavaPlugin {
    public static BNear instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") == null) {
            getLogger().warning("PlaceholderAPI not found! Some features may not work!");
        }

        getCommand("near").setExecutor(new CommandNear());

        getLogger().info(
                "\n██████╗░███╗░░██╗███████╗░█████╗░██████╗░\n" +
                        "██╔══██╗████╗░██║██╔════╝██╔══██╗██╔══██╗\n" +
                        "██████╦╝██╔██╗██║█████╗░░███████║██████╔╝\n" +
                        "██╔══██╗██║╚████║██╔══╝░░██╔══██║██╔══██╗\n" +
                        "██████╦╝██║░╚███║███████╗██║░░██║██║░░██║\n" +
                        "╚═════╝░╚═╝░░╚══╝╚══════╝╚═╝░░╚═╝╚═╝░░╚═╝\n" +
                        "Created by: bTeam");
    }
}