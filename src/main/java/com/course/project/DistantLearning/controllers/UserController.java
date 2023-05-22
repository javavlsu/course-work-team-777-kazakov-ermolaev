package com.course.project.DistantLearning.controllers;

import com.course.project.DistantLearning.dto.response.LectorResponse;
import com.course.project.DistantLearning.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/lectors")
    @PreAuthorize("hasRole('LECTOR') or hasRole('ADMIN')")
    public ResponseEntity<List<LectorResponse>> getLectors() {
        var lectors = userService.getLectors();

        if (!lectors.isEmpty()) {
            return new ResponseEntity<>(lectors, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
