import java.util.ArrayList;

public class taskManager {
    private ArrayList<tasks> tasks;

    public taskManager() {
        tasks = new ArrayList<>();
    }
    
    public void addTask(tasks task) { tasks.add(task); }

    public void removeTask(String title) throws taskNFE {
        tasks taskToRemove = getTaskByTitle(title);
        if (taskToRemove != null) {
            tasks.remove(taskToRemove);
        } else {
            throw new taskNFE("Task '" + title + "' not found.");
        }
    }

    public tasks getTaskByTitle(String title) {
        for (tasks task : tasks) {
            if (task.getTitle().equalsIgnoreCase(title)) {
                return task;
            }
        }
        return null;
    }

    public ArrayList<tasks> getAllTasks() {
        return tasks;
    }
}
