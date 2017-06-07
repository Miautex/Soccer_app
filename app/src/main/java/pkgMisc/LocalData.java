package pkgMisc;

import java.io.Serializable;
import java.util.TreeSet;

import pkgData.Game;
import pkgData.Player;

public class LocalData implements Serializable {
    private TreeSet<Player> allPlayers;
    private TreeSet<Game> allGames;
    private TreeSet<PlayerWithPassword> localPlayers;
    private TreeSet<Game> localGames;

    public LocalData(TreeSet<Player> allPlayers, TreeSet<Game> allGames) {
        this.allPlayers = allPlayers;
        this.allGames = allGames;
    }

    public LocalData(TreeSet<Player> allPlayers, TreeSet<Game> allGames,
                     TreeSet<PlayerWithPassword> localPlayers, TreeSet<Game> localGames) {
        this(allPlayers,allGames);
        this.localPlayers = localPlayers;
        this.localGames = localGames;
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

    public TreeSet<PlayerWithPassword> getLocalPlayers() {
        return localPlayers;
    }

    public void setLocalPlayers(TreeSet<PlayerWithPassword> localPlayers) {
        this.localPlayers = localPlayers;
    }

    public TreeSet<Game> getLocalGames() {
        return localGames;
    }

    public void setLocalGames(TreeSet<Game> localGames) {
        this.localGames = localGames;
    }
}
