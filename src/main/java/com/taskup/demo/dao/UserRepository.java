package com.taskup.demo.dao;

import com.taskup.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    User getUserById(int id);

}