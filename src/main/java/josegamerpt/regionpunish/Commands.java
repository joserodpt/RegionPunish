package josegamerpt.regionpunish;

import java.util.Arrays;

import josegamerpt.regionpunish.classes.RPlayer;
import josegamerpt.regionpunish.classes.SelectionCube;
import josegamerpt.regionpunish.managers.PlayerManager;
import josegamerpt.regionpunish.utils.PlayerInput;
import josegamerpt.regionpunish.utils.Text;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commands implements CommandExecutor {

    String nop = "&cSorry but you don't have permission to use this command.";

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if ((sender instanceof Player)) {
            Player p = (Player) sender;
            RPlayer rp = PlayerManager.searchPlayer(p);
            if ((cmd.getName().equalsIgnoreCase("regionpunish")) && (p.hasPermission("RegionPunish.Command"))) {
                if (args.length == 0) {
                    printHelp(p);
                } else if (args.length == 1) {
                    if (args[0].equals("filter")) {
                        if (rp.pos1 != null && rp.pos2 != null) {
                            rp.sc = new SelectionCube(rp.pos1.getLocation(), rp.pos2.getLocation());
                            rp.clearSelection();
                            Text.send(p, "Found &9" + rp.sc.getPlayersIn().size() + " &fplayers in this selection. &6(" + rp.sc.getTotalBlocks() + " blocks)");
                            Text.sendList(p, rp.sc.getPlayersInName());
                            if (rp.sc.getPlayersIn().size() > 0) {
                                Text.sendList(p, Arrays.asList("&fYou can &ekick &fplayers in this selection with &b/regionpunish kick", "&fYou can &4ban &fplayers in this selection with &b/regionpunish ban"));
                            } else {
                                rp.sc = null;
                            }
                        } else {
                            Text.send(p, "Your selection is not fully defined.");
                        }
                    } else if (args[0].equals("kick")) {
                        if (rp.sc == null) {
                            Text.send(p, "Your selection is not fully defined.");
                            return false;
                        }
                        if (rp.sc.getPlayersIn().size() < 1) {
                            Text.send(p, "&fThere are no selected players.");
                        } else {
                            new PlayerInput(p, new PlayerInput.InputRunnable() {
                                @Override
                                public void run(String input) {
                                    rp.sc.getPlayersIn().forEach(player -> player.kickPlayer(Text.addColor("\n&cYou were kicked by " + p.getDisplayName() + "\n\n&7Reason:\n" + input)));
                                    Text.send(p, "&fKicked all the selected players.");
                                }
                            }, new PlayerInput.InputRunnable() {
                                @Override
                                public void run(String input) {
                                    //
                                }
                            }, "&9&lType the kick", "&freason.");
                        }
                    } else if (args[0].equals("ban")) {
                        if (rp.sc == null) {
                            Text.send(p, "Your selection is not fully defined.");
                            return false;
                        }
                        if (rp.sc.getPlayersIn().size() < 1) {
                            Text.send(p, "&fThere are no selected players.");
                        } else {
                            new PlayerInput(p, new PlayerInput.InputRunnable() {
                                @Override
                                public void run(String input) {

                                    for (Player player : rp.sc.getPlayersIn()) {
                                        String ban = Text.addColor("\n&cYou were &4banned &cby " + p.getDisplayName() + "\n\n&7Reason:\n" + input);
                                        Bukkit.getBanList(BanList.Type.NAME).addBan(player.getName(), ban, null, p.getName());
                                        player.kickPlayer(ban);
                                    }
                                    Text.send(p, "&fKicked all the selected players.");
                                }
                            }, new PlayerInput.InputRunnable() {
                                @Override
                                public void run(String input) {
                                    //
                                }
                            }, "&9&lType the ban", "&freason.");
                        }
                    } else if (args[0].equals("tool")) {
                        Text.send(p,
                                "&fYou can use the &aSelection Tool &fto define a region. When done, type /regionpunish filter.");
                        p.getInventory().addItem(RegionPunish.SelectionTool);
                    } else {
                        Text.send(p, "&fNo command has been found with that syntax.");
                    }
                } else {
                    printHelp(p);
                }
            } else {
                Text.send(p, nop);
            }
        } else {
            System.out.print("Only players can execute this command.");
        }
        return false;
    }

    private void printHelp(Player p) {
        Text.sendList(p,
                Arrays.asList("", "        &9Region&bPunish", "&7Release &a" + RegionPunish.pl.getDescription().getVersion(), "",
                        "/regionpunish tool", "/regionpunish filter", "/regionpunish kick", "/regionpunish ban", ""));
    }
}