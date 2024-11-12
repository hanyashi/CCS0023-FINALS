import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;
    private List<Tasks> taskList;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.taskList = new ArrayList<>();
    }

    // getters n setters for usernames and pws
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<Tasks> getTaskList() {
        return taskList;
    }

    public void addTask(Tasks task) {
        taskList.add(task);
    }

    public void removeTask(Tasks task) {
        taskList.remove(task);
    }
}
