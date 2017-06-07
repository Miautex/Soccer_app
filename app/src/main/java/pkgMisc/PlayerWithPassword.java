package pkgMisc;

/**
 * Created by Elias on 02.06.2017.
 */

import android.support.annotation.NonNull;

import java.io.Serializable;

import pkgData.Player;

/**
 * This class is used only to store a player with his password in offline mode before the player is inserted by Webservice
 */
public class PlayerWithPassword implements Comparable, Serializable {
    private Player player;
    private String password;

    public PlayerWithPassword(Player player, String password) {

        this.player = player;
        this.password = password;
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

    @Override
    public int compareTo(@NonNull Object o) {
        PlayerWithPassword tmpP = (PlayerWithPassword) o;

        return this.getPlayer().getId()-tmpP.getPlayer().getId();
    }

    @Override
    public boolean equals(Object obj) {
        boolean eq = false;
        PlayerWithPassword tmpP;
        if (obj != null) {
            if (obj instanceof PlayerWithPassword) {
                tmpP = (PlayerWithPassword) obj;
                if (this.getPlayer() != null && tmpP.getPlayer() != null) {
                    eq = this.getPlayer().getId().equals(tmpP.getPlayer().getId());
                }
            }
        }
        return eq;
    }

    @Override
    public int hashCode() {
        return player.hashCode();
    }
}
