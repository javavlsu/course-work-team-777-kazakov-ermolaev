package com.course.project.DistantLearning.controllers;

import com.course.project.DistantLearning.dto.request.CreateDisciplineRequest;
import com.course.project.DistantLearning.models.Discipline;
import com.course.project.DistantLearning.models.Student;
import com.course.project.DistantLearning.dto.response.MessageResponse;
import com.course.project.DistantLearning.models.User;
import com.course.project.DistantLearning.service.DisciplineService;
import com.course.project.DistantLearning.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/api/discipline")
public class DisciplineController {
    @Autowired
    DisciplineService disciplineService;

    @Autowired
    UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('STUDENT') or hasRole('LECTOR') or hasRole('ADMIN')")
    public ResponseEntity<List<Discipline>> getDisciplines() {
        User user = userService.getAuthorizeUser();
        List<Discipline> disciplines = disciplineService.getDiscipline(user.getId());

        if (disciplines.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(disciplines, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createDiscipline(@RequestBody CreateDisciplineRequest createDisciplineRequest) {
        disciplineService.createDiscipline(createDisciplineRequest);
        return ResponseEntity.ok(new MessageResponse("Discipline is creating"));
    }


    @GetMapping("/student")
    @PreAuthorize("hasRole('STUDENT') or hasRole('LECTOR') or hasRole('ADMIN')")
    public ResponseEntity<Student> getStudent() {
        return new ResponseEntity<>(userService.getStudent(), HttpStatus.OK);
    }

}
