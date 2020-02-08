package josegamerpt.regionpunish;

import josegamerpt.regionpunish.classes.RPlayer;
import josegamerpt.regionpunish.classes.SelectionBlock;
import josegamerpt.regionpunish.managers.PlayerManager;
import josegamerpt.regionpunish.utils.Itens;
import josegamerpt.regionpunish.utils.PlayerInput;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public final class RegionPunish extends JavaPlugin implements Listener {

    public static ItemStack SelectionTool = Itens.createItemLoreEnchanted(Material.STICK, 1, "&9Selection &fTool", Arrays.asList("&fRight and Left click to define a selection."));
    public static Plugin pl;
    public static String name = " §9Region§bPunish §f| ";
    public static String nmsver;
    PluginManager pm = Bukkit.getPluginManager();

    public static void log(String string) {
        System.out.print(ChatColor.stripColor(name + string));
    }

    public static String getPrefix() {
        return name;
    }

    @Override
    public void onEnable() {
        pl = this;

        String star = "<---------------- RegionPunish PT ---------------->".replace("PT", "| " +
                this.getDescription().getVersion());
        log(star);

        log("Registering Events.");
        pm.registerEvents(this, this);
        pm.registerEvents(PlayerInput.getListener(), this);

        log("Registering Commands.");
        getCommand("regionpunish").setExecutor(new Commands());

        for (Player p : Bukkit.getOnlinePlayers()) {
            PlayerManager.loadPlayer(p);
        }

        log("Plugin has been loaded.");
        log("Author: JoseGamer_PT | " + this.getDescription().getWebsite());
        log(star);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        PlayerManager.loadPlayer(e.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        PlayerManager.players.remove(PlayerManager.searchPlayer(e.getPlayer()));
    }

    @EventHandler
    public void onUseEvent(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK
                && player.getInventory().getItemInMainHand().equals(this.SelectionTool)) {
            RPlayer p = PlayerManager.searchPlayer(player);

            if (p.pos2 != null) {
                p.pos2.revert();
            }
            p.pos2 = new SelectionBlock(e.getClickedBlock().getLocation(), e.getClickedBlock().getType());
            p.sendMessage("&9POS2 &fselected (&bX: " + e.getClickedBlock().getX() + " Y: " + e.getClickedBlock().getY()
                    + " Z: " + e.getClickedBlock().getZ() + "&f)");
            return;

        }
        if (e.getAction() == Action.LEFT_CLICK_BLOCK
                && player.getInventory().getItemInMainHand().equals(this.SelectionTool)) {

            e.setCancelled(true);

            RPlayer p = PlayerManager.searchPlayer(player);

            if (p.pos1 != null) {
                p.pos1.revert();
            }
            p.pos1 = new SelectionBlock(e.getClickedBlock().getLocation(), e.getClickedBlock().getType());
            p.sendMessage("&9POS1 &fselected (&bX: " + e.getClickedBlock().getX() + " Y: " + e.getClickedBlock().getY()
                    + " Z: " + e.getClickedBlock().getZ() + "&f)");

        }
    }

    @Override
    public void onDisable() {
        PlayerManager.players.forEach(rPlayer -> rPlayer.clearSelection());
    }
}
