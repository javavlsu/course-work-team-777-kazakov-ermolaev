package com.course.project.DistantLearning.service;

import com.course.project.DistantLearning.dto.response.StudentResponse;
import com.course.project.DistantLearning.models.*;
import com.course.project.DistantLearning.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class LabWorkService {
    @Autowired
    LabWorkRepository labWorkRepository;

    @Autowired
    DisciplineService disciplineService;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    TestTransactionService testTransactionService;

    @Autowired
    StudentLabWorkRepository studentLabWorkRepository;

    @Autowired
    DisciplineRepository disciplineRepository;

    @Autowired
    LectorRepository lectorRepository;

    @Autowired
    UserService userService;

    @Autowired
    ScoreRepository scoreRepository;

    public Boolean existsByTitle(String title) { return labWorkRepository.existsByTitle(title); }

    public List<LabWork> getLabWorkByDiscipline (Long idDiscipline) {
        List<LabWork> labWorks = new ArrayList<>();
        Optional<Discipline> discipline = disciplineService.getDisciplineById(idDiscipline);
        discipline.ifPresent(value -> labWorks.addAll(labWorkRepository.findByDiscipline(value)));

        return labWorks;

    }

    public Optional<LabWork> getLabWorkById(Long idLabWork) {
        return labWorkRepository.findById(idLabWork);
    }

    public boolean createLabWork(Long idDiscipline, LabWork labWork) {
        Optional<Discipline> discipline = disciplineService.getDisciplineById(idDiscipline);
        if (discipline.isPresent()) {
            labWork.setDiscipline(discipline.get());
            labWork.setStatus("None");
            labWorkRepository.save(labWork);
            return true;
        }
        return false;

    }

    public boolean updateLabWork(Long idLabWork, LabWork labWork) {
        Optional<LabWork> labWorkData = labWorkRepository.findById(idLabWork);

        if (labWorkData.isPresent()) {
            LabWork _labWork = labWorkData.get();
            _labWork.setTitle(labWork.getTitle());
            _labWork.setManual(labWork.getManual());
            _labWork.setDeadline(labWork.getDeadline());
            _labWork.setStatus("None");
            labWorkRepository.save(_labWork);



            return true;
        }
        else {
            return false;
        }
    }

    public void deleteLabWork(Long idLabWork) { labWorkRepository.deleteById(idLabWork); }


    @Transactional(propagation = Propagation.SUPPORTS)
    public void Test1() {
        testTransactionService.InsertingGroupWithoutException();
        testTransactionService.InsertingUserWithException();
    }

    public List<StudentResponse> getScoresForLabWork(Long idLabWork) {
        List<StudentResponse> studentResponseList = new ArrayList<>();
        Optional<LabWork> labWork = labWorkRepository.findById(idLabWork);
        if (labWork.isPresent()) {
            for (var student: studentRepository.findAll()) {
                Optional<StudentLabWork> studentLabWork = studentLabWorkRepository.findByStudentAndLabwork(student, labWork.get());
                if (studentLabWork.isPresent()) {
                    StudentResponse studentResponse = new StudentResponse();
                    studentResponse.setId(student.getId());
                    studentResponse.setName(student.getUser().getFullName());
                    studentResponse.setGroupName(student.getGroup().getName());
                    studentResponse.setEmail(student.getUser().getEmail());
                    studentResponse.setScoreLab(studentLabWork.get().getScoreLab());
                    studentResponseList.add(studentResponse);
                }
            }
        }
        return studentResponseList;
    }

    public Boolean createScoreLabWork(Long idDiscipline, Long IdLabWork, User user) {
        try {
            Optional<Student> student = studentRepository.findByUser(user);
            Optional<LabWork> labWork = labWorkRepository.findById(IdLabWork);
            Optional<Discipline> discipline = disciplineRepository.findById(idDiscipline);
            if (student.isPresent() & labWork.isPresent() & discipline.isPresent()) {
                StudentLabWork studentLabWork = new StudentLabWork();
                studentLabWork.setPassedDate(new Date());
                studentLabWork.setStudent(student.get());
                studentLabWork.setLabwork(labWork.get());
                studentLabWork.setDiscipline(discipline.get());
                studentLabWorkRepository.save(studentLabWork);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean updateScoreLabWork(Long idLabWork, Long idStudent, User user, float score) {
        Optional<Student> student = studentRepository.findById(idStudent);
        Optional<LabWork> labWork = labWorkRepository.findById(idLabWork);

        if (student.isPresent() & labWork.isPresent()) {
            var studentLabWork = studentLabWorkRepository.findByStudentAndLabwork(student.get(), labWork.get());
            Optional<Score> scoreEntity = scoreRepository.findByStudentAndDiscipline(student.get(), labWork.get().getDiscipline());
            if (scoreEntity.isPresent()) {
                Score _score = scoreEntity.get();
                if (studentLabWork.isPresent()) {
                    StudentLabWork studentLabWork1 = studentLabWork.get();
                    studentLabWork1.setScoreLab(score);
                    studentLabWork1.setLector(lectorRepository.findByUser(userService.getAuthorizeUser().get()).get());
                    studentLabWorkRepository.save(studentLabWork1);
                    _score.setScore(scoreEntity.get().getScore() - (studentLabWork.get().getScoreLab() - score));
                    scoreRepository.save(_score);
                    return true;
                }
            }
        }
        return false;
    }
}
