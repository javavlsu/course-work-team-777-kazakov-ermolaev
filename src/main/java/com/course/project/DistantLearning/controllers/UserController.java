package com.course.project.DistantLearning.controllers;

import com.course.project.DistantLearning.dto.request.SignupRequest;
import com.course.project.DistantLearning.dto.response.LectorResponse;
import com.course.project.DistantLearning.dto.response.MessageResponse;
import com.course.project.DistantLearning.dto.response.StudentResponse;
import com.course.project.DistantLearning.models.*;
import com.course.project.DistantLearning.repository.RoleRepository;
import com.course.project.DistantLearning.repository.UserRepository;
import com.course.project.DistantLearning.service.GroupService;
import com.course.project.DistantLearning.service.RoleService;
import com.course.project.DistantLearning.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    GroupService groupService;

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

    @GetMapping("/students/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable("id") Long idStudent) {
        Optional<Student> student = userService.getStudentById(idStudent);


        if (student.isPresent()) {
            Student _student = student.get();
            StudentResponse studentResponse = new StudentResponse();
            studentResponse.setId(_student.getId());
            studentResponse.setName(_student.getUser().getFullName());
            studentResponse.setEmail(_student.getUser().getEmail());

            try {
                studentResponse.setGroupName(_student.getGroup().getName());
            } catch (Exception e) {
                studentResponse.setGroupName("Без группы");
            }
            return new ResponseEntity<>(studentResponse, HttpStatus.OK);
        }
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/students/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateStudent(@PathVariable("id") Long idStudent, @RequestBody StudentResponse studentResponse) {
        return ResponseEntity.ok(userService.updateStudent(idStudent, studentResponse));
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

    @GetMapping("/lectors/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LectorResponse> getLectorById(@PathVariable("id") Long idLector) {
        Optional<Lector> lector = userService.getLectorById(idLector);

        if (lector.isPresent()) {
            Lector _lector = lector.get();
            LectorResponse lectorResponse = new LectorResponse(idLector, _lector.getUser().getFullName(), _lector.getUser().getEmail());
            return new ResponseEntity<>(lectorResponse, HttpStatus.OK);
        }
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/lectors/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateLector(@PathVariable("id") Long idLector, @RequestBody LectorResponse lectorResponse) {
        return ResponseEntity.ok(userService.updateLector(idLector, lectorResponse));
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

    @GetMapping("/students/groups")
    @PreAuthorize("hasRole('LECTOR') or hasRole('ADMIN')")
    public ResponseEntity<List<Group>> getGroups() {
        List<Group> groups = groupService.getGroups();

        if (groups.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(groups, HttpStatus.OK);
    }

    @PostMapping("/student/groups")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createGroup(@RequestBody Group group) {
        if (groupService.existsByName(group.getName())) {
            return ResponseEntity.ok(new MessageResponse("This groupName is already exists"));
        }
        groupService.createGroup(group);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/students/groups/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateGroup(@PathVariable("id") Long idGroup, @RequestBody Group group) {
        return ResponseEntity.ok(groupService.updateGroup(idGroup, group));
    }

    @DeleteMapping("/students/groups/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteGroup(@PathVariable("id") Long idGroup) {
        try {
            groupService.deleteGroup(idGroup);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
