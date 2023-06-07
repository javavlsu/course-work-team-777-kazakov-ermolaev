package com.course.project.DistantLearning.service;

import com.course.project.DistantLearning.models.Group;
import com.course.project.DistantLearning.models.Student;
import com.course.project.DistantLearning.models.User;
import com.course.project.DistantLearning.repository.GroupRepository;
import com.course.project.DistantLearning.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;

@Service
public class TestTransactionService {

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    UserRepository userRepository;

    @Transactional(propagation = Propagation.REQUIRED, noRollbackFor = { RuntimeException.class })
    public void InsertingUserWithException() throws RuntimeException {
        User user = new User();
        user.setUsername("dadadadddd");
        user.setFullName("hruhru");
        user.setEmail("hruhru@gmail.com");
        user.setPassword("123456");
        user.setBirthdate(LocalDate.now());
        userRepository.save(user);
        User user1 = new User();
        user1.setFullName("hruhru1234");
        user1.setEmail("hruhru1234@gmail.com");
        user1.setPassword("123456");
        user1.setBirthdate(LocalDate.now());
        userRepository.save(user1);


    }
    @Transactional(propagation = Propagation.REQUIRES_NEW, noRollbackFor = { RuntimeException.class })
    public void InsertingGroupWithoutException() throws RuntimeException {
        Group group = new Group();
        group.setName("fafsafa");
        group.setStudentList(new ArrayList<Student>());
        groupRepository.save(group);
        Group group1 = new Group();
        group1.setName("fafsaflkfsfldsffsa");
        group1.setStudentList(new ArrayList<Student>());
        groupRepository.save(group1);
    }
}
