package com.taskup.demo.service;

import com.taskup.demo.dao.UserRepository;
import com.taskup.demo.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    final private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public void addUser(User user) {
        userRepository.save(user);
    }
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll( );
    }
    @Override
    public User getUserById(int userId) {
        return userRepository.getUserById(userId);
    }
}
