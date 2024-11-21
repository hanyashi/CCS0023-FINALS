package com.sushi;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public final class TaskManager {
    private List<Task> tasks;

    public TaskManager() {
        tasks = new ArrayList<>();
        loadTasks();
    }

    public void loadTasks() {
    try (FileReader reader = new FileReader("tasks.json")) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        tasks = gson.fromJson(reader, new TypeToken<List<Task>>() {}.getType());
        if (tasks == null) {
            tasks = new ArrayList<>();
        }
    } catch (IOException e) {
        System.err.println("Could not load tasks.json: " + e.getMessage());
        tasks = new ArrayList<>();
    }
}


    public void saveTasks() {
        try (Writer writer = new FileWriter("tasks.json")) {
            Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .setPrettyPrinting()
            .create();    
            gson.toJson(tasks, writer);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addTask(Task task) {
        tasks.add(task);
        saveTasks();
    }

    public void removeTask(String id) throws TaskNFE {
        Task taskToRemove = getTaskById(id);
        if (taskToRemove != null) {
            tasks.remove(taskToRemove);
            saveTasks();
        } else {
            throw new TaskNFE("Task with ID '" + id + "' not found.");
        }
    }
    

    public Task getTaskById(String id) {
        for (Task task : tasks) {
            if (task.getId().equals(UUID.fromString(id))) {
                return task;
            }
        }
        return null;
    }

    public List<Task> getAllTasks() {
        return tasks;
    }
}
