package com.sushi;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
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
            Gson gson = new Gson();
            tasks = gson.fromJson(reader, new TypeToken<List<Task>>() {
            }.getType());
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
            for (Task task : tasks) {
                if (task.getId().equals(UUID.fromString(id))) {
                    throw new TaskNFE("Task '" + task.getTitle() + "' not found.");
                }
            }
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

    public void updateTaskStatus(String id, boolean isCompleted) {
        Task task = getTaskById(id);
        if (task != null) {
            if (isCompleted) {
                tasks.remove(task);
                tasks.add(task);
            } else {
                tasks.remove(task);
                tasks.add(0, task);
            }
            saveTasks();
        }

    }

    public List<Task> getAllTasks() {
        return tasks;
    }
}
