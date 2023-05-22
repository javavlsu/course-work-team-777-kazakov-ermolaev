package com.course.project.DistantLearning.service;

import com.course.project.DistantLearning.dto.response.LectorResponse;
import com.course.project.DistantLearning.models.Lector;
import com.course.project.DistantLearning.models.Student;
import com.course.project.DistantLearning.models.User;
import com.course.project.DistantLearning.repository.LectorRepository;
import com.course.project.DistantLearning.repository.StudentRepository;
import com.course.project.DistantLearning.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    StudentRepository studentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    LectorRepository lectorRepository;

    public String getCurrentUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return currentPrincipalName;
    }

    public User getAuthorizeUser() {
        return userRepository.findByUsername(getCurrentUserName()).get();
    }

    public Student getStudent() {
        return studentRepository.findByUser(userRepository.findByUsername(getCurrentUserName()).get()).get();
    }

    public List<LectorResponse> getLectors() {
        var lectors = lectorRepository.findAll();
        List<LectorResponse> listLector = new ArrayList<>();

        if (!lectors.isEmpty()) {

            for(var lector: lectors) {
                var lectorResponse = new LectorResponse();
                lectorResponse.setId(lector.getId());
                lectorResponse.setName(lector.getUser().getFullName());

                listLector.add(lectorResponse);
            }
        }

        return listLector;
    }

    public Lector getAuthorizeLector() {
        return lectorRepository.findByUser(userRepository.findByUsername(getCurrentUserName()).get()).get();
    }
}
