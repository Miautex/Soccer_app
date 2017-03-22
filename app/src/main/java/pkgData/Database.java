package pkgData;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by Elias on 22.03.2017.
 */

public class Database {
    private static Database instance = null;
    private TreeMap<Integer, Player> mapPlayers = null;


    private Database() throws Exception {
        mapPlayers = new TreeMap<>();

        generateTestData();
    }

    public static Database getInstance() throws Exception {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public ArrayList<Player> getPlayers() throws Exception {
        return new ArrayList<>(mapPlayers.values());
    }

    public void addPlayer(Player p) throws Exception {
        if (mapPlayers.containsKey(p.getId())) {
            throw new Exception("Player with id=" + p.getId() + " already exists");
        }

         mapPlayers.put(p.getId(), p);
    }

    public void updatePlayer(Player p) throws Exception {
        if (!mapPlayers.containsKey(p.getId())) {
            throw new Exception("Player with id=" + p.getId() + " doesn't exist, so it cannot be updated");
        }

        mapPlayers.remove(p.getId());
        mapPlayers.put(p.getId(), p);
    }

    public void removePlayer(int id) throws Exception {
         mapPlayers.remove(id);
    }

    private void generateTestData() throws Exception {
         mapPlayers.put(1, new Player(1, "martin", "Martin"));
         mapPlayers.put(2, new Player(2, "elias", "Elias"));
         mapPlayers.put(3, new Player(3, "marco", "Marco"));
         mapPlayers.put(4, new Player(4, "raphael", "Raphael"));
         mapPlayers.put(5, new Player(5, "pascal", "Pascal"));
         mapPlayers.put(6, new Player(6, "jakob", "Jakob"));
         mapPlayers.put(7, new Player(7, "stefan", "Stefan"));
         mapPlayers.put(8, new Player(8, "lukas", "Lukas"));
    }
}
