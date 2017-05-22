package group2.schoolproject.a02soccer;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

public class DebugSettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.debug_settings);
    }
}
