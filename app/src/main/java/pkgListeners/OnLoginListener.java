package pkgListeners;

public interface OnLoginListener {
    void loginSuccessful();
    void loginFailed(Exception ex);
}
