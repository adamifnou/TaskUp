package com.taskup.demo.service;

import com.taskup.demo.model.Task;

public interface TaskService {
    Task getTaskById(int taskId);
    void addTask(Task task);
    void deleteTask(int taskId);
}
