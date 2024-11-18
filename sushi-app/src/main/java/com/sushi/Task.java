package com.sushi;

import java.util.Date;
import java.util.UUID;

public class Task {
    private UUID id;
    private Boolean completed;
    private String title;
    private String description;
    private Date dueDate;
    private String priority;
    private String status;
    private String category;

    // TODO: Make unique identifier for Task

    // attributes
    public Task(Boolean completed, String title, String description, Date dueDate, String priority,
            String status,
            String category) {
        this.completed = completed;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.status = status;
        this.category = category;
    }

    // SETTERS & GETTERS FOR ATTRIBUTES
    public UUID getId() {
        if (id == null) {
            id = UUID.randomUUID();
        }
        return id;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
