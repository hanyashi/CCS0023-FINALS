import java.util.ArrayList;

public class TaskManager {
    private final ArrayList<Tasks> tasks;

    public TaskManager() {
        tasks = new ArrayList<>();
    }
    
    public void addTask(Tasks task) { tasks.add(task); }

    public void removeTask(String title) throws TaskNFE {
        Tasks taskToRemove = getTaskByTitle(title);
        if (taskToRemove != null) {
            tasks.remove(taskToRemove);
        } else {
            throw new TaskNFE("Task '" + title + "' not found.");
        }
    }

    public Tasks getTaskByTitle(String title) {
        for (Tasks task : tasks) {
            if (task.getTitle().equalsIgnoreCase(title)) {
                return task;
            }
        }
        return null;
    }

    public ArrayList<Tasks> getAllTasks() {
        return tasks;
    }
}
