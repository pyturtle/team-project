package data_access.file;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONObject;

import data_access.interfaces.file.JsonDataAccess;
import data_access.interfaces.user.ChangePasswordUserDataAccessInterface;
import data_access.interfaces.user.LoginUserDataAccessInterface;
import data_access.interfaces.user.LogoutUserDataAccessInterface;
import entity.user.User;
import use_case.signup.SignupUserDataAccessInterface;

/**
 * FileUserDataAccessObject is a file-backed DAO that stores and retrieves user
 * accounts using JSON serialization. It supports signup, login, logout, and
 * password change operations.
 */
public class FileUserDataAccessObject extends JsonDataAccess<User> implements
        SignupUserDataAccessInterface,
        LoginUserDataAccessInterface,
        ChangePasswordUserDataAccessInterface,
        LogoutUserDataAccessInterface {

    private final ArrayList<User> accounts = new ArrayList<>();
    private String currentUsername;

    /**
     * Creates a FileUserDataAccessObject and loads existing users from the file
     * if provided.
     *
     * @param filePath the JSON file path to load and save user accounts,
     *                 or null to start empty
     */
    public FileUserDataAccessObject(String filePath) {
        super(filePath);
        if (filePath != null) {
            try {
                super.loadFromJson(accounts);
            }
            catch (IOException ex) {
                System.err.println(
                        "Could not load users from file: " + ex.getMessage());
            }
        }
    }

    /**
     * Saves a new user account to storage.
     *
     * @param user the user to save
     */
    @Override
    public void save(User user) {
        accounts.add(user);
        saveToJson(accounts);
    }

    /**
     * Retrieves a user by username.
     *
     * @param username the username of the requested user
     * @return the matching user or null if no such user exists
     */
    @Override
    public User get(String username) {
        return accounts.stream()
                .filter(user -> user.getName().equals(username))
                .findFirst()
                .orElse(null);
    }

    /**
     * Returns the username of the currently logged-in user.
     *
     * @return the current username, or null if no user is logged in
     */
    @Override
    public String getCurrentUsername() {
        return currentUsername;
    }

    /**
     * Sets the username of the currently logged-in user.
     *
     * @param name the username to set as current
     */
    @Override
    public void setCurrentUsername(String name) {
        currentUsername = name;
    }

    /**
     * Checks whether a user account with the given username exists.
     *
     * @param identifier the username to check
     * @return true if a matching account exists, false otherwise
     */
    @Override
    public boolean existsByName(String identifier) {
        return accounts.stream().anyMatch(user -> user.getName().equals(identifier));
    }

    /**
     * Changes a user’s password by overwriting the stored user entry.
     *
     * @param user the updated user instance
     */
    @Override
    public void changePassword(User user) {
        accounts.add(user);
        saveToJson(accounts);
    }

    /**
     * Converts a JSON object into a User entity.
     *
     * @param jsonObject the JSON user representation
     * @return a User parsed from the JSON data
     */
    @Override
    public User parseJsonObject(JSONObject jsonObject) {
        final String username = jsonObject.getString("username");
        final String password = jsonObject.getString("password");
        return new User(username, password);
    }

    /**
     * Converts a User entity to its JSON representation.
     *
     * @param object the user to convert
     * @return a JSONObject containing user properties
     */
    @Override
    public JSONObject convertObjectToJson(User object) {
        final JSONObject userObj = new JSONObject();
        userObj.put("username", object.getName());
        userObj.put("password", object.getPassword());
        return userObj;
    }
}
