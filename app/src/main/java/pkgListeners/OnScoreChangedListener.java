package pkgListeners;

import android.support.v4.app.Fragment;

/**
 * Created by Elias on 03.05.2017.
 */

public interface OnScoreChangedListener {
    public void onScoreUpdated(int sumGoalsShot, Fragment fragment);
}
