package com.course.project.DistantLearning.service;

import com.course.project.DistantLearning.models.ERole;
import com.course.project.DistantLearning.models.Role;
import com.course.project.DistantLearning.models.User;
import com.course.project.DistantLearning.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public List<String> getUserRoles(User user) {
        List<String> roles = new ArrayList<>();

        for(var role: user.getRoles()) {
            roles.add(role.getName().toString());
        }

        return roles;
    }

    public Set<Role> getNewUserRoles(String userRole) {
        Set<Role> roles = new HashSet<>();
        Set<String> strRoles = new HashSet<>();
        strRoles.add(userRole);

        if (strRoles.contains("admin")) {
            strRoles.add("lector");
            strRoles.add("student");
        } else if (strRoles.contains("lector")) {
            strRoles.add("student");
        }

        strRoles.forEach(role -> {
            switch (role) {
                case "admin":
                    Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(adminRole);

                    break;
                case "lector":
                    Role lectorRole = roleRepository.findByName(ERole.ROLE_LECTOR)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(lectorRole);

                    break;
                default:
                    Role studentRole = roleRepository.findByName(ERole.ROLE_STUDENT)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(studentRole);
            }
        });

        return roles;
    }
}
