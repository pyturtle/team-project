package use_case.remember_me;

import data_access.PreferenceRepository;
import entity.user.SavedUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.remember_me.RememberMeInteractor;

import static org.junit.jupiter.api.Assertions.*;

public class RememberMeInteractorTest {

    private PreferenceRepository prefs;
    private RememberMeInteractor rememberMe;

    @BeforeEach
    void setUp() {
        prefs = new PreferenceRepository();
        prefs.clear();
        rememberMe = new RememberMeInteractor(prefs);
    }

    @Test
    void testSaveCredentialsWhenRememberMeIsTrue() {
        rememberMe.saveCredentials("alice", "password123", true);

        assertTrue(prefs.loadRememberMe());
        assertEquals("alice", prefs.loadUsername());
        assertEquals("password123", prefs.loadPassword());
    }

    @Test
    void testSaveCredentialsWhenRememberMeIsFalse() {
        rememberMe.saveCredentials("alice", "password123", false);

        assertFalse(prefs.loadRememberMe());
        assertTrue(prefs.loadUsername() == null || prefs.loadUsername().isEmpty());
        assertTrue(prefs.loadPassword() == null || prefs.loadPassword().isEmpty());
    }

    @Test
    void testLoadCredentialsReturnsUser() {
        rememberMe.saveCredentials("bob", "mypassword", true);

        SavedUser loaded = rememberMe.loadCredentials();

        assertNotNull(loaded);
        assertEquals("bob", loaded.getUsername());
        assertEquals("mypassword", loaded.getPassword());
    }

    @Test
    void testLoadCredentialsReturnsNullIfRememberMeFlagFalse() {
        rememberMe.saveCredentials("charlie", "1234", false);

        SavedUser loaded = rememberMe.loadCredentials();

        assertNull(loaded);
    }

    @Test
    void testHasRememberedCredentialsTrue() {
        rememberMe.saveCredentials("david", "pass!", true);

        assertTrue(rememberMe.hasRememberedCredentials());
    }
}
