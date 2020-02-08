package josegamerpt.regionpunish.classes;

import josegamerpt.regionpunish.RegionPunish;
import josegamerpt.regionpunish.managers.PlayerManager;
import josegamerpt.regionpunish.utils.Text;
import org.bukkit.entity.Player;

public class RPlayer {

    public SelectionBlock pos1;
    public SelectionBlock pos2;
    public SelectionCube sc;
    public Player player;

    public RPlayer(Player p) {
        this.player = p;
    }

    public void save() {
        PlayerManager.players.add(this);
    }

    public void clearSelection() {
        if (pos1 != null) {
            pos1.revert();
        }
        if (pos2 != null) {
            pos2.revert();
        }
        pos1 = null;
        pos2 = null;
    }

    public void sendMessage(String string) {
        player.sendMessage(RegionPunish.getPrefix() + Text.addColor(string));
    }
}