package ru.mine.bnear;

import org.bukkit.plugin.java.JavaPlugin;
import ru.mine.bnear.сommand.CommandNear;

public final class BNear extends JavaPlugin {
    public static BNear instance;
    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        getCommand("near").setExecutor(new CommandNear());

        getLogger().info(
                "\n██████╗░███╗░░██╗███████╗░█████╗░██████╗░\n" +
                     "██╔══██╗████╗░██║██╔════╝██╔══██╗██╔══██╗\n" +
                     "██████╦╝██╔██╗██║█████╗░░███████║██████╔╝\n" +
                     "██╔══██╗██║╚████║██╔══╝░░██╔══██║██╔══██╗\n" +
                     "██████╦╝██║░╚███║███████╗██║░░██║██║░░██║\n" +
                     "╚═════╝░╚═╝░░╚══╝╚══════╝╚═╝░░╚═╝╚═╝░░╚═╝\n" +
                        "Created by: BaNoF_");
    }
}