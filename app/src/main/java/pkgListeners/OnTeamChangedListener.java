package pkgListeners;

import android.support.v4.app.Fragment;

import pkgData.Player;
import pkgData.Team;

/**
 * Created by Raphael on 09.05.2017.
 */

public interface OnTeamChangedListener {
    public void onTeamUpdated(Player p, Team t, boolean remove);
}
