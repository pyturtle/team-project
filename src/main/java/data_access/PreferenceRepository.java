package data_access;

import java.util.prefs.Preferences;

public class PreferenceRepository {
    private final Preferences prefs = Preferences.userRoot().node("SmartPlannerApp");

    public void save(String username, String password, boolean remember) {
        try {
            prefs.put("username", username);
            prefs.put("password", password);
            prefs.putBoolean("rememberMe", remember);
            prefs.flush(); // Ensure changes are written to disk
        } catch (Exception e) {
            System.err.println("Failed to save preferences: " + e.getMessage());
        }
    }

    public void clear() {
        try {
            prefs.remove("username");
            prefs.remove("password");
            prefs.putBoolean("rememberMe", false);
            prefs.flush();
        } catch (Exception e) {
            System.err.println("Failed to clear preferences: " + e.getMessage());
        }
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