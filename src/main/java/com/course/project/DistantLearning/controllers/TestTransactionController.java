package com.course.project.DistantLearning.controllers;

import com.course.project.DistantLearning.dto.response.MessageResponse;
import com.course.project.DistantLearning.service.TestTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/testTransactional")
public class TestTransactionController {
    @Autowired
    TestTransactionService testTransactionService;

    @GetMapping("/lab5")
    public ResponseEntity<?> tests() {
        return ResponseEntity.ok().body(new MessageResponse("fnajfnawjfnfwnf"));
    }

    @GetMapping
    @Transactional(propagation=Propagation.SUPPORTS)
    public void testMethod() {
        testTransactionService.InsertingGroupWithoutException();
        testTransactionService.InsertingUserWithException();
    }
}
