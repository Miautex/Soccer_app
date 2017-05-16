package group2.schoolproject.a02soccer;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Locale;

import group2.schoolproject.a02soccer.AddGameSelectPlayersActivity;
import group2.schoolproject.a02soccer.AddPlayerActivity;
import group2.schoolproject.a02soccer.EditPlayerActivity;
import group2.schoolproject.a02soccer.R;
import pkgDatabase.Database;
import pkgMenu.DynamicMenuItem;

/**
 * Created by Raphael on 28.04.2017.
 * The best productowner
 */

public class BaseActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
}

    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == DynamicMenuItem.GERMAN.ordinal()){
            changeLanguage("de");
        }
        else if(item.getItemId() == DynamicMenuItem.ENGLISH.ordinal()){
            changeLanguage("en");
        }
        return super.onOptionsItemSelected(item);
    }

    public void openActivity(Class toOpen){
        Intent myIntent = new Intent(this, toOpen);
        this.startActivity(myIntent);
    }

    public boolean onPrepareOptionsMenu(Menu menu,Class cl) {
        if(getResources().getConfiguration().locale.toString().contains("de")){
            menu.add(0,DynamicMenuItem.ENGLISH.ordinal(),Menu.NONE, R.string.mniEnglish);
        }
        else{
            menu.add(0,DynamicMenuItem.GERMAN.ordinal(),Menu.NONE, R.string.mniGerman);
        }
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        onPrepareOptionsMenu(menu, this.getClass());
        return true;
    }

    private void changeLanguage(String s){
        Locale locale = new Locale(s);
        Database.getInstance().setLocale(locale);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = locale;
        res.updateConfiguration(conf, dm);
        finish();
        startActivity(new Intent(this,this.getClass()).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    public void showMessage(String msg){
        if(Database.getInstance().isToast()){
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
        else{
            Snackbar.make(findViewById(android.R.id.content),msg,Snackbar.LENGTH_SHORT).show();
        }
    }

}