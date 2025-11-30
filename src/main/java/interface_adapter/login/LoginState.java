package interface_adapter.login;

/**
 * The state for the Login View Model.
 */
public class LoginState {
    private String username = "";
    private String loginError;
    private String password = "";
    private boolean rememberMe = false;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLoginError() {
        return loginError;
    }

    public void setLoginError(String usernameError) {
        this.loginError = usernameError;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
}