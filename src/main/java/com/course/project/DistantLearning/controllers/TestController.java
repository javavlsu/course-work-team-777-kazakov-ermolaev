package com.course.project.DistantLearning.controllers;

import com.course.project.DistantLearning.dto.response.MessageResponse;
import com.course.project.DistantLearning.models.Test;
import com.course.project.DistantLearning.service.TestService;
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
@RequestMapping("/api/{idDiscipline}/tests")
public class TestController {
    @Autowired
    TestService testService;

    @GetMapping
    @PreAuthorize("hasRole('STUDENT') or hasRole('LECTOR') or hasRole('ADMIN')")
    public ResponseEntity<List<Test>> getTest(@PathVariable("idDiscipline") Long idDiscipline) {
        List<Test> tests = testService.getTestByDiscipline(idDiscipline);

        if (tests.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(tests, HttpStatus.OK);
    }

    @GetMapping("/{idTest}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('LECTOR') or hasRole('ADMIN')")
    public ResponseEntity<Test> getTestById(@PathVariable("idTest") Long idTest) {
        Optional<Test> test = testService.getTestById(idTest);

        if (test.isPresent())
            return new ResponseEntity<>(test.get(), HttpStatus.OK);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PostMapping
    @PreAuthorize("hasRole('LECTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> createTest(@PathVariable("idDiscipline") Long idDiscipline, @RequestBody Test test) {
        if (testService.existsByTitle(test.getTitle()))
            return ResponseEntity.badRequest().body(new MessageResponse("Ошибка: тест с таким название уже существует!"));

        if (test.getTitle() == "")
            return ResponseEntity.badRequest().body(new MessageResponse("Ошибка: название теста не заполнено!"));

        if (test.getDateStart().after(test.getDeadline()))
            return ResponseEntity.badRequest().body(new MessageResponse("Ошибка: тест начинается позже чем заканчивается!"));

        if (test.getDateStart().before(new Date()))
            return ResponseEntity.badRequest().body(new MessageResponse("Ошибка: тест не может начинаться раньше сегодняшней даты!"));

        if (testService.createTest(idDiscipline, test)) {
            return ResponseEntity.ok(new MessageResponse("Test is creating"));
        };

        return ResponseEntity.badRequest().body(new MessageResponse("Error: test was not created"));
    }

    @PutMapping("/{idTest}")
    @PreAuthorize("hasRole('LECTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> updateTest(@PathVariable("idTest") Long idTest, @RequestBody Test test) {
        /*if (testService.existsByTitle(test.getTitle())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Ошибка: тест с таким название уже существует!"));
        }*/

        if (test.getTitle() == "") {
            return ResponseEntity.badRequest().body(new MessageResponse("Ошибка: название теста не заполнено!"));
        }

        if (test.getDateStart().after(test.getDeadline()))
            return ResponseEntity.badRequest().body(new MessageResponse("Ошибка: тест начинается позже чем заканчивается!"));

        if (test.getDateStart().before(new Date()))
            return ResponseEntity.badRequest().body(new MessageResponse("Ошибка: тест не может начинаться раньше сегодняшней даты!"));

        if (testService.updateTest(idTest, test)) {
            return ResponseEntity.ok(new MessageResponse("Test has updated"));
        };

        return ResponseEntity.badRequest().body(new MessageResponse("Error: Test has not updated"));
    }

    @DeleteMapping("/{idTest}")
    @PreAuthorize("hasRole('LECTOR') or hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteDiscipline(@PathVariable("idTest") Long idTest) {
        try {
            testService.deleteTest(idTest);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
