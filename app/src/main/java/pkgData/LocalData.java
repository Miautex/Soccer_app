package pkgData;

import java.io.Serializable;
import java.util.TreeSet;

public class LocalData implements Serializable {
    private TreeSet<Player> allPlayers;
    private TreeSet<Game> allGames;
    private TreeSet<Player> localPlayers;
    private TreeSet<Game> localGames;

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

    public TreeSet<Player> getLocalPlayers() {
        return localPlayers;
    }

    public void setLocalPlayers(TreeSet<Player> localPlayers) {
        this.localPlayers = localPlayers;
    }

    public TreeSet<Game> getLocalGames() {
        return localGames;
    }

    public void setLocalGames(TreeSet<Game> localGames) {
        this.localGames = localGames;
    }
}
