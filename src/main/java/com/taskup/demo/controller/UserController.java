package com.taskup.demo.controller;

import com.taskup.demo.constants.TaskStatus;
import com.taskup.demo.dto.TaskDTO;
import com.taskup.demo.model.Task;
import com.taskup.demo.service.TaskService;
import com.taskup.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RestController
public class UserController {
    private final UserService userService;
    private final TaskService taskService;

    @Autowired
    public UserController(UserService userService, TaskService taskService) {
        this.userService = userService;
        this.taskService = taskService;
    }
    @GetMapping("/user/welcome")
    public String welcome() {
        return "Hello User";
    }
     // Fonctionnalités attendues :
     // 1- En tant que utilisateur, je veux pouvoir Ajouter des taches.
    @PostMapping("/user/{userId}/addTask")
    @ResponseBody
    public ResponseEntity<String> addTask(@PathVariable("userId") int userId,
                                          @RequestBody Task task) {
        if(!(userService.getUserById(userId) == null)){
            task.setCreationDateTime(LocalDateTime.now());
            //make sure the deadline is not null
            if(task.getDeadlineDateTime() == null){
                return ResponseEntity.badRequest().body("Missing required parameter: 'deadlineDateTime'");
            }else{
                //cast the deadline to LocalDateTime
                task.setDeadlineDateTime(LocalDateTime.parse(task.getDeadlineDateTime().toString()));
            }
            // add the creator of the task
            task.setCreator(userService.getUserById(userId));

            taskService.addTask(task);
            return ResponseEntity.ok("Task added successfully");

        } else {
            return ResponseEntity.badRequest().body("User not found");
        }
    }

     // 2- En tant que utilisateur, je veux pouvoir Modifier des taches.
    @PutMapping("/user/{userId}/updateTask/{taskId}")
    @ResponseBody
    public ResponseEntity<String> updateTask(@PathVariable("userId") int userId,
                                             @PathVariable("taskId") int taskId,
                                             @RequestBody Task task) {
        Task taskToUpdate = taskService.getTaskById(taskId);
        System.out.println("taskToUpdate: " + taskToUpdate);
        if (taskToUpdate == null) {
            return ResponseEntity.badRequest().body("Task not found" );
        }
        if (taskToUpdate.getCreator().getId() != userId) {
            return ResponseEntity.badRequest().body("You are not the creator of this task");
        }
        taskToUpdate.setTitle(task.getTitle());
        taskToUpdate.setDescription(task.getDescription());
        taskToUpdate.setDeadlineDateTime(task.getDeadlineDateTime());
        taskToUpdate.setStatus(task.getStatus());
        taskToUpdate.setWasModified(true);
        taskService.addTask(taskToUpdate);
        return ResponseEntity.ok("Task updated successfully");
    }
     // 3- En tant que utilisateur, je veux pouvoir Supprimer des taches.
    @DeleteMapping("/user/{userId}/deleteTask/{taskId}")
    @ResponseBody
    public ResponseEntity<String> deleteTask(@PathVariable("userId") int userId,
                                             @PathVariable("taskId") int taskId) {
        Task task = taskService.getTaskById(taskId);
        if (task == null) {
            return ResponseEntity.badRequest().body("Task not found");
        }
        if (task.getCreator().getId() != userId) {
            return ResponseEntity.badRequest().body("You are not the creator of this task");
        }
        taskService.deleteTask(taskId);
        return ResponseEntity.ok("Task deleted successfully");
    }

     // 4- En tant que utilisateur, je veux pouvoir Voir tout mes taches.
    @GetMapping("/user/{userId}/tasks")
    @ResponseBody
    public ResponseEntity<Object> getTasks(@PathVariable("userId") int userId) {
        if (userService.getUserById(userId) == null) {
            return ResponseEntity.badRequest().body("User not found");
        }
        // Get all tasks
        List<Task> tasks = userService.getUserById(userId).getTasks();
        List<TaskDTO> tasksDto = new ArrayList<>();
        if (tasks.isEmpty()) {
            return ResponseEntity.ok("No tasks found");
        }else {
           //convert to TaskDTO with map
           tasksDto = tasks.stream().map(task -> {
                TaskDTO taskDTO = new TaskDTO();
                taskDTO.setId(task.getId());
                taskDTO.setTitle(task.getTitle());
                taskDTO.setDescription(task.getDescription());
                taskDTO.setCreationDateTime(task.getCreationDateTime());
                taskDTO.setDeadlineDateTime(task.getDeadlineDateTime());
                taskDTO.setStatus(task.getStatus());
                taskDTO.setWasModified(task.isWasModified());
                return taskDTO;
            }).collect(Collectors.toList());
        }
        return ResponseEntity.ok(  tasksDto);
        }
        
     // 5- En tant que utilisateur, je veux pouvoir Voir tout mes taches incomplets.
    @GetMapping("/user/{userId}/incompleteTasks")
    @ResponseBody
    public ResponseEntity<Object> getIncompleteTasks(@PathVariable("userId") int userId) {
        if (userService.getUserById(userId) == null) {
            return ResponseEntity.badRequest().body("User not found");
        }
        // Get all tasks
        List<Task> tasks = userService.getUserById(userId).getTasks();
        List<TaskDTO> tasksDto = new ArrayList<>();
        if (tasks.isEmpty()) {
            return ResponseEntity.ok("No tasks found");
        } else {
            for (Task task : tasks) {
                if (!task.getStatus().equals(TaskStatus.COMPLETED)) {
                    TaskDTO taskDTO = new TaskDTO();
                    taskDTO.setId(task.getId());
                    taskDTO.setTitle(task.getTitle());
                    taskDTO.setDescription(task.getDescription());
                    taskDTO.setCreationDateTime(task.getCreationDateTime());
                    taskDTO.setDeadlineDateTime(task.getDeadlineDateTime());
                    taskDTO.setStatus(task.getStatus());
                    taskDTO.setWasModified(task.isWasModified());
                    tasksDto.add(taskDTO);

                }
            }
        }
        return ResponseEntity.ok(tasks);
    }

     // 6- En tant que utilisateur, je veux pouvoir Voir tout mes taches complets.
     @GetMapping("/user/{userId}/completeTasks")
     @ResponseBody
     public ResponseEntity<String> getCompleteTasks(@PathVariable("userId") int userId) {
         if (userService.getUserById(userId) == null) {
             return ResponseEntity.badRequest().body("User not found");
         }
         // Get all tasks
         List<Task> tasks = userService.getUserById(userId).getTasks();
         List<String> tasksDetails = new ArrayList<>();
         if (tasks.isEmpty()) {
             return ResponseEntity.ok("No tasks found");
         } else {
             for (Task task : tasks) {
                 if (task.getStatus().equals(TaskStatus.COMPLETED)) {
                     tasksDetails.add("Title: " + task.getTitle() + "\n" + "Description: " + task.getDescription() + "\n" + "Deadline: " + task.getDeadlineDateTime() + "\n" + "Status: " + task.getStatus());
                 }
             }
         }
         return ResponseEntity.ok("Completed Tasks: " + tasksDetails);
     }
}
