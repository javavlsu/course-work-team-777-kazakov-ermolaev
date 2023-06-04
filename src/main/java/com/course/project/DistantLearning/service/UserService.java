package com.course.project.DistantLearning.service;

import com.course.project.DistantLearning.dto.response.LectorResponse;
import com.course.project.DistantLearning.dto.response.MessageResponse;
import com.course.project.DistantLearning.dto.response.StudentResponse;
import com.course.project.DistantLearning.models.*;
import com.course.project.DistantLearning.repository.LectorRepository;
import com.course.project.DistantLearning.repository.StudentRepository;
import com.course.project.DistantLearning.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    StudentRepository studentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    LectorRepository lectorRepository;

    @Autowired
    RoleService roleService;

    @Autowired
    GroupService groupService;

    public String getCurrentUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
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

    public Optional<User> getUserByID(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getAuthorizeUser() {
        return userRepository.findByUsername(getCurrentUserName());
    }

    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    public Student getStudent() {
        return studentRepository.findByUser(userRepository.findByUsername(getCurrentUserName()).get()).get();
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
        return studentList.stream().sorted(Comparator.comparingLong(StudentResponse::getId)).toList();
    }

    public void deleteStudent(Long idStudent) {
        User user = userRepository.findById(studentRepository.findById(idStudent).get().getUser().getId()).get();
        studentRepository.deleteById(idStudent);
        userRepository.delete(user);
    }

    public MessageResponse updateStudent(Long idStudent, StudentResponse studentResponse) {
        Optional<Student> studentData = studentRepository.findById(idStudent);

        if (studentData.isPresent()) {
            Student student = studentData.get();
            User user = studentData.get().getUser();

            user.setFullName(studentResponse.getName());
            user.setEmail(studentResponse.getEmail());

            userRepository.save(user);
            studentRepository.save(student);
            return new MessageResponse("Update student has finished successfully");
        } else {
            return new MessageResponse("Error! Update student has stopped");
        }
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

        return listLector.stream().sorted(Comparator.comparingLong(LectorResponse::getId)).toList();
    }

    public List<Lector> getAllLectors() { return lectorRepository.findAll(); }

    public Optional<Lector> getLectorById(Long id) {
        return lectorRepository.findById(id);
    }

    public Lector getAuthorizeLector() {
        return lectorRepository.findByUser(userRepository.findByUsername(getCurrentUserName()).get()).get();
    }

    public void deleteLector(Long idLector) {
        User user = userRepository.findById(lectorRepository.findById(idLector).get().getUser().getId()).get();
        lectorRepository.deleteById(idLector);
        userRepository.delete(user);
    }

    public MessageResponse updateLector(Long idLector, LectorResponse lectorResponse) {
        Optional<Lector> lectorData = lectorRepository.findById(idLector);

        if (lectorData.isPresent()) {
            Lector lector = lectorData.get();
            User user = lectorData.get().getUser();

            user.setFullName(lectorResponse.getName());
            user.setEmail(lectorResponse.getEmail());

            userRepository.save(user);
            lectorRepository.save(lector);
            return new MessageResponse("Update lector has finished successfully");
        } else {
            return new MessageResponse("Error! Update lector has stopped");
        }
    }

    public List<StudentResponse> getStudentsOfGroup(Long idGroup) {
        List<Student> studentList = groupService.getGroupById(idGroup).get().getStudentList();

        List<StudentResponse> listStudent = new ArrayList<>();

        if (!studentList.isEmpty()) {

            for(var student: studentList) {
                var studentResponse = new StudentResponse();
                studentResponse.setId(student.getId());
                studentResponse.setName(student.getUser().getFullName());
                studentResponse.setEmail(student.getUser().getEmail());
                studentResponse.setGroupName(student.getGroup().getName());

                listStudent.add(studentResponse);
            }
        }

        return listStudent.stream().sorted(Comparator.comparingLong(StudentResponse::getId)).toList();
    }

    public List<StudentResponse> getStudentWithoutGroup() {
        List<StudentResponse> studentResponseList = new ArrayList<>();
        for(var student: studentRepository.findAll()) {
            if (student.getGroup() == null) {
                var studentResp = new StudentResponse();
                studentResp.setId(student.getId());
                studentResp.setName(student.getUser().getFullName());
                studentResp.setEmail(student.getUser().getEmail());
                studentResp.setGroupName("Нет группы");
                studentResponseList.add(studentResp);
            }
        }
        return studentResponseList.stream().sorted(Comparator.comparingLong(StudentResponse::getId)).toList();
    }
}
