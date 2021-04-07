package josegamerpt.regionpunish;

import josegamerpt.regionpunish.utils.PlayerInput;
import josegamerpt.regionpunish.utils.Text;
import me.mattstudios.mf.base.CommandManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class RegionPunish extends JavaPlugin implements Listener {

    public static Plugin pl;
    public static String name = " §9§lRegion§b§lPunish §f| ";
    PluginManager pm = Bukkit.getPluginManager();
    private CommandManager commandManager;

    public static void log(String string) {
        System.out.print(ChatColor.stripColor(name + string));
    }

    public static String getPrefix() {
        return name;
    }

    public static Plugin getPlugin() {
        return pl;
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

        commandManager = new CommandManager(this);
        commandManager.hideTabComplete(true);

        //command messages
        commandManager.getMessageHandler().register("cmd.no.exists", sender -> sender.sendMessage(getPrefix() + Text.color("&cThe command you're trying to run doesn't exist!")));
        commandManager.getMessageHandler().register("cmd.no.permission", sender -> sender.sendMessage(getPrefix() + Text.color("&fYou &cdon't &fhave permission to execute this command!")));
        commandManager.getMessageHandler().register("cmd.wrong.usage", sender -> sender.sendMessage(getPrefix() + Text.color("&cWrong usage for the command!")));

        //registo de comandos #portugal
        commandManager.register(new RegionPunishCMD());

        new Metrics(this, 10959);

        log("Plugin has been loaded.");
        log("Author: JoseGamer_PT | " + this.getDescription().getWebsite());
        log(star);
    }

}
