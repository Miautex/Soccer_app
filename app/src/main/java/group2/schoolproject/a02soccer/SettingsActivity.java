package group2.schoolproject.a02soccer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;

public class SettingsActivity extends BaseActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_activity_settings);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged (SharedPreferences sharedPreferences, String key) {
        if (key.equals("preference_general_language")) {
            finish();
            startActivity(new Intent(this,this.getClass()).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
    }

    @Override
    public void onBackPressed () {
        NavUtils.navigateUpFromSameTask(this);
    }
}