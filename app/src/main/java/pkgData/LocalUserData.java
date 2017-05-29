package pkgData;

import java.io.Serializable;

public class LocalUserData implements Serializable {
    private Player player;
    private String password;
    private boolean isLoggedIn;

    public LocalUserData(Player player, String password, boolean isLoggedIn) {
        this.player = player;
        this.password = password;
        this.isLoggedIn = isLoggedIn;
    }

    public LocalUserData() {
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }
}
