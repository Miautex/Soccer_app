package pkgData;

import java.io.Serializable;
import java.util.TreeSet;

public class LocalData implements Serializable {
    private TreeSet<Player> allPlayers;
    private TreeSet<Game> allGames;

    public LocalData(TreeSet<Player> allPlayers, TreeSet<Game> allGames) {
        this.allPlayers = allPlayers;
        this.allGames = allGames;
    }

    public TreeSet<Player> getAllPlayers() {
        return allPlayers;
    }

    public void setAllPlayers(TreeSet<Player> allPlayers) {
        this.allPlayers = allPlayers;
    }

    public TreeSet<Game> getAllGames() {
        return allGames;
    }

    public void setAllGames(TreeSet<Game> allGames) {
        this.allGames = allGames;
    }
}
