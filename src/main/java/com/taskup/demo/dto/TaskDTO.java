package com.taskup.demo.dto;

import com.taskup.demo.constants.TaskStatus;

import java.time.LocalDateTime;

public class TaskDTO {
    private int id;

    private String title;
    private String description;
    // creation datetime
    private LocalDateTime creationDateTime;
    // deadline datetime
    private LocalDateTime deadlineDateTime;
    // status of the task
    private TaskStatus status;
    // was modified by default is false
    private boolean wasModified = false;
    // user who created the task


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(LocalDateTime creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public LocalDateTime getDeadlineDateTime() {
        return deadlineDateTime;
    }

    public void setDeadlineDateTime(LocalDateTime deadlineDateTime) {
        this.deadlineDateTime = deadlineDateTime;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public boolean isWasModified() {
        return wasModified;
    }

    public void setWasModified(boolean wasModified) {
        this.wasModified = wasModified;
    }
}
