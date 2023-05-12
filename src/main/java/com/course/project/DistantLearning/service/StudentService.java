package com.course.project.DistantLearning.service;

import com.course.project.DistantLearning.models.Student;
import com.course.project.DistantLearning.repository.StudentRepository;
import com.course.project.DistantLearning.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class StudentService {
    @Autowired
    StudentRepository studentRepository;

    @Autowired
    UserRepository userRepository;

    public String getCurrentUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return currentPrincipalName;
    }

    public Student getStudent() {
        return studentRepository.findByUser(userRepository.findByUsername(getCurrentUserName()).get()).get();
    }
}
