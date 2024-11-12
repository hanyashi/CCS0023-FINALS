package com.sushi;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public final class TaskManager {
    private List<Tasks> tasks;

    public TaskManager() {
        tasks = new ArrayList<>();
        loadTasks();
    }

    public void loadTasks() {
        try (FileReader reader = new FileReader("tasks.json")) {
            Gson gson = new Gson();
            tasks = gson.fromJson(reader, new TypeToken<List<Tasks>>(){}.getType());
            if (tasks == null) {
                tasks = new ArrayList<>();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    public void saveTasks() {
    try (Writer writer = new FileWriter("tasks.json")) {
        Gson gson;
        gson = new GsonBuilder().setPrettyPrinting().create();
        gson.toJson(tasks, writer);
    } catch (IOException e) {
        e.printStackTrace();
    }
    }
    
    public void addTask(Tasks task) { 
        tasks.add(task); 
        saveTasks();
    }

    public void removeTask(String title) throws TaskNFE {
        Tasks taskToRemove = getTaskByTitle(title);
        if (taskToRemove != null) {
            tasks.remove(taskToRemove);
            saveTasks();
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

    public List<Tasks> getAllTasks() {
        return tasks;
    }
}
