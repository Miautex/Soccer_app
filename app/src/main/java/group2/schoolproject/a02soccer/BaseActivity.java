package group2.schoolproject.a02soccer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import pkgDatabase.Database;
import pkgMenu.DynamicMenuItem;

/**
 * Created by Raphael on 28.04.2017.
 * The best productowner
 */

public class BaseActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLanguage(PreferenceManager.getDefaultSharedPreferences(this).getString("preference_general_language", "en"));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == DynamicMenuItem.SETTINGS.ordinal()) {
            openActivity(DebugSettingsActivity.class);
        }
        else {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            if (item.getItemId() == DynamicMenuItem.GERMAN.ordinal()) {
                editor.putString("preference_general_language", "de");
            } else if (item.getItemId() == DynamicMenuItem.ENGLISH.ordinal()) {
                editor.putString("preference_general_language", "en");
            } else if (item.getItemId() == DynamicMenuItem.SPANISCH.ordinal()) {
                editor.putString("preference_general_language", "es");
            }
            editor.commit();
            finish();
            startActivity(new Intent(this, this.getClass()).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
        return super.onOptionsItemSelected(item);
    }

    public void openActivity(Class toOpen) {
        Intent myIntent = new Intent(this, toOpen);
        this.startActivity(myIntent);
    }

    public boolean onPrepareOptionsMenu(Menu menu, Class cl) {
        menu.add(0, DynamicMenuItem.SETTINGS.ordinal(), Menu.NONE, "DEBUG SETTINGS");
        if (getResources().getConfiguration().locale.toString().contains("de")) {
            menu.add(0, DynamicMenuItem.ENGLISH.ordinal(), Menu.NONE, R.string.mniEnglish);
            menu.add(0, DynamicMenuItem.SPANISCH.ordinal(), Menu.NONE, "Spanisch");
        } else if ((getResources().getConfiguration().locale.toString().contains("en"))) {
            menu.add(0, DynamicMenuItem.GERMAN.ordinal(), Menu.NONE, R.string.mniGerman);
            menu.add(0, DynamicMenuItem.SPANISCH.ordinal(), Menu.NONE, "Spanisch");
        } else {
            menu.add(0, DynamicMenuItem.GERMAN.ordinal(), Menu.NONE, R.string.mniGerman);
            menu.add(0, DynamicMenuItem.ENGLISH.ordinal(), Menu.NONE, R.string.mniEnglish);
        }
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        onPrepareOptionsMenu(menu, this.getClass());
        return true;
    }

    protected void changeLanguage(String s) {
        Locale locale = new Locale(s);
        Database.getInstance().setLocale(locale);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = locale;
        res.updateConfiguration(conf, dm);
        finish();
        startActivity(new Intent(this, this.getClass()).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    protected void setLanguage(String s) {
        Locale locale = new Locale(s);
        Database.getInstance().setLocale(locale);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = locale;
        res.updateConfiguration(conf, dm);
    }

    public void showMessage(String msg) {
        if (Database.getInstance().isToast()) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        } else {
            Snackbar s = Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_SHORT);
            TextView snackbarActionTextView = (TextView) s.getView().findViewById(android.support.design.R.id.snackbar_text);
            snackbarActionTextView.setTextSize(16);
            s.show();
        }
    }
}