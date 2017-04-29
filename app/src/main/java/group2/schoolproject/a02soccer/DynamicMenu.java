package group2.schoolproject.a02soccer;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * Created by Raphael on 28.04.2017.
 * The best productowner
 */

public class DynamicMenu {


    public static boolean onCreateOptionsMenu(Menu menu, Activity act) {
        MenuInflater inflater = act.getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public static Class onOptionsItemSelected(MenuItem item) {
        Class retVal = null;
        //Switch funktioniert zwar, es könnten aber fehler auftreten
        /*switch (item.getItemId()) {
            case (0):
                retVal = MainActivity.class;
                break;
            case (1):
                retVal = AddPlayerActivity.class;
                break;
            case (2):
                retVal = EditPlayerActivity.class;
                break;
            default:
                //return super.onOptionsItemSelected(item);
        }*/
        if(item.getItemId() == DynamicMenuItem.MAIN.ordinal()){
            retVal= MainActivity.class;
        }
        else if (item.getItemId() == DynamicMenuItem.ADD_PLAYER.ordinal()){
            retVal = AddPlayerActivity.class;
        }
        else if(item.getItemId() == DynamicMenuItem.EDIT_PLAYER.ordinal()){
            retVal = EditPlayerActivity.class;
        }
        else if(item.getItemId() == DynamicMenuItem.LOGIN.ordinal()){
            retVal = LoginActivity.class;
        }
        // else if(item.getItemId() == DynamicMenuItem."neue_activty_EnumWert".ordinal())
        // retVal = "neue_Activity".class"
        return retVal;
    }

    public static boolean onPrepareOptionsMenu(Menu menu,Class cl) {
        menu.clear();
        menu.add(0,DynamicMenuItem.MAIN.ordinal(), Menu.NONE,R.string.mniMain);
        menu.add(0, DynamicMenuItem.ADD_PLAYER.ordinal(), Menu.NONE, R.string.mniAdd);
        menu.add(0,DynamicMenuItem.EDIT_PLAYER.ordinal(),Menu.NONE, R.string.mniEdit);
        menu.add(0,DynamicMenuItem.LOGIN.ordinal(),Menu.NONE, R.string.mniLogin);
        //menu.add(0,DynamicMenuItem."neue_activty_EnumWert".ordinal(),Menu.NONE, R.string."strings.xml value für die Activity bzw für Menuitem/eintrag")
        //die aktuelle Activity wird aus dem Menü entfernt
        menu.removeItem(classtoEnum(cl).ordinal());
        return true;
    }

    public static boolean onPrepareOptionsMenu(Menu menu) {
        //wenn kein Menüpunkt entfernt werden muss
        menu.clear();
        menu.add(0,DynamicMenuItem.MAIN.ordinal(), Menu.NONE,R.string.mniMain);
        menu.add(0, DynamicMenuItem.ADD_PLAYER.ordinal(), Menu.NONE, R.string.mniAdd);
        menu.add(0,DynamicMenuItem.EDIT_PLAYER.ordinal(),Menu.NONE, R.string.mniEdit);
        menu.add(0,DynamicMenuItem.LOGIN.ordinal(),Menu.NONE, R.string.mniLogin);
        //menu.add(0,DynamicMenuItem."neue_activty_EnumWert".ordinal(),Menu.NONE, R.string."strings.xml value für die Activity bzw für Menuitem/eintrag")
        return true;
    }

    private static DynamicMenuItem classtoEnum(Class cl){
        DynamicMenuItem dmi = DynamicMenuItem.MAIN;
        if (cl == AddPlayerActivity.class) {
            dmi= DynamicMenuItem.ADD_PLAYER;
        }
        else if (cl == EditPlayerActivity.class) {
            dmi = DynamicMenuItem.EDIT_PLAYER;
        }
        //neue Einträge einfach mit else is (cl == "neue_Activity".class)
        //dmi = "Enumwert für die neue_Activity" (muss selbst angelegt werden)
        return dmi;
    }

}