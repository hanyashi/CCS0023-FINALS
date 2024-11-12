import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private Map<String, User> users = new HashMap<>(); // using HM for easier access to primary key data
    private User currentUser;

    public boolean registerUser(String username, String password) {
        if (users.containsKey(username)) {
            return false; // for when the user is trying to register with a name that is alreaddy in the database
        } else {
            users.put(username, new User(username, password));
            return true;
        }
    }

    public boolean loginUser(String username, String password) {
        User user = users.get(username);

        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            return true; // simple login matching
        } else {
            return false;
        }
    }

    public void logoutUser() {
        currentUser = null; // sets the value of currentUser to none when logging out
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }
}
