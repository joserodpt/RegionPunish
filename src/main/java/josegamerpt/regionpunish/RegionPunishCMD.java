package josegamerpt.regionpunish;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import josegamerpt.regionpunish.utils.PlayerInput;
import josegamerpt.regionpunish.utils.SelectionCube;
import josegamerpt.regionpunish.utils.Text;
import me.mattstudios.mf.annotations.*;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

@Command("regionpunish")
@Alias({"rp", "regp"})
public class RegionPunishCMD extends CommandBase {

    String playerOnly = "[RegionPunish] Only players can run this command.";

    @Default
    public void defaultCommand(final CommandSender commandSender) {
        Text.sendList(commandSender,
                Arrays.asList("", "         &9&lRegion&b&lPunish", "&7Release &a" + RegionPunish.getPlugin().getDescription().getVersion(), ""));
    }

    @SubCommand("filter")
    @Permission("RegionPunish.Command")
    public void filtercmd(final CommandSender commandSender) {
        if (commandSender instanceof Player) {
            Player p = (Player) commandSender;
            WorldEditPlugin w = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
            try {
                com.sk89q.worldedit.regions.Region r = w.getSession(p.getPlayer()).getSelection(w.getSession(p.getPlayer()).getSelectionWorld());

                if (r != null) {
                    Location pos1 = new Location(p.getWorld(), r.getMaximumPoint().getBlockX(), r.getMaximumPoint().getBlockY(), r.getMaximumPoint().getBlockZ());
                    Location pos2 = new Location(p.getWorld(), r.getMinimumPoint().getBlockX(), r.getMinimumPoint().getBlockY(), r.getMinimumPoint().getBlockZ());

                    SelectionCube s = new SelectionCube(pos1, pos2);
                    Text.send(p, "Found &9" + s.getPlayersIn().size() + " &fplayers in this selection. &6(" + s.getTotalBlocks() + " blocks)");
                    Text.sendList(p, s.getPlayersInName());
                    if (s.getPlayersIn().size() > 0) {
                        Text.sendList(p, Arrays.asList("&fYou can &ekick &fplayers in this selection with &b/regionpunish kick", "&fYou can &4ban &fplayers in this selection with &b/regionpunish ban"));
                    }
                }
            } catch (Exception e) {
                Text.send(p, "Your selection is not fully defined.");
            }
        } else {
            commandSender.sendMessage(playerOnly);
        }
    }

    @SubCommand("kick")
    @Permission("RegionPunish.Command")
    public void kickcmd(final CommandSender commandSender) {
        if (commandSender instanceof Player) {
            Player p = (Player) commandSender;
            WorldEditPlugin w = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
            try {
                com.sk89q.worldedit.regions.Region r = w.getSession(p.getPlayer()).getSelection(w.getSession(p.getPlayer()).getSelectionWorld());

                if (r != null) {
                    Location pos1 = new Location(p.getWorld(), r.getMaximumPoint().getBlockX(), r.getMaximumPoint().getBlockY(), r.getMaximumPoint().getBlockZ());
                    Location pos2 = new Location(p.getWorld(), r.getMinimumPoint().getBlockX(), r.getMinimumPoint().getBlockY(), r.getMinimumPoint().getBlockZ());

                    SelectionCube s = new SelectionCube(pos1, pos2);

                    if (s.getPlayersIn().size() < 1) {
                        Text.send(p, "&fThere are no players to select.");
                    } else {
                        new PlayerInput(p, input -> {
                            s.getPlayersIn().forEach(player -> player.kickPlayer(Text.color("\n&cYou were kicked by " + p.getDisplayName() + "\n\n&7Reason:\n" + input)));
                            Text.send(p, "&fKicked all the selected players.");
                        }, input -> {
                            //
                        }, "&9&lType the kick", "&freason.");
                    }
                }
            } catch (Exception e) {
                Text.send(p, "Your selection is not fully defined.");
            }

        } else {
            commandSender.sendMessage(playerOnly);
        }
    }

    @SubCommand("ban")
    @Permission("RegionPunish.Command")
    public void bancmd(final CommandSender commandSender) {
        if (commandSender instanceof Player) {
            Player p = (Player) commandSender;
            WorldEditPlugin w = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
            try {
                com.sk89q.worldedit.regions.Region r = w.getSession(p.getPlayer()).getSelection(w.getSession(p.getPlayer()).getSelectionWorld());

                if (r != null) {
                    Location pos1 = new Location(p.getWorld(), r.getMaximumPoint().getBlockX(), r.getMaximumPoint().getBlockY(), r.getMaximumPoint().getBlockZ());
                    Location pos2 = new Location(p.getWorld(), r.getMinimumPoint().getBlockX(), r.getMinimumPoint().getBlockY(), r.getMinimumPoint().getBlockZ());

                    SelectionCube s = new SelectionCube(pos1, pos2);

                    if (s.getPlayersIn().size() < 1) {
                        Text.send(p, "&fThere are no selected players.");
                    } else {
                        new PlayerInput(p, input -> {
                            for (Player player : s.getPlayersIn()) {
                                String ban = Text.color("\n&cYou were &4banned &cby " + p.getDisplayName() + "\n\n&7Reason:\n" + input);
                                Bukkit.getBanList(BanList.Type.NAME).addBan(player.getName(), ban, null, p.getName());
                                player.kickPlayer(ban);
                            }
                            Text.send(p, "&fKicked all the selected players.");
                        }, input -> {
                            //
                        }, "&9&lType the ban", "&freason.");
                    }
                }
            } catch (Exception e) {
                Text.send(p, "Your selection is not fully defined.");
            }
        } else {
            commandSender.sendMessage(playerOnly);
        }
    }
}