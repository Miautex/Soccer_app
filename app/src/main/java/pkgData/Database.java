package pkgData;

import android.app.Application;

import java.util.ArrayList;

/**
 * Created by Elias on 22.03.2017.
 */

public class Database extends Application {
    private static Database instance = null;
    private ArrayList<Player> listPlayers = null;

    private Database() throws Exception {
        listPlayers = new ArrayList<>();
        generateTestData();
    }

    public static Database getInstance() throws Exception {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public ArrayList<Player> getPlayers() {
        return listPlayers;
    }

    public void insert(Player p) throws Exception {
        if (listPlayers.contains(p)) {
            throw new Exception("Player with id=" + p.getId() + " already exists");
        }
        listPlayers.add(p);
    }

    public void update(Player p) throws Exception {
        if (!listPlayers.contains(p)) {
            throw new Exception("Player with id=" + p.getId() + " doesn't exist, so it cannot be updated");
        }
        listPlayers.remove(p);
        listPlayers.add(p);
    }

    public void remove(Player p) throws Exception {
        if (!listPlayers.contains(p)) {
            throw new Exception("Player with id=" + p.getId() + " doesn't exist, so it cannot be deleted");
        }
        listPlayers.remove(p);
    }

    public void commit() {
        //TODO
    }

    public void rollback() {
        //TODO
    }

    private void generateTestData() throws Exception {
        generateTestPlayers();
    }

    private void generateTestPlayers() throws Exception {
        listPlayers.add(new Player(1, "martin", "Martin"));
        listPlayers.add(new Player(2, "elias", "Elias"));
        listPlayers.add(new Player(3, "marco", "Marco"));
        listPlayers.add(new Player(4, "raphael", "Raphael"));
        listPlayers.add(new Player(5, "pascal", "Pascal"));
        listPlayers.add(new Player(6, "jakob", "Jakob"));
        listPlayers.add(new Player(7, "stefan", "Stefan"));
        listPlayers.add(new Player(8, "lukas", "Lukas"));
    }

    /**
     * Checks whether the passed username and password are valid or not
     *
     * @param  username the username
     * @param  pw_Unencrypted the unencrypted password
     * @return the player if the login data is correct, or null if it is incorrect
     */
    public Player login(String username, String pw_Unencrypted) {
        //Temporary, because no webservice yet
        return checkForUsername(getPlayers(), username);
    }

    private Player checkForUsername(ArrayList<Player> players, String username) {
        Player retVal = null;

        for (int i=0; i<players.size() && retVal==null; i++) {
            if (players.get(i).getUsername().equals(username)) {
                retVal = players.get(i);
            }
        }

        return retVal;
    }
}
