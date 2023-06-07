package com.course.project.DistantLearning.controllers;

import com.course.project.DistantLearning.dto.response.AnswerOptionResponse;
import com.course.project.DistantLearning.dto.request.UpdateAnswer;
import com.course.project.DistantLearning.dto.response.MessageResponse;
import com.course.project.DistantLearning.dto.response.StudentResponse;
import com.course.project.DistantLearning.models.*;
import com.course.project.DistantLearning.service.RoleService;
import com.course.project.DistantLearning.service.TestService;
import com.course.project.DistantLearning.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/api/{idDiscipline}/tests")
public class TestController {
    @Autowired
    TestService testService;

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @GetMapping
    @PreAuthorize("hasRole('STUDENT') or hasRole('LECTOR') or hasRole('ADMIN')")
    public ResponseEntity<List<Test>> getTest(@PathVariable("idDiscipline") Long idDiscipline) {
        List<Test> tests = testService.getTestByDiscipline(idDiscipline);

//        if (tests.isEmpty())
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(tests, HttpStatus.OK);
    }

    @GetMapping("/{idTest}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('LECTOR') or hasRole('ADMIN')")
    public ResponseEntity<Test> getTestById(@PathVariable("idTest") Long idTest) {
        Optional<Test> test = testService.getTestById(idTest);

        return test.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }
    @PostMapping
    @PreAuthorize("hasRole('LECTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> createTest(@PathVariable("idDiscipline") Long idDiscipline, @RequestBody Test test) {
        if (testService.testExistsByTitle(test.getTitle()))
            return ResponseEntity.badRequest().body(new MessageResponse("Ошибка: тест с таким название уже существует!"));

        if (test.getTitle() == "")
            return ResponseEntity.badRequest().body(new MessageResponse("Ошибка: название теста не заполнено!"));

        if (test.getDateStart().after(test.getDeadline()))
            return ResponseEntity.badRequest().body(new MessageResponse("Ошибка: тест начинается позже чем заканчивается!"));

        if (test.getDateStart().before(new Date()))
            return ResponseEntity.badRequest().body(new MessageResponse("Ошибка: тест не может начинаться раньше сегодняшней даты!"));

        if (testService.createTest(idDiscipline, test))
            return ResponseEntity.ok(new MessageResponse("Test is creating"));

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

        if (testService.updateTest(idTest, test))
            return ResponseEntity.ok(new MessageResponse("Test has updated"));

        return ResponseEntity.badRequest().body(new MessageResponse("Error: Test has not updated"));
    }

