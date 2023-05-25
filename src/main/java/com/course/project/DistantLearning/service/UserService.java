package com.course.project.DistantLearning.service;

import com.course.project.DistantLearning.dto.response.LectorResponse;
import com.course.project.DistantLearning.dto.response.StudentResponse;
import com.course.project.DistantLearning.models.Lector;
import com.course.project.DistantLearning.models.Student;
import com.course.project.DistantLearning.models.User;
import com.course.project.DistantLearning.repository.LectorRepository;
import com.course.project.DistantLearning.repository.StudentRepository;
import com.course.project.DistantLearning.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LectorRepository lectorRepository;

    @Autowired
    private RoleService roleService;

    public String getCurrentUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return currentPrincipalName;
    }

    public void createUser(User user) {
        userRepository.save(user);

        List<String> roles = roleService.getUserRoles(user);

        if(roles.contains("ROLE_LECTOR") & !roles.contains("ROLE_ADMIN")) {
            Lector lector = new Lector(user);
            lectorRepository.save(lector);
        } else if(roles.contains("ROLE_STUDENT") & !roles.contains("ROLE_ADMIN") & !roles.contains("ROLE_LECTOR")) {
            Student student = new Student(user);
            studentRepository.save(student);
        }
    }

    public boolean existsByUsername(String username) { return userRepository.existsByUsername(username); }

    public boolean existsByEmail(String email) { return userRepository.existsByEmail(email); }

    public User getUserByID(Long id) {
        return userRepository.findById(id).get();
    }

    public Lector getLectorById(Long id) {
        return lectorRepository.findById(id).get();
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id).get();
    }

    public User getAuthorizeUser() {
        return userRepository.findByUsername(getCurrentUserName()).get();
    }

    public Student getStudent() {
        return studentRepository.findByUser(userRepository.findByUsername(getCurrentUserName()).get()).get();
    }

    public List<LectorResponse> getLectors() {
        var lectors = lectorRepository.findAll();
        List<LectorResponse> listLector = new ArrayList<>();

        if (!lectors.isEmpty()) {

            for(var lector: lectors) {
                var lectorResponse = new LectorResponse();
                lectorResponse.setId(lector.getId());
                lectorResponse.setName(lector.getUser().getFullName());
                lectorResponse.setEmail(lector.getUser().getEmail());

                listLector.add(lectorResponse);
            }
        }

        return listLector;
    }

    public List<StudentResponse> getStudents() {
        var students = studentRepository.findAll();
        List<StudentResponse> studentList = new ArrayList<>();

        if (!students.isEmpty()) {

            for(var student: students) {
                var studentResponse = new StudentResponse();
                studentResponse.setId(student.getId());
                studentResponse.setName(student.getUser().getFullName());
                studentResponse.setEmail(student.getUser().getEmail());
                try {
                    studentResponse.setGroupName(student.getGroup().getName());
                } catch (Exception e) {
                    studentResponse.setGroupName("Без группы");
                }

                studentList.add(studentResponse);
            }
        }
        return studentList;
    }

    public void deleteStudent(Long idStudent) {
        User user = userRepository.findById(studentRepository.findById(idStudent).get().getUser().getId()).get();
        studentRepository.deleteById(idStudent);
        userRepository.delete(user);
    }

    public void deleteLector(Long idLector) {
        User user = userRepository.findById(lectorRepository.findById(idLector).get().getUser().getId()).get();
        lectorRepository.deleteById(idLector);
        userRepository.delete(user);
    }

    public Lector getAuthorizeLector() {
        return lectorRepository.findByUser(userRepository.findByUsername(getCurrentUserName()).get()).get();
    }
}
