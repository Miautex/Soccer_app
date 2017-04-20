package pkgData;

import android.app.Application;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Database extends Application {
    private static Database instance = null;
    private ArrayList<Player> listPlayers = null;
    private ArrayList<Game> listGames = null;
    private Player currentlyLoggedInPlayer = null;

    private Database() throws Exception {
        listPlayers = new ArrayList<>();
        listGames = new ArrayList<>();
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

    /**
     * Returns a copy of all games
     * @return a COPIED collection of all games
     */
    public ArrayList<Game> getGames() {
        return new ArrayList<>(listGames);
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
        generateTestGames();
    }

    private void generateTestPlayers() throws Exception {
        listPlayers.add(new Player("admin", "Admin", true));
        listPlayers.add(new Player("elias", "Elias", false));
        listPlayers.add(new Player("marco", "Marco", false));
        listPlayers.add(new Player("raphael", "Raphael", false));
        listPlayers.add(new Player("pascal", "Pascal", false));
        listPlayers.add(new Player("jakob", "Jakob", false));
        listPlayers.add(new Player("martin", "Martin", false));
        listPlayers.add(new Player("lukas", "Lukas", false));
    }

    private void generateTestGames() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

        listGames.add(new Game(1, sdf.parse("13.02.2017"), 2, 1));
        listGames.add(new Game(2, sdf.parse("16.02.2017"), 1, 4));
        listGames.add(new Game(3, sdf.parse("21.02.2017"), 4, 5));
        listGames.add(new Game(4, sdf.parse("02.03.2017"), 6, 3));
        listGames.add(new Game(5, sdf.parse("11.03.2017"), 1, 1));
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
        currentlyLoggedInPlayer = checkForUsername(getPlayers(), username.trim());

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
        return (checkForUsername(getPlayers(), username.trim()) == null ? true : false);
    }
}
