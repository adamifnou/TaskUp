package com.taskup.demo.service;

import com.taskup.demo.dao.AdminRepository;
import com.taskup.demo.model.Admin;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    final private AdminRepository adminRepository;

    public AdminServiceImpl(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }
    @Override
    public Admin getAdminById(int adminId) {
        return adminRepository.getAdminById(adminId);
    }
}
