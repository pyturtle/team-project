package use_case.remember_me;

import data_access.PreferenceRepository;
import entity.SavedUser;

public class RememberMe {

    private final PreferenceRepository prefs;

    public RememberMe(PreferenceRepository prefs) {
        this.prefs = prefs;
    }

    public void saveCredentials(String username, String password, boolean rememberMe) {
        if (rememberMe) {
            prefs.save(username, password, true);
        } else {
            prefs.clear();
        }
    }

    public SavedUser loadCredentials() {
        if (!prefs.loadRememberMe()) return null;

        String username = prefs.loadUsername();
        String password = prefs.loadPassword();

        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            return null;
        }

        return new SavedUser(username, password);
    }

    public boolean hasRememberedCredentials() {
        return prefs.loadRememberMe() && !prefs.loadUsername().isEmpty();
    }
}