package data_access.file;
import data_access.interfaces.file.JsonDataAccess;
import data_access.interfaces.user.ChangePasswordUserDataAccessInterface;
import data_access.interfaces.user.LoginUserDataAccessInterface;
import data_access.interfaces.user.LogoutUserDataAccessInterface;
import entity.user.User;
import org.json.JSONObject;
import use_case.signup.SignupUserDataAccessInterface;
import java.io.*;
import java.util.ArrayList;

/**
 * DAO for user data implemented using a File to persist the data.
 */
public class FileUserDataAccessObject extends JsonDataAccess<User> implements SignupUserDataAccessInterface,
        LoginUserDataAccessInterface,
        ChangePasswordUserDataAccessInterface,
        LogoutUserDataAccessInterface {

    private final ArrayList<User> accounts = new ArrayList<>();

    private String currentUsername;

    /**
     * Construct this DAO for saving to and reading from a local file.
     *
     * @param filePath     the path of the file to save to
     * @throws RuntimeException if there is an IOException when accessing the file
     */
    public FileUserDataAccessObject(String filePath) {
        super(filePath);
        if(filePath != null) {
            try {
                super.loadFromJson(accounts);
            } catch (IOException e) {
                System.err.println("Could not load users from file: " + e.getMessage());
            }
        }
    }


    @Override
    public void save(User user) {
        accounts.add(user);
        saveToJson(accounts);
    }

    @Override
    public User get(String username) {
        return accounts.stream()
                .filter(user -> user.getName().equals(username))
                .findFirst()
                .orElse(null);
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
    public boolean existsByName(String identifier) {
        return accounts.stream().anyMatch(user -> user.getName().equals(identifier));
    }

    @Override
    public void changePassword(User user) {
        accounts.add(user);
        saveToJson(accounts);
    }

    @Override
    public User parseJsonObject(JSONObject jsonObject) {
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        return new User(username, password);
    }

    @Override
    public JSONObject convertObjectToJson(User object) {
        JSONObject userObj = new JSONObject();
        userObj.put("username", object.getName());
        userObj.put("password", object.getPassword());
        return userObj;
    }
}
