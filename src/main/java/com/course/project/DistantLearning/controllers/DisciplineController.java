package com.course.project.DistantLearning.controllers;

import com.course.project.DistantLearning.dto.request.CreateOrUpdateDisciplineRequest;
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

    @GetMapping("/withLector/{idLector}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Discipline>> getLectorsHasDisciplines(@PathVariable("idLector") Long idLector) {
        List<Discipline> disciplines = disciplineService.getLectorsHasDisciplines(idLector);

        if (disciplines.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(disciplines, HttpStatus.OK);
    }

    @GetMapping("/without/{idLector}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Discipline>> getLectorsHasNotDisciplines(@PathVariable("idLector") Long idLector) {
        List<Discipline> disciplines = disciplineService.getLectorHasNotDisciplines(idLector);

        if (disciplines.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(disciplines, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createDiscipline(@RequestBody CreateOrUpdateDisciplineRequest createOrUpdateDisciplineRequest) {
        disciplineService.createDiscipline(createOrUpdateDisciplineRequest);
        return ResponseEntity.ok(new MessageResponse("Discipline is creating"));
    }

//    @PostMapping
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<?> updateDiscipline(@PathVariable("id") long id, @RequestBody CreateOrUpdateDisciplineRequest createOrUpdateDisciplineRequest) {
//        disciplineService.updateDiscipline(createOrUpdateDisciplineRequest);
//        return ResponseEntity.ok(new MessageResponse("Discipline is updating"));
//    }

}
