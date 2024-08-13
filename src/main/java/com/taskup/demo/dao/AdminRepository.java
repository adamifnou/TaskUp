package com.taskup.demo.dao;

import com.taskup.demo.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Admin getAdminById(int id);
}
