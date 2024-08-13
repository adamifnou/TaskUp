package com.taskup.demo.controller;

import com.taskup.demo.constants.TaskStatus;
import com.taskup.demo.dto.UserDTO;
import com.taskup.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.taskup.demo.service.AdminService;
import com.taskup.demo.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200/")
@RestController
public class AdminController {
    private final AdminService adminService;
    private final UserService userService;


    @Autowired
    public AdminController(AdminService adminService, UserService userService) {
        this.adminService = adminService;
        this.userService = userService;
    }

    @GetMapping("/admin/{adminId}")
    @ResponseBody
    public String welcome() {
        return "Hello Admin";
    }

    // 7- as an admin i want to be able to add/update and delete a user.
    // as an admin i want to be asked for my badge number before adding/updating or deleting a user.
    @PostMapping("/admin/{adminId}/addUser")
    @ResponseBody
    public ResponseEntity<String> addUser(@PathVariable("adminId") int adminId,
                                        @RequestBody User user,
                                        @RequestParam("badgeNumber") String badgeNumber) {
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        int age = user.getAge();

        if (badgeNumber == null) {
            return ResponseEntity.badRequest().body("Missing required parameter: 'badgeNumber'");
        } else if (!badgeNumber.equals(adminService.getAdminById(adminId).getBadgeNumber())) {
            System.out.println("Invalid badge number"+badgeNumber);
            return ResponseEntity.badRequest().body("Invalid badge number" + badgeNumber);
        } else {
            // Add user
            userService.addUser(new User(firstName, lastName, age));

            return ResponseEntity.ok("User added successfully");
        }

    }

    // 8- as an admin i want to be able to see all users.

        @GetMapping("/admin/{adminId}/users")
        @ResponseBody
        public ResponseEntity<Object> getUsers(@PathVariable("adminId") int adminId,
                                               @RequestParam("badgeNumber") String badgeNumber) {
            if (badgeNumber == null) {
                return ResponseEntity.badRequest().body("Missing required parameter: 'badgeNumber'");
            } else if (!badgeNumber.equals(adminService.getAdminById(adminId).getBadgeNumber())) {
                return ResponseEntity.badRequest().body("Invalid badge number" + badgeNumber);
            } else {
                // Get all users
                List<User> users = userService.getAllUsers();

                // Convert to UserDTO
                List<UserDTO> userDTOs = users.stream().map(user -> {
                    UserDTO userDTO = new UserDTO();
                    userDTO.setId(user.getId());
                    userDTO.setFirstName(user.getFirstName());
                    userDTO.setLastName(user.getLastName());
                    userDTO.setAge(user.getAge());
                    return userDTO;
                }).collect(Collectors.toList());

                // Respond with JSON of all users without tasks
                return ResponseEntity.ok(userDTOs);
            }
        }



    // 9- as an admin i want to be able to see all users with incomplete tasks.
    @GetMapping("/admin/{adminId}/users/incompleteTasks")
    @ResponseBody
    public ResponseEntity<Object> getUsersWithIncompleteTasks(@PathVariable("adminId") int adminId,
                                                               @RequestParam("badgeNumber") String badgeNumber) {
        if (badgeNumber == null) {
            return ResponseEntity.badRequest().body("Missing required parameter: 'badgeNumber'");
        } else if (!badgeNumber.equals(adminService.getAdminById(adminId).getBadgeNumber())) {
            return ResponseEntity.badRequest().body("Invalid badge number" + badgeNumber);
        } else {
            // Get all users
            List<User> users = userService.getAllUsers();
            List<User> usersWithIncompleteTasks = new ArrayList<>();
        for(User user: users){
                boolean hasIncompleteTask = false;
                List<Task> tasks = user.getTasks();
                for(Task task : tasks) {
                    if(!task.getStatus().equals(TaskStatus.COMPLETED)){
                        hasIncompleteTask = true;
                        break;
                    }
                }
                if(hasIncompleteTask){
                    usersWithIncompleteTasks.add(user);
                }
            }
            // Convert to UserDTO
            List<UserDTO> userDTOs = usersWithIncompleteTasks.stream().map(user -> {
                UserDTO userDTO = new UserDTO();
                userDTO.setId(user.getId());
                userDTO.setFirstName(user.getFirstName());
                userDTO.setLastName(user.getLastName());
                userDTO.setAge(user.getAge());
                return userDTO;
            }).collect(Collectors.toList());
            return ResponseEntity.ok( userDTOs);
        }
    }

}
