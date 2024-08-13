package com.taskup.demo.service;

import com.taskup.demo.model.User;

import java.util.List;


public interface UserService {
    /*
    void saveUser(User user);
    void deleteUserById(int userId);
    void updateUser(User user);
    void deleteUser(User user);
*/
    User getUserById(int userId);

    void addUser(User user);
    //get all users
    List<User> getAllUsers();
}
