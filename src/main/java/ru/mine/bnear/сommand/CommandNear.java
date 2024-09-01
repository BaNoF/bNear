package ru.mine.bnear.сommand;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
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

public class CommandNear implements CommandExecutor {

    public static Group getPlayerGroup(Player player) {
        LuckPerms luckPerms = LuckPermsProvider.get();
        User user = luckPerms.getUserManager().getUser(player.getName());
        Group group = luckPerms.getGroupManager().getGroup(user.getPrimaryGroup());
        return group;
    }

    public static String getGroupPrefix(Group group) {
        String prefix = group.getCachedData().getMetaData().getPrefix();
        return prefix == null ? group.getName() : prefix;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ConfigUtil.getString("settings.console"));
            return true;
        }

        Player player = (Player) sender;
        int radius = ConfigUtil.getInt("settings.max-radius");

        if(!player.hasPermission("near.usage")) {
            player.sendMessage(ConfigUtil.getString("settings.error-permission"));
            try {
                player.playSound(player.getLocation(), Sound.valueOf(ConfigUtil.getString("settings.error-sound")), SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
            catch (IllegalArgumentException e){
                BNear.instance.getLogger().warning(ConfigUtil.getString("settings.no-sound"));
            }
        } else {
            TextComponent.Builder messageBuilder = Component.text();
            messageBuilder.append(
                    LegacyComponentSerializer.legacyAmpersand().deserialize(
                            ConfigUtil.getString("settings.radar")
                                    .replace("{search_radius}", ConfigUtil.getString("settings.max-radius"))
                    )
            ).append(Component.newline());

            boolean found = false;

            for (Player nearPlayer : player.getWorld().getPlayers()) {
                if (nearPlayer.equals(player)) continue;
                if (player.getWorld() != nearPlayer.getWorld()) continue;
                if (player.getLocation().distance(nearPlayer.getLocation()) > radius) continue;

                Group group = getPlayerGroup(nearPlayer);
                String prefix = getGroupPrefix(group) + " ";

                Component prefixComponent = LegacyComponentSerializer.legacyAmpersand().deserialize(prefix);
                Component nameComponent = Component.text(nearPlayer.getName());
                Component buttonComponent = Component.text("[Инвентарь]")
                        .color(NamedTextColor.GREEN)
                        .clickEvent(ClickEvent.runCommand("/invsee " + nearPlayer.getName()));
                Location origin = player.getLocation();
                Vector target = nearPlayer.getLocation().toVector();
                origin.setDirection(target.subtract(origin.toVector()));
                int yaw = (int) ((player.getLocation().getYaw() - origin.getYaw()) / 45);
                Direction direction = new Direction(BNear.instance);
                Component dirComp = LegacyComponentSerializer.legacyAmpersand().deserialize(direction.getdirection(yaw));
                // костыли
                double dist = player.getLocation().distance(nearPlayer.getLocation());
                long rounddist = Math.round(dist);
                String text = rounddist + " блоков";
                Component distComp = Component.text(text)
                        .color(NamedTextColor.YELLOW);


                Component playerLine = Component.empty()
                        .append(prefixComponent)
                        .append(nameComponent)
                        .append(Component.space())
                        .append(dirComp)
                        .append(Component.space())
                        .append(distComp)
                        .append(Component.space())
                        .append(buttonComponent);

                messageBuilder.append(playerLine).append(Component.newline());
                found = true;
            }

            if (found) {
                player.sendMessage(messageBuilder.build());
            } else {
                player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(
                        ConfigUtil.getString("settings.no-one-around")
                ));
            }
        }
        return true;
    }
}
