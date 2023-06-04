package com.course.project.DistantLearning.service;

import com.course.project.DistantLearning.dto.response.MessageResponse;
import com.course.project.DistantLearning.models.AnswerOption;
import com.course.project.DistantLearning.models.Discipline;
import com.course.project.DistantLearning.models.Student;
import com.course.project.DistantLearning.models.Test;
import com.course.project.DistantLearning.repository.AnswerOptionRepository;
import com.course.project.DistantLearning.repository.TaskRepository;
import com.course.project.DistantLearning.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TestService {
    @Autowired
    TestRepository testRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    DisciplineService disciplineService;

    @Autowired
    AnswerOptionRepository answerOptionRepository;

    public Boolean existsByTitle(String title) { return testRepository.existsByTitle(title); }

    public List<Test> getAllTest() { return testRepository.findAll(); }

    public List<Test> getTestByDiscipline (Long idDiscipline) {
        List<Test> tests = new ArrayList<>();
        Optional<Discipline> discipline = disciplineService.getDisciplineById(idDiscipline);
        discipline.ifPresent(value -> tests.addAll(testRepository.findByDiscipline(value)));

        return tests;

    }

    public Optional<Test> getTestById(Long idTest) {
        Optional<Test> test = testRepository.findById(idTest);
        if (test.isPresent()) {
            Test _test = test.get();
            if (_test.getStatus().equals("close") && _test.getDateStart().before(new Date())) {
                if (_test.getDeadline().after(new Date()))
                    _test.setStatus("open");
            }

            if (_test.getStatus().equals("open") && _test.getDeadline().before(new Date()))
                _test.setStatus("close");

            if (test.get().getStatus() != _test.getStatus())
                testRepository.save(_test);
        }
        return testRepository.findById(idTest);
    }

    public boolean createTest(Long idDiscipline, Test test) {
        Optional<Discipline> discipline = disciplineService.getDisciplineById(idDiscipline);
        if (discipline.isPresent()) {
            test.setDiscipline(discipline.get());
            test.setStatus("close");
            testRepository.save(test);
            return true;
        }
        return false;

    }

    public boolean updateTest(Long idTest, Test test) {
        Optional<Test> testData = testRepository.findById(idTest);

        if (testData.isPresent()) {
            Test _test = testData.get();
            _test.setTitle(test.getTitle());
            _test.setDateStart(test.getDateStart());
            _test.setDeadline(test.getDeadline());
            testRepository.save(_test);
            return true;
        }
        else {
            return false;
        }
    }

    public void deleteTest(Long idTest) { testRepository.deleteById(idTest); }
}
