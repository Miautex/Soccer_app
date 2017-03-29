package pkgData;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

public final class Player implements Comparable, Serializable, Cloneable {

    private int id;
    private boolean isAdmin;
    private String name, username;
    private HashSet<PlayerPosition> positions;

    private Player () throws Exception {
        super();
        id = 0;
        isAdmin = false;
        name = null;
        username = null;
        positions = new HashSet<>();
    }

    public Player(int id, String username, String name) throws Exception {
        this();
        setId(id);
        setUsername(username);
        setName(name);
    }

    private void setId (int id) {
        this.id = id;
    }

    public int getId () {
        return id;
    }

    public boolean isAdmin () {
        return isAdmin;
    }

    private void setAdmin (boolean admin) {
        isAdmin = admin;
    }

    public String getName () {
        return name;
    }

    public void setName (String name) throws Exception {
        if (name == null) {
            throw new Exception("name must not be null");
        }
        if (name.isEmpty()) {
            throw new Exception("name must not be empty");
        }
        this.name = name;
    }

    public String getUsername () {
        return username;
    }

    public void setUsername (String username) throws Exception {
        if (username == null) {
            throw new Exception("username must not be null");
        }
        if (username.isEmpty()) {
            throw new Exception("username must not be empty");
        }
        this.username = username.toLowerCase();
    }

    public ArrayList<PlayerPosition> getPositions() {
        return new ArrayList<>(positions);
    }

    public void addPosition (PlayerPosition position) throws Exception {
        positions.add(position);
    }

    public void removePosition (PlayerPosition position) throws Exception {
        positions.remove(position);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public int hashCode () {
        return id;
    }

    @Override
    public boolean equals (Object obj) {
        boolean eq = false;
        Player tmpP;
        if (obj != null) {
            if (obj instanceof Player) {
                tmpP = (Player)obj;
                eq = this.getId() == tmpP.getId();
            }
        }
        return eq;
    }

    @Override
    public String toString () {
        return "Player (#" + getId() + " - " + getUsername() + ") " + getUsername();
    }

    @Override
    protected void finalize () throws Throwable {
        super.finalize();
    }

    @Override
    public int compareTo (@NonNull Object o) {
        int cp;
        Player tmpP = (Player) o;

        cp = getName().compareTo(tmpP.getName());

        return cp;
    }
}
