package josegamerpt.regionpunish.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import josegamerpt.regionpunish.RegionPunish;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Text {

    public static String color(final String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static void sendList(CommandSender p, List<String> list) {
        list.forEach(s -> p.sendMessage(color(s)));
    }

    public static ArrayList<String> color(List<String> list) {
        ArrayList<String> color = new ArrayList<>();
        list.forEach(o -> color.add(Text.color(o)));
        return color;
    }

    public static void send(Player p, String string) {
        p.sendMessage(RegionPunish.getPrefix() + Text.color(string));
    }

}