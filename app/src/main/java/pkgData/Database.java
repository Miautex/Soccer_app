package pkgData;

import android.app.Application;

import java.util.ArrayList;

/**
 * Created by Elias on 22.03.2017.
 */

public class Database extends Application {
    private static Database instance = null;
    private ArrayList<Player> listPlayers = null;
    private Player currentlyLoggedInPlayer = null;

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

    /**
     * Returns a copy of the currently logged in player
     * @return a COPY of the currently logged in player
     */
    public Player getCurrentlyLoggedInPlayer() {
        Player retVal = null;

        try {
            retVal = (Player) currentlyLoggedInPlayer.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return retVal;
    }

    /**
     * Returns a copy of all players
     * @return a COPIED collection of all players
     */
    public ArrayList<Player> getPlayers() {
        return new ArrayList<>(listPlayers);
    }

    public void insert(Player p) throws Exception {
        if (listPlayers.contains(p)) {
            throw new Exception("Player with id=" + p.getId() + " already exists");
        }

        if (checkForUsername(getPlayers(), p.getUsername()) != null) {
            throw new Exception("Username '" + p.getUsername() + "' is already assigned to a different user");
        }

        if (currentlyLoggedInPlayer.equals(p)) {
            currentlyLoggedInPlayer = p;
        }

        listPlayers.add(p);
    }

    public void update(Player p) throws Exception {
        if (!listPlayers.contains(p)) {
            throw new Exception("Player with id=" + p.getId() + " doesn't exist, so it cannot be updated");
        }
        ArrayList<Player> tmpPlayers = getPlayers();
        tmpPlayers.remove(getCurrentlyLoggedInPlayer());

        if (checkForUsername(tmpPlayers, p.getUsername()) != null) {
            throw new Exception("Username '" + p.getUsername() + "' is already assigned to a different user");
        }

        if (currentlyLoggedInPlayer.equals(p)) {
            currentlyLoggedInPlayer = p;
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
        listPlayers.add(new Player(1, "admin", "Admin"));
        listPlayers.add(new Player(2, "elias", "Elias"));
        listPlayers.add(new Player(3, "marco", "Marco"));
        listPlayers.add(new Player(4, "raphael", "Raphael"));
        listPlayers.add(new Player(5, "pascal", "Pascal"));
        listPlayers.add(new Player(6, "jakob", "Jakob"));
        listPlayers.add(new Player(7, "martin", "Martin"));
        listPlayers.add(new Player(8, "lukas", "Lukas"));
    }

    /**
     * Checks whether the passed username and password are valid or not
     * If username and password are correct, this user is set to currentlyLoggedInUser
     *
     * @param  username the username
     * @param  pw_Unencrypted the unencrypted password
     * @return true, if the username and password are correct
     */
    public boolean login(String username, String pw_Unencrypted) {
        //Temporary, because no webservice yet
        currentlyLoggedInPlayer = checkForUsername(getPlayers(), username);

        return currentlyLoggedInPlayer==null ? false : true;
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

    public boolean isUsernameAvailable(String username) {
        return (checkForUsername(getPlayers(), username) == null ? true : false);
    }
}
