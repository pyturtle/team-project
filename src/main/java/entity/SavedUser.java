package entity;

public class SavedUser {
    private String username;
    private String password;

    public SavedUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
}
