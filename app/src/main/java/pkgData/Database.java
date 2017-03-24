package pkgData;

import android.app.Application;

import java.util.ArrayList;

/**
 * Created by Elias on 22.03.2017.
 */

public class Database extends Application {
    private static Database instance = null;
    private ArrayList<DatabaseObject> listDbos = null;

    private Database() throws Exception {
        listDbos = new ArrayList<>();
        generateTestData();
    }

    public static Database getInstance() throws Exception {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public ArrayList<Player> getPlayers() {
        ArrayList<Player> listPlayers = new ArrayList<>();

        for (DatabaseObject dbo: listDbos) {
            if (dbo.getClass().equals(Player.class)) {
                listPlayers.add((Player) dbo);
            }
        }

        return listPlayers;
    }

    public void insert(DatabaseObject dbo) throws Exception {
        if (listDbos.contains(dbo)) {
            throw new Exception(dbo.getClass() + " with id=" + dbo.getId() + " already exists");
        }
        listDbos.add(dbo);
    }

    public void update(DatabaseObject dbo) throws Exception {
        if (!listDbos.contains(dbo)) {
            throw new Exception(dbo.getClass() + " with id=" + dbo.getId() + " doesn't exist, so it cannot be updated");
        }
        listDbos.remove(dbo);
        listDbos.add(dbo);
    }

    public void remove(DatabaseObject dbo) throws Exception {
        if (!listDbos.contains(dbo)) {
            throw new Exception(dbo.getClass() + " with id=" + dbo.getId() + " doesn't exist, so it cannot be deleted");
        }
        listDbos.remove(dbo);
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
        listDbos.add(new Player(1, "martin", "Martin"));
        listDbos.add(new Player(2, "elias", "Elias"));
        listDbos.add(new Player(3, "marco", "Marco"));
        listDbos.add(new Player(4, "raphael", "Raphael"));
        listDbos.add(new Player(5, "pascal", "Pascal"));
        listDbos.add(new Player(6, "jakob", "Jakob"));
        listDbos.add(new Player(7, "stefan", "Stefan"));
        listDbos.add(new Player(8, "lukas", "Lukas"));
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