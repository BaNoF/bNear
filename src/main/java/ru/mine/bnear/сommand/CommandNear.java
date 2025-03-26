// CommandNear.java
package ru.mine.bnear.сommand;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import ru.mine.bnear.BNear;
import ru.mine.bnear.utils.ConfigUtil;
import ru.mine.bnear.utils.Direction;
import me.clip.placeholderapi.PlaceholderAPI;

public class CommandNear implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ConfigUtil.getString("settings.console"));
            return true;
        }

        Player player = (Player) sender;
        int radius = ConfigUtil.getInt("settings.max-radius");
        boolean enableInvSee = ConfigUtil.getBoolean("settings.enable-invsee-button");

        if(!player.hasPermission("near.usage")) {
            player.sendMessage(ConfigUtil.getString("settings.error-permission"));
            try {
                player.playSound(player.getLocation(),
                        Sound.valueOf(ConfigUtil.getString("settings.error-sound")),
                        SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
            catch (IllegalArgumentException e){
                BNear.instance.getLogger().warning(ConfigUtil.getString("settings.no-sound"));
            }
            return true;
        }

        TextComponent.Builder messageBuilder = Component.text();
        messageBuilder.append(
                LegacyComponentSerializer.legacyAmpersand().deserialize(
                                ConfigUtil.getString("settings.radar")
                                        .replace("{search_radius}", String.valueOf(radius))
                        )
                        .append(Component.newline()));

        boolean found = false;

        for (Player nearPlayer : player.getWorld().getPlayers()) {
            if (shouldSkipPlayer(player, nearPlayer, radius)) continue;

            String prefix = getFormattedPrefix(nearPlayer);
            Component playerLine = buildPlayerLine(player, nearPlayer, prefix, enableInvSee);

            messageBuilder.append(playerLine).append(Component.newline());
            found = true;
        }

        sendResultMessage(player, found, messageBuilder);
        return true;
    }

    private boolean shouldSkipPlayer(Player player, Player nearPlayer, int radius) {
        return nearPlayer.equals(player) ||
                player.getWorld() != nearPlayer.getWorld() ||
                player.getLocation().distance(nearPlayer.getLocation()) > radius ||
                !player.canSee(nearPlayer);
    }

    private String getFormattedPrefix(Player player) {
        String format = ConfigUtil.getString("settings.name-format", "%player_name%");
        return PlaceholderAPI.setPlaceholders(player, format);
    }

    private Component buildPlayerLine(Player viewer, Player target, String prefix, boolean enableInvSee) {
        Component prefixComponent = LegacyComponentSerializer.legacyAmpersand().deserialize(prefix);

        Component directionComponent = getDirectionComponent(viewer, target);
        Component distanceComponent = getDistanceComponent(viewer, target);

        TextComponent.Builder lineBuilder = Component.text()
                .append(prefixComponent)
                .append(Component.space())
                .append(directionComponent)
                .append(Component.space())
                .append(distanceComponent);

        if (enableInvSee && viewer.hasPermission("near.invsee")) {
            lineBuilder.append(Component.space())
                    .append(buildInvSeeButton(target));
        }

        return lineBuilder.build();
    }

    private Component getDirectionComponent(Player viewer, Player target) {
        Location origin = viewer.getLocation();
        Vector targetVec = target.getLocation().toVector();
        origin.setDirection(targetVec.subtract(origin.toVector()));
        int yaw = (int) ((viewer.getLocation().getYaw() - origin.getYaw()) / 45);
        Direction direction = new Direction();
        return LegacyComponentSerializer.legacyAmpersand().deserialize(direction.getdirection(yaw));
    }

    private Component getDistanceComponent(Player viewer, Player target) {
        double dist = viewer.getLocation().distance(target.getLocation());
        return Component.text(Math.round(dist) + " блоков")
                .color(NamedTextColor.YELLOW);
    }

    private Component buildInvSeeButton(Player target) {
        return Component.text("[Инвентарь]")
                .color(NamedTextColor.GREEN)
                .clickEvent(ClickEvent.runCommand("/invsee " + target.getName()));
    }

    private void sendResultMessage(Player player, boolean found, TextComponent.Builder builder) {
        if (found) {
            player.sendMessage(builder.build());
        } else {
            player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(
                    ConfigUtil.getString("settings.no-one-around")
            ));
        }
    }
}