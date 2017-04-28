package group2.schoolproject.a02soccer;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * Created by Raphael on 28.04.2017.
 */

public class dynamicMenu {


    public static boolean onCreateOptionsMenu(Menu menu, Activity act) {
        MenuInflater inflater = act.getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public static Class onOptionsItemSelected(MenuItem item) {
        Class test = null;
        switch (item.getItemId()) {
            case R.id.mniAddUser:
                test = AddPlayerActivity.class;
                break;
            case R.id.mniEditUser:
                test = EditPlayerActivity.class;
                break;
            default:
                //return super.onOptionsItemSelected(item);
        }
        return test;
    }

}