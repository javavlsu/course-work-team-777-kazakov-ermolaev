package com.course.project.DistantLearning.service;

import com.course.project.DistantLearning.dto.response.StudentResponse;
import com.course.project.DistantLearning.models.Discipline;
import com.course.project.DistantLearning.models.LabWork;
import com.course.project.DistantLearning.models.StudentLabWork;
import com.course.project.DistantLearning.repository.LabWorkRepository;
import com.course.project.DistantLearning.repository.StudentLabWorkRepository;
import com.course.project.DistantLearning.repository.StudentRepository;
import com.course.project.DistantLearning.repository.StudentTestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
}
