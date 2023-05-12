package com.course.project.DistantLearning.controllers;

import com.course.project.DistantLearning.models.Discipline;
import com.course.project.DistantLearning.models.Student;
import com.course.project.DistantLearning.queries.response.MessageResponse;
import com.course.project.DistantLearning.service.DisciplineService;
import com.course.project.DistantLearning.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/discipline")
public class DisciplineController {
    @Autowired
    DisciplineService disciplineService;

    @Autowired
    StudentService studentService;

    @GetMapping
    @PreAuthorize("hasRole('STUDENT') or hasRole('LECTOR') or hasRole('ADMIN')")
    public ResponseEntity<List<Discipline>> getDisciplines() {
        Student student = studentService.getStudent();
        List<Discipline> disciplines = disciplineService.getDiscipline(student.getGroup().getId());

        if (disciplines.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(disciplines, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('LECTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> createDiscipline(@RequestBody Discipline discipline) {
        disciplineService.createDiscipline(discipline);
        return ResponseEntity.ok(new MessageResponse("Discipline is creating"));
    }


    @GetMapping("/student")
    @PreAuthorize("hasRole('STUDENT') or hasRole('LECTOR') or hasRole('ADMIN')")
    public ResponseEntity<Student> getStudent() {
        return new ResponseEntity<>(studentService.getStudent(), HttpStatus.OK);
    }

}