    @DeleteMapping("/{idTest}")
    @PreAuthorize("hasRole('LECTOR') or hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteTest(@PathVariable("idTest") Long idTest) {
        try {
            testService.deleteTest(idTest);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/{idTest}/tasks")
    @PreAuthorize("hasRole('STUDENT') or hasRole('LECTOR') or hasRole('ADMIN')")
    public ResponseEntity<List<Task>> getTask(@PathVariable("idTest") Long idTest) {
        List<Task> tasks = testService.getTaskByTest(idTest);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("/{idTest}/tasks/{idTask}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('LECTOR') or hasRole('ADMIN')")
    public ResponseEntity<Task> getTaskById(@PathVariable("idTask") Long idTask) {
        Optional<Task> task = testService.getTaskById(idTask);

        return task.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }
    @PostMapping("/{idTest}/tasks")
    @PreAuthorize("hasRole('LECTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> createTask(@PathVariable("idTest") Long idTest, @RequestBody Task task) {
        if (testService.taskExistsByTitle(task.getTitle()))
            return ResponseEntity.badRequest().body(new MessageResponse("Ошибка: задание с таким название уже существует!"));

        if (task.getTitle() == "")
            return ResponseEntity.badRequest().body(new MessageResponse("Ошибка: название задания не заполнено!"));

        if (testService.createTask(idTest, task))
            return ResponseEntity.ok(new MessageResponse("Task is creating"));

        return ResponseEntity.badRequest().body(new MessageResponse("Error: Task was not created"));
    }

    @PutMapping("/{idTest}/tasks/{idTask}")
    @PreAuthorize("hasRole('LECTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> updateTask(@PathVariable("idTask") Long idTask, @RequestBody Task task) {
        /*if (testService.existsByTitle(test.getTitle())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Ошибка: тест с таким название уже существует!"));
        }*/

        if (task.getTitle() == "") {
            return ResponseEntity.badRequest().body(new MessageResponse("Ошибка: название задания не заполнено!"));
        }

        if (testService.updateTask(idTask, task))
            return ResponseEntity.ok(new MessageResponse("Task has updated"));

        return ResponseEntity.badRequest().body(new MessageResponse("Error: Task has not updated"));
    }

    @DeleteMapping("/{idTest}/tasks/{idTask}")
    @PreAuthorize("hasRole('LECTOR') or hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteTask(@PathVariable("idTask") Long idTask) {
        try {
            testService.deleteTask(idTask);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/{idTest}/tasks/{idTask}/answerOptions")
    @PreAuthorize("hasRole('STUDENT') or hasRole('LECTOR') or hasRole('ADMIN')")
    public ResponseEntity<List<AnswerOptionResponse>> getAnswerOption(@PathVariable("idTask") Long idTask) {
        List<AnswerOptionResponse> answerOptions = testService.getAnswerOptionByTask(idTask);
        return new ResponseEntity<>(answerOptions, HttpStatus.OK);
    }

    @GetMapping("/{idTest}/allAnswerOptions")
    @PreAuthorize("hasRole('STUDENT') or hasRole('LECTOR') or hasRole('ADMIN')")
    public ResponseEntity<List<AnswerOptionResponse>> getAllAnswerOptionInTest(@PathVariable("idTest") Long idTest) {
        List<AnswerOptionResponse> answerOptions = testService.geyAllAnswerOptionInTest(idTest);
        return new ResponseEntity<>(answerOptions, HttpStatus.OK);
    }

    @GetMapping("/{idTest}/tasks/{idTask}/answerOptions/{idAnswerOption}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('LECTOR') or hasRole('ADMIN')")
    public ResponseEntity<AnswerOptionResponse> getAnswerOptionById(@PathVariable("idAnswerOption") Long idAnswerOption) {
        AnswerOptionResponse answerOption = testService.getAnswerOptionById(idAnswerOption);

        if (answerOption == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(answerOption, HttpStatus.OK);
    }

    @PostMapping("/{idTest}/tasks/{idTask}/answerOptions")
    @PreAuthorize("hasRole('LECTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> createAnswerOption(@PathVariable("idTask") Long idTask, @RequestBody AnswerOptionResponse answerOptionResponse) {
        if (testService.answerOptionExistsByTitle(answerOptionResponse.getTitle()))
            return ResponseEntity.badRequest().body(new MessageResponse("Ошибка: такой ответ уже существует!"));

        if (Objects.equals(answerOptionResponse.getTitle(), ""))
            return ResponseEntity.badRequest().body(new MessageResponse("Ошибка: ответ не заполнен!"));

        if (!testService.isOnlyOneAnswerCreate(idTask, answerOptionResponse)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Ошибка: в задании уже есть правильный ответ!"));
        }

        if (testService.createAnswerOption(idTask, answerOptionResponse))
            return ResponseEntity.ok(new MessageResponse("AnswerOption is creating"));

        return ResponseEntity.badRequest().body(new MessageResponse("Error: AnswerOption was not created"));
    }

    @PutMapping("/{idTest}/tasks/{idTask}/answerOptions/{idAnswerOption}")
    @PreAuthorize("hasRole('LECTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> updateAnswerOption(@RequestBody UpdateAnswer answers) {

        if (!testService.isOnlyOneAnswerUpdate(answers))
            return ResponseEntity.badRequest().body(new MessageResponse("Ошибка: задание содержит больше одного правильного ответа или не содержит ни одного правльного ответа!"));

        if (testService.updateAnswerOption(answers))
            return ResponseEntity.ok(new MessageResponse("AnswerOption has updated"));

        return ResponseEntity.badRequest().body(new MessageResponse("Error: AnswerOption has not updated"));
    }

    @DeleteMapping("/{idTest}/tasks/{idTask}/answerOptions/{idAnswerOption}")
    @PreAuthorize("hasRole('LECTOR') or hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteAnswerOption(@PathVariable("idAnswerOption") Long idAnswerOption) {
        try {
            testService.deleteAnswerOption(idAnswerOption);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{idTest}/checkTest")
    @PreAuthorize("hasRole('STUDENT') or hasRole('LECTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> checkTest(@PathVariable("idTest") Long idTest, @RequestBody UpdateAnswer updateAnswer) {
        Optional<User> user = userService.getAuthorizeUser();

        if (user.isPresent()) {
            List<String> roles = new ArrayList<>(roleService.getUserRoles(user.get()));
            if (!roles.isEmpty()) {
                if(!roles.contains("ROLE_ADMIN") & !roles.contains("ROLE_LECTOR") & roles.contains("ROLE_STUDENT")) {
                    if (testService.checkStudentAnswer(user.get(), idTest, updateAnswer))
                        return ResponseEntity.ok().body(new MessageResponse("Test was checked"));
                }
            }
        }

        return ResponseEntity.badRequest().body(new MessageResponse("Test has not checked"));
    }

    @GetMapping("/{idTest}/scoreStudentForTest")
    @PreAuthorize("hasRole('STUDENT') or hasRole('LECTOR') or hasRole('ADMIN')")
    public ResponseEntity<StudentTest> getScoreStudentForTest(@PathVariable("idTest") Long idTest) {
        Optional<User> user = userService.getAuthorizeUser();
        if (user.isPresent()) {
            Optional<StudentTest> studentTest = testService.getStudentScoreForTest(idTest, user.get());
            if (studentTest.isPresent()) {
                return new ResponseEntity<>(studentTest.get(), HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{idTest}/scoreForTest")
    @PreAuthorize("hasRole('LECTOR') or hasRole('ADMIN')")
    public ResponseEntity<List<StudentResponse>> getScores(@PathVariable("idTest") Long idTest) {
        List<StudentResponse> studentResponseList = testService.getScoresForTest(idTest);

        return new ResponseEntity<>(studentResponseList, HttpStatus.OK);
    }

    @PostMapping("/{idTest}/giveOneMoreChance")
    @PreAuthorize("hasRole('LECTOR') or hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> giveOneMoreChance(@PathVariable("idDiscipline") Long idDiscipline, @PathVariable("idTest") Long idTest, @RequestBody StudentResponse studentResponse) {
        try {
            testService.getMoreChance(studentResponse, idTest, idDiscipline);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
