package com.course.project.DistantLearning.controllers;

import com.course.project.DistantLearning.dto.request.CreateOrUpdateDisciplineRequest;
import com.course.project.DistantLearning.dto.response.LectorResponse;
import com.course.project.DistantLearning.models.Discipline;
import com.course.project.DistantLearning.models.Group;
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
import java.util.Optional;

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
        Optional<User> user = userService.getAuthorizeUser();
        List<Discipline> disciplines = new ArrayList<>();
        user.ifPresent(value -> disciplines.addAll(disciplineService.getDiscipline(value.getId())));

        if (disciplines.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(disciplines, HttpStatus.OK);
    }

    @GetMapping("/{idDiscipline}")
    @PreAuthorize("hasRole('LECTOR') or hasRole('ADMIN')")
    public ResponseEntity<Discipline> getDisciplineById(@PathVariable("idDiscipline") Long idDiscipline) {
        var discipline = disciplineService.getDisciplineById(idDiscipline);

        return discipline.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createDiscipline(@RequestBody CreateOrUpdateDisciplineRequest createOrUpdateDisciplineRequest) {
        if (disciplineService.existsByTitle(createOrUpdateDisciplineRequest.getTitle())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Ошибка: дисциплина с таким именем уже существует!"));
        }
        if (createOrUpdateDisciplineRequest.getTitle() == "") {
            return ResponseEntity.badRequest().body(new MessageResponse("Ошибка: имя дисциплины не заполнено!"));
        }

        disciplineService.createDiscipline(createOrUpdateDisciplineRequest);
        return ResponseEntity.ok(new MessageResponse("Discipline is creating"));
    }

    @PutMapping("/{idDiscipline}")
    @PreAuthorize("hasRole('LECTOR') or hasRole('ADMIN')")
    public  ResponseEntity<?> updateDiscipline(@PathVariable("idDiscipline") Long idDiscipline, @RequestBody CreateOrUpdateDisciplineRequest createOrUpdateDisciplineRequest) {
        return ResponseEntity.ok(disciplineService.updateDiscipline(idDiscipline, createOrUpdateDisciplineRequest));
    }

    @GetMapping("/groups/withDiscipline/{idDiscipline}")
    @PreAuthorize("hasRole('LECTOR') or hasRole('ADMIN')")
    public ResponseEntity<List<Group>> getGroupInByDisciplineId(@PathVariable("idDiscipline") Long idDiscipline) {
        List<Group> groups = disciplineService.getGroupInByDisciplineId(idDiscipline);

        if(groups.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(groups, HttpStatus.OK);
    }

    @GetMapping("/groups/withoutDiscipline/{idDiscipline}")
    @PreAuthorize("hasRole('LECTOR') or hasRole('ADMIN')")
    public ResponseEntity<List<Group>> getGroupOutByDisciplineId(@PathVariable("idDiscipline") Long idDiscipline) {
        List<Group> groups = disciplineService.getGroupOutByDisciplineId(idDiscipline);

        if(groups.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(groups, HttpStatus.OK);
    }

    @GetMapping("/lector/withDiscipline/{idDiscipline}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<LectorResponse>> getLectorsInByDisciplineId(@PathVariable("idDiscipline") Long idDiscipline) {
        List<LectorResponse> lectors = disciplineService.getLectorsInByDisciplineId(idDiscipline);

        if (lectors.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(lectors, HttpStatus.OK);
    }

    @GetMapping("/lector/withoutDiscipline/{idDiscipline}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<LectorResponse>> getLectorsOutByDisciplineId(@PathVariable("idDiscipline") Long idDiscipline) {
        List<LectorResponse> lectors = disciplineService.getLectorsOutByDisciplineId(idDiscipline);

        if (lectors.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(lectors, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteDiscipline(@PathVariable("id") Long idDiscipline) {
        try {
            disciplineService.deleteDiscipline(idDiscipline);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
