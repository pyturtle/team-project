package data_access.in_memory;

import data_access.interfaces.user.ChangePasswordUserDataAccessInterface;
import data_access.interfaces.user.LoginUserDataAccessInterface;
import data_access.interfaces.user.LogoutUserDataAccessInterface;
import entity.user.User;
import use_case.signup.SignupUserDataAccessInterface;

import java.util.HashMap;
import java.util.Map;

/**
 * In-memory implementation of the DAO for storing user data. This implementation does
 * NOT persist data between runs of the program.
 */
public class InMemoryUserDataAccessObject implements SignupUserDataAccessInterface,
        LoginUserDataAccessInterface,
        ChangePasswordUserDataAccessInterface,
        LogoutUserDataAccessInterface {

    private final Map<String, User> users = new HashMap<>();

    private String currentUsername;

    @Override
    public boolean existsByName(String identifier) {
        return users.containsKey(identifier);
    }

    @Override
    public void save(User user) {
        users.put(user.getName(), user);
    }

    @Override
    public User get(String username) {
        return users.get(username);
    }

    @Override
    public String getCurrentUsername() {
        return currentUsername;
    }

    @Override
    public void setCurrentUsername(String name) {
        currentUsername = name;
    }

    @Override
    public void changePassword(User user) {

        users.put(user.getName(), user);
    }

}
