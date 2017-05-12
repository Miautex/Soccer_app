package pkgMenu;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Locale;

import group2.schoolproject.a02soccer.AddGameSelectPlayersActivity;
import group2.schoolproject.a02soccer.AddPlayerActivity;
import group2.schoolproject.a02soccer.EditPlayerOwnActivity;
import group2.schoolproject.a02soccer.R;
import pkgDatabase.Database;

/**
 * Created by Raphael on 28.04.2017.
 * The best productowner
 */

public class DynamicMenuActivity extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
}

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        //Switch funktioniert zwar, es könnten aber fehler auftreten
        /*if(item.getItemId() == DynamicMenuItem.MAIN.ordinal()){
            openActivity(MainActivity.class);
        }
        else if (item.getItemId() == DynamicMenuItem.ADD_PLAYER.ordinal()){
            openActivity(AddPlayerActivity.class);
        }
        else if(item.getItemId() == DynamicMenuItem.EDIT_PLAYER.ordinal()){
            openActivity(EditPlayerOwnActivity.class);
        }
        else if(item.getItemId() == DynamicMenuItem.LOGIN.ordinal()){
            openActivity(LoginActivity.class);
        }
        else if(item.getItemId() == DynamicMenuItem.ADD_GAME.ordinal()){
            openActivity(AddGameSelectPlayersActivity.class);
        }
        else if(item.getItemId() == DynamicMenuItem.EDITTEAM.ordinal()){
            openActivity(TeamDivisionActivity.class);
        }
        else*/ if(item.getItemId() == DynamicMenuItem.GERMAN.ordinal()){
            changeLanguage("de");
        }
        else if(item.getItemId() == DynamicMenuItem.ENGLISH.ordinal()){
            changeLanguage("en");
        }
        // else if(item.getItemId() == DynamicMenuItem."neue_activty_EnumWert".ordinal())
        // openActivity("neue_Activity".class);
        return super.onOptionsItemSelected(item);
    }

    public void openActivity(Class toOpen){
        Intent myIntent = new Intent(this, toOpen);
        this.startActivity(myIntent);
    }

    //entfernt "überflüssige Menuitems" und entscheidet welche Sprache zum Wechsel angezeigt werden muss
    public boolean onPrepareOptionsMenu(Menu menu,Class cl) {
        //Activity die diese Methode ausführt, wird aus dem Menu entfernt
        //menu.removeItem(classtoEnum(cl).ordinal());
        //Sprache hinzufügen
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
        //menu.add(0,DynamicMenuItem.MAIN.ordinal(), Menu.NONE,R.string.mniMain);
        //menu.add(0, DynamicMenuItem.ADD_PLAYER.ordinal(), Menu.NONE, R.string.mniAdd);
        //menu.add(0,DynamicMenuItem.EDIT_PLAYER.ordinal(),Menu.NONE, R.string.mniEdit);
        //menu.add(0,DynamicMenuItem.ADD_GAME.ordinal(),Menu.NONE, R.string.mniAddGame);
        //menu.add(0,DynamicMenuItem.LOGIN.ordinal(),Menu.NONE, R.string.mniLogin);
        //menu.add(0,DynamicMenuItem.EDITTEAM.ordinal(),Menu.NONE,R.string.mniTeam);
        //menu.add(0,DynamicMenuItem."neue_activty_EnumWert".ordinal(),Menu.NONE, R.string."strings.xml value für die Activity bzw für Menuitem/eintrag")
        onPrepareOptionsMenu(menu, this.getClass());
        return true;
    }

    private static DynamicMenuItem classtoEnum(Class cl){
        DynamicMenuItem dmi = DynamicMenuItem.MAIN;
        if (cl == AddPlayerActivity.class) {
            dmi= DynamicMenuItem.ADD_PLAYER;
        }
        else if (cl == EditPlayerOwnActivity.class) {
            dmi = DynamicMenuItem.EDIT_PLAYER;
        }
        else if (cl == AddGameSelectPlayersActivity.class) {
            dmi = DynamicMenuItem.ADD_GAME;
        }
        //neue Einträge einfach mit else is (cl == "neue_Activity".class)
        //dmi = "Enumwert für die neue_Activity" (muss selbst angelegt werden)
        return dmi;
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
        //unverbuggte Version
        //Intent intent = new Intent(getApplicationContext(), MainActivity.getClass());
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //startActivity(intent);
    }

}