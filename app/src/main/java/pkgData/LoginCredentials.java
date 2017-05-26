package pkgData;

import java.io.Serializable;

public class LoginCredentials implements Serializable {
    private String username,
            password_enc;

    public LoginCredentials(String username, String password_enc) {
        setUsername(username);
        setPassword_enc(password_enc);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword_enc() {
        return password_enc;
    }

    public void setPassword_enc(String password_enc) {
        this.password_enc = password_enc;
    }
}

