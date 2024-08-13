package com.taskup.demo.service;

import com.taskup.demo.dao.TaskRepository;
import com.taskup.demo.model.Task;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {
    final private TaskRepository taskRepository;;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

   @Override
    public void addTask(Task task) {
        taskRepository.save(task);
    }

    @Override
    public Task getTaskById(int taskId) {
        return taskRepository.getTaskById(taskId);
    }

    @Override
    public void deleteTask(int taskId) {
        taskRepository.deleteById(taskId);
    }
}
