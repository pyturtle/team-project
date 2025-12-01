package data_access;

import java.util.prefs.Preferences;

public class PreferenceRepository {
    private final Preferences prefs = Preferences.userRoot().node("SmartPlannerApp");

    public void save(String username, String password, boolean remember) {
        prefs.put("username", username);
        prefs.put("password", password);
        prefs.putBoolean("rememberMe", remember);
    }

    public void clear() {
        prefs.remove("username");
        prefs.remove("password");
        prefs.putBoolean("rememberMe", false);
    }

    public String loadUsername() {
        return prefs.get("username", "");
    }

    public String loadPassword() {
        return prefs.get("password", "");
    }

    public boolean loadRememberMe() {
        return prefs.getBoolean("rememberMe", false);
    }
}