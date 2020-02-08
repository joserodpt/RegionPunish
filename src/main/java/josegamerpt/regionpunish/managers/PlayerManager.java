package josegamerpt.regionpunish.managers;

import josegamerpt.regionpunish.classes.RPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class PlayerManager {

    public static ArrayList<RPlayer> players = new ArrayList<>();

    public static void loadPlayer(Player player) {
        RPlayer mp = new RPlayer(player);
        mp.save();
    }

    public static RPlayer searchPlayer(Player player) {
        for (RPlayer p : players) {
            if (p.player.equals(player)) {
                return p;
            }
        }
        return null;
    }
}