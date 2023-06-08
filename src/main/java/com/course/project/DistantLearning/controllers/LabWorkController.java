package com.course.project.DistantLearning.controllers;

import com.course.project.DistantLearning.dto.response.MessageResponse;
import com.course.project.DistantLearning.dto.response.StudentLabWorkResponse;
import com.course.project.DistantLearning.dto.response.StudentResponse;
import com.course.project.DistantLearning.models.LabWork;
import com.course.project.DistantLearning.models.User;
import com.course.project.DistantLearning.repository.LabWorkRepository;
import com.course.project.DistantLearning.service.LabWorkService;
import com.course.project.DistantLearning.service.RoleService;
import com.course.project.DistantLearning.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/api/{idDiscipline}/labworks")
public class LabWorkController {
    @Autowired
    LabWorkService labWorkService;

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

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

    @GetMapping("/{idLabWork}/scoreStudentForLabWork")
    @PreAuthorize("hasRole('LECTOR') or hasRole('ADMIN')")
    public ResponseEntity<List<StudentResponse>> getScores(@PathVariable("idLabWork") Long idLabWork) {
        List<StudentResponse> studentResponseList = labWorkService.getScoresForLabWork(idLabWork);

        return new ResponseEntity<>(studentResponseList, HttpStatus.OK);
    }

    @GetMapping("/{idLabWork}/createScoreLabWork")
    @PreAuthorize("hasRole('STUDENT') or hasRole('LECTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> createScoreLabWork(@PathVariable("idDiscipline") Long idDiscipline, @PathVariable("idLabWork") Long idLabWork) {
        Optional<User> user = userService.getAuthorizeUser();

        if (user.isPresent()) {
            List<String> roles = new ArrayList<>(roleService.getUserRoles(user.get()));
            if (!roles.isEmpty()) {
                if(!roles.contains("ROLE_ADMIN") & !roles.contains("ROLE_LECTOR") & roles.contains("ROLE_STUDENT")) {
                    if (labWorkService.createScoreLabWork(idDiscipline, idLabWork, user.get()))
                        return ResponseEntity.ok().body(new MessageResponse("ScoreLabWork was created"));
                }
            }
        }

        return ResponseEntity.badRequest().body(new MessageResponse("ScoreLabWork did not created"));
    }

    @PutMapping("/{idLabWork}/putScore")
    @PreAuthorize("hasRole('LECTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> putLabWorkScore(@PathVariable("idLabWork") Long idLabWork, @RequestBody StudentLabWorkResponse studentLabWorkResponse) {
        if (labWorkService.updateScoreLabWork(idLabWork, studentLabWorkResponse.getIdStudent(), studentLabWorkResponse.getUser(), studentLabWorkResponse.getScore())) {
            return ResponseEntity.ok(new MessageResponse("Score for labWork has updated!"));
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Update score ofr labWork has crashed!"));
    }

}
