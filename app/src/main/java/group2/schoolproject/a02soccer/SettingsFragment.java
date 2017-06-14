package group2.schoolproject.a02soccer;

import android.os.Bundle;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import pkgData.Player;
import pkgDatabase.Database;
import pkgPreference.LayoutPickerPreference;

/**
 * @author Marco Wilscher
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        Player p = Database.getInstance().getCurrentlyLoggedInPlayer();
        if (p != null && !p.isAdmin()) {
            PreferenceCategory cat = (PreferenceCategory) findPreference("preference_general");
            LayoutPickerPreference lpp = (LayoutPickerPreference)findPreference("preference_assignment_layout");
            cat.removePreference(lpp);
        }
    }
}
