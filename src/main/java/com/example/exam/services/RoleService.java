package com.example.exam.services;
import com.example.exam.entities.Role;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class RoleService {

    public List<Role> getAllRoles() {
        return Arrays.asList(Role.values());
    }

    public List<String> getAllRoleNames() {
        return Arrays.asList(Role.values()).stream()
                .map(Role::name)
                .toList();
    }
}