package use_case.remember_me;

import entity.user.SavedUser;

public interface RememberMeDataAccessInterface {
    void saveCredentials(SavedUser credentials, boolean rememberMe);

    SavedUser loadCredentials();

    boolean hasRememberedCredentials();

    void clearCredentials();
}