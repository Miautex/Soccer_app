package pkgDatabase.pkgListener;

public interface OnLoginListener {
    void loginSuccessful(String username);
    void loginFailed(Exception ex);
}
