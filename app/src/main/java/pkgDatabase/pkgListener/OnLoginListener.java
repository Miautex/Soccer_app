package pkgDatabase.pkgListener;

import pkgDatabase.pkgHandler.LoginHandler;

public interface OnLoginListener {
    void loginFinished(LoginHandler handler);
}
