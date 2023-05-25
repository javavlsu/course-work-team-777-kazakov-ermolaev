package com.course.project.DistantLearning.controllers;

import com.course.project.DistantLearning.dto.request.SignupRequest;
import com.course.project.DistantLearning.dto.response.LectorResponse;
import com.course.project.DistantLearning.dto.response.MessageResponse;
import com.course.project.DistantLearning.dto.response.StudentResponse;
import com.course.project.DistantLearning.models.ERole;
import com.course.project.DistantLearning.models.Role;
import com.course.project.DistantLearning.models.Student;
import com.course.project.DistantLearning.models.User;
import com.course.project.DistantLearning.repository.RoleRepository;
import com.course.project.DistantLearning.repository.UserRepository;
import com.course.project.DistantLearning.service.RoleService;
import com.course.project.DistantLearning.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    PasswordEncoder encoder;

    @PostMapping("/newuser")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> newUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userService.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userService.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getFullName(),
                signUpRequest.getBirthdate());

        user.setRoles(roleService.getNewUserRoles(signUpRequest.getRole()));
        userService.createUser(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }


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

    @GetMapping("/students")
    @PreAuthorize("hasRole('LECTOR') or hasRole('ADMIN')")
    public ResponseEntity<List<StudentResponse>> getStudents() {
        var students = userService.getStudents();

        if (!students.isEmpty()) {
            return new ResponseEntity<>(students, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/students/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteStudent(@PathVariable("id") Long idStudent) {
        try {
            userService.deleteStudent(idStudent);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/lectors/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteLector(@PathVariable("id") Long idLector) {
        try {
            userService.deleteLector(idLector);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
