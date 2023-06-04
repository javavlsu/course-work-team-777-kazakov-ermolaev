package com.course.project.DistantLearning.controllers;

import com.course.project.DistantLearning.dto.response.MessageResponse;
import com.course.project.DistantLearning.models.LabWork;
import com.course.project.DistantLearning.repository.LabWorkRepository;
import com.course.project.DistantLearning.service.LabWorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/api/{idDiscipline}/labworks")
public class LabWorkController {
    @Autowired
    LabWorkService labWorkService;

    @GetMapping
    @PreAuthorize("hasRole('STUDENT') or hasRole('LECTOR') or hasRole('ADMIN')")
    public ResponseEntity<List<LabWork>> getLabWork(@PathVariable("idDiscipline") Long idDiscipline) {
        List<LabWork> labWorks = labWorkService.getLabWorkByDiscipline(idDiscipline);

//        if (labWorks.isEmpty())
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(labWorks, HttpStatus.OK);
    }

    @GetMapping("/{idLabWork}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('LECTOR') or hasRole('ADMIN')")
    public ResponseEntity<LabWork> getTestById(@PathVariable("idLabWork") Long idLabWork) {
        Optional<LabWork> labWork = labWorkService.getLabWorkById(idLabWork);

        return labWork.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }
    @PostMapping
    @PreAuthorize("hasRole('LECTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> createLabWork(@PathVariable("idDiscipline") Long idDiscipline, @RequestBody LabWork labWork) {
        if (labWorkService.existsByTitle(labWork.getTitle()))
            return ResponseEntity.badRequest().body(new MessageResponse("Ошибка: Лабораторная работа с таким название уже существует!"));

        if (labWork.getTitle() == "")
            return ResponseEntity.badRequest().body(new MessageResponse("Ошибка: название л.р. не заполнено!"));

        if (labWork.getDeadline().before(new Date()))
            return ResponseEntity.badRequest().body(new MessageResponse("Ошибка: Лабораторная работа не может заканчиваться раньше сегодняшней даты!"));

        if (labWorkService.createLabWork(idDiscipline, labWork))
            return ResponseEntity.ok(new MessageResponse("LabWork is creating"));

        return ResponseEntity.badRequest().body(new MessageResponse("Error: LabWork was not created"));
    }

    @PutMapping("/{idLabWork}")
    @PreAuthorize("hasRole('LECTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> updateTest(@PathVariable("idLabWork") Long idTest, @RequestBody LabWork labWork) {
        if (labWork.getTitle() == "") {
            return ResponseEntity.badRequest().body(new MessageResponse("Ошибка: название л.р. не заполнено!"));
        }

        if (labWork.getDeadline().before(new Date()))
            return ResponseEntity.badRequest().body(new MessageResponse("Ошибка: Лабораторная работа не может заканчиваться раньше сегодняшней даты!"));

        if (labWorkService.updateLabWork(idTest, labWork))
            return ResponseEntity.ok(new MessageResponse("LabWork has updated"));

        return ResponseEntity.badRequest().body(new MessageResponse("Error: LabWork has not updated"));
    }

    @DeleteMapping("/{idLabWork}")
    @PreAuthorize("hasRole('LECTOR') or hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteDiscipline(@PathVariable("idLabWork") Long idLabWork) {
        try {
            labWorkService.deleteLabWork(idLabWork);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
