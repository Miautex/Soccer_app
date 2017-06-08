package pkgData;

import java.io.Serializable;
import java.util.TreeSet;

import pkgException.NameTooLongException;
import pkgException.NameTooShortException;
import pkgException.UsernameTooLongException;
import pkgException.UsernameTooShortException;
import pkgMisc.UsernameValidator;

public final class Player implements Comparable, Serializable, Cloneable {
    private final static int MAX_LENGTH_NAME = 25,
                             MAX_LENGTH_USERNAME = 20,
                             MIN_LENGTH_NAME = 3,
                             MIN_LENGTH_USERNAME = 3;

    private Integer id = null;
    private Boolean isAdmin = false;
    private String name = null,
                   username = null;
    private TreeSet<PlayerPosition> positions = null;
    private PlayerStatistics statistics;
    private boolean isLocallySavedOnly;

    public Player() {
        super();
        id = null;
        isAdmin = false;
        name = null;
        username = null;
        positions = new TreeSet<>();
        statistics = new PlayerStatistics();
        isLocallySavedOnly = false;
    }

    public Player(int id){
        setId(id);
    }

    public Player(int id, String username, String name, Boolean isAdmin) throws Exception {
        this();
        setId(id);
        setUsername(username);
        setName(name);
        setAdmin(isAdmin);
    }

    public Player(String username, String name, Boolean isAdmin) throws Exception {
        this();
        setUsername(username);
        setName(name);
        setAdmin(isAdmin);
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws Exception {
        if (name == null) {
            throw new Exception("name must not be null");
        }
        else if (name.length() > MAX_LENGTH_NAME) {
            throw new NameTooLongException(MAX_LENGTH_NAME);
        }
        else if (name.length() < MIN_LENGTH_NAME) {
            throw new NameTooShortException(MIN_LENGTH_NAME);
        }
        this.name = name.trim();
    }

    public String getUsername() {
        String retVal = null;

        //if username is deactivated (contains '~'), return deactivated
        if (username.contains("~")) {
            retVal = "deleted";
        }
        else {
            retVal = username;
        }

        return retVal;
    }

    public void setUsername(String username) throws Exception {
        if (username == null) {
            throw new Exception("username must not be null");
        }
        else if (!UsernameValidator.validate(username)) {
            throw new IllegalArgumentException("username must only contain letters and numbers");
        }
        else if (username.length() > MAX_LENGTH_USERNAME) {
            throw new UsernameTooLongException(MAX_LENGTH_USERNAME);
        }
        else if (username.length() < MIN_LENGTH_USERNAME) {
            throw new UsernameTooShortException(MIN_LENGTH_USERNAME);
        }

        this.username = (username.toLowerCase()).trim();
    }

    public TreeSet<PlayerPosition> getPositions() {
        return positions;
    }

    public void addPosition(PlayerPosition position) throws Exception {
        positions.add(position);
    }

    public void removePosition(PlayerPosition position) throws Exception {
        positions.remove(position);
    }

    public PlayerStatistics getStatistics() {
        return statistics;
    }

    public void setStatistics(PlayerStatistics statistics) {
        this.statistics = statistics;
    }

    public boolean isLocallySavedOnly() {
        return isLocallySavedOnly;
    }

    public void setLocallySavedOnly(boolean locallySavedOnly) {
        isLocallySavedOnly = locallySavedOnly;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        boolean eq = false;
        Player tmpP;
        if (obj != null) {
            if (obj instanceof Player) {
                tmpP = (Player) obj;
                if (this.getId() != null) {
                    eq = this.getId().equals(tmpP.getId());
                }
            }
        }
        return eq;
    }

    @Override
    public String toString() {
        return getName() + " \n@" + getUsername();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    public int compareTo(Object o) {
        Player tmpP = (Player) o;

        return this.getId()-tmpP.getId();
    }
}