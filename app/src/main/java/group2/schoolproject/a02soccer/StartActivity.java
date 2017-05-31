package group2.schoolproject.a02soccer;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import java.net.SocketTimeoutException;

import pkgData.LocalUserData;
import pkgDatabase.Database;
import pkgWSA.Accessor;
import pkgWSA.AccessorResponse;
import pkgWSA.HttpMethod;
import pkgWSA.WebRequestTaskListener;

public class StartActivity extends BaseActivity implements WebRequestTaskListener {
    Database db;
    LocalUserData localUserData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_start);
            setTitle(R.string.app_name);

            db = Database.getInstance();
            localUserData = db.loadLocalUserData(this);
            db.setContext(this);
            Accessor.init(getApplicationContext());
            db.initPreferences(getApplicationContext());

            /*if (localUserData != null) {
                System.out.println("------------ username=" + localUserData.getPlayer());
                System.out.println("------------ pw=" + localUserData.getPassword());
                System.out.println("------------ isloggedin=" + localUserData.isLoggedIn());
            }
            else {
                System.out.println("------------ localUserData==null");
            }*/

            if (localUserData == null || localUserData.getPlayer() == null || localUserData.getPassword() == null) {
                openLogin();
            }
            else {
                if (isDeviceOnline()) {
                    startIsServerAvailableCheck();
                }
                else {
                    //if offline and logged in
                    if (localUserData.isLoggedIn()) {
                        tryLocalLogin();
                    }
                    else {
                        openLoginWithParams(localUserData.getPlayer().getUsername(), localUserData.getPassword(),
                                false, false);
                    }
                }
            }

        }
        catch (Exception ex) {
            ex.printStackTrace();
            //showMessage(getString(R.string.Error) + ": " + ex.getMessage());
        }
    }

    private void tryLocalLogin() {
        try {
            if (db.loginLocal(localUserData.getPlayer().getUsername(), localUserData.getPassword(), this)) {
                openMainActivity();
            } else {
                throw new Exception("cannot login locally");
            }
        }
        catch (Exception ex) {
            //If login locally fails, open login activity
            openLogin();
        }
    }

    private void openMainActivity(){
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void startIsServerAvailableCheck() throws Exception {
        try {
            Accessor.init(this);
            //Check to see if server responds or if timeout happens
            Accessor.runRequestAsync(HttpMethod.GET, "", null, null, this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public void done(AccessorResponse response) {
        if (response.getException() != null && response.getException().getClass().equals(SocketTimeoutException.class)) {
            tryLocalLogin();
        }
        else {
            openLoginWithParams(localUserData.getPlayer().getUsername(), localUserData.getPassword(),
                    localUserData.isLoggedIn(), true);
        }
    }

    private boolean isDeviceOnline() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void openLogin() {
        finish();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void openLoginWithParams(String username, String password, boolean doAutoLogin, boolean isOnline) {
        finish();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("username", username);
        intent.putExtra("password", password);
        intent.putExtra("doAutoLogin", doAutoLogin);
        intent.putExtra("isOnline", isOnline);
        startActivity(intent);

    }
}