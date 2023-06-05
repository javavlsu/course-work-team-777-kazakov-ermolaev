package com.course.project.DistantLearning.service;

import com.course.project.DistantLearning.dto.request.CreateAnswerOption;
import com.course.project.DistantLearning.models.AnswerOption;
import com.course.project.DistantLearning.models.Discipline;
import com.course.project.DistantLearning.models.Task;
import com.course.project.DistantLearning.models.Test;
import com.course.project.DistantLearning.repository.AnswerOptionRepository;
import com.course.project.DistantLearning.repository.TaskRepository;
import com.course.project.DistantLearning.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

    public Boolean testExistsByTitle(String title) { return testRepository.existsByTitle(title); }

    public List<Test> getTestByDiscipline (Long idDiscipline) {
        List<Test> tests = new ArrayList<>();
        Optional<Discipline> discipline = disciplineService.getDisciplineById(idDiscipline);
        discipline.ifPresent(value -> tests.addAll(testRepository.findByDiscipline(value)));

        return tests.stream().sorted(Comparator.comparingLong(Test::getId)).toList();

    }

    public Optional<Test> getTestById(Long idTest) {
        Optional<Test> test = testRepository.findById(idTest);
        if (test.isPresent()) {
            Test _test = test.get();
            if (_test.getStatus().equals("close") && _test.getDateStart().before(new Date())) {
                if (_test.getDeadline().after(new Date())) {
                    _test.setStatus("open");
                    testRepository.save(_test);
                }
            }

            if (_test.getStatus().equals("open") && _test.getDeadline().before(new Date())) {
                _test.setStatus("close");
                testRepository.save(_test);
            }
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

    public Boolean taskExistsByTitle(String title) { return taskRepository.existsByTitle(title); }



    public List<Task> getTaskByTest(Long idTest) {
        List<Task> tasks = new ArrayList<>();
        Optional<Test> test = testRepository.findById(idTest);
        test.ifPresent(value -> tasks.addAll(taskRepository.findByTest(value)));
        return tasks.stream().sorted(Comparator.comparingLong(Task::getId)).toList();
    }

    public Optional<Task> getTaskById(Long idTask) { return taskRepository.findById(idTask); }

    public boolean createTask(Long idTest, Task task) {
        Optional<Test> test = testRepository.findById(idTest);
        if (test.isPresent()) {
            task.setTest(test.get());
            taskRepository.save(task);
            return true;
        }
        return false;
    }

    public boolean updateTask(Long idTask, Task task) {
        Optional<Task> taskData = taskRepository.findById(idTask);

        if (taskData.isPresent()) {
            Task _task = taskData.get();
            _task.setTitle(task.getTitle());
            taskRepository.save(_task);
            return true;
        }
        else {
            return false;
        }
    }

    public void deleteTask(Long idTask) { taskRepository.deleteById(idTask); }



    public Boolean answerOptionExistsByTitle(String title) { return answerOptionRepository.existsByTitle(title); }

    public List<AnswerOption> getAnswerOptionByTask(Long idTask) {
        List<AnswerOption> answerOptions = new ArrayList<>();
        Optional<Task> task = taskRepository.findById(idTask);
        task.ifPresent(value -> answerOptions.addAll(answerOptionRepository.findByTask(value)));
        return answerOptions.stream().sorted(Comparator.comparingLong(AnswerOption::getId)).toList();
    }

    public Optional<AnswerOption> getAnswerOptionById(Long idAnswerOption) { return answerOptionRepository.findById(idAnswerOption); }

    public boolean createAnswerOption(Long idTask, CreateAnswerOption createAnswerOption) {
        Optional<Task> task = taskRepository.findById(idTask);
        if (task.isPresent()) {
            AnswerOption answerOption = new AnswerOption();
            answerOption.setTitle(createAnswerOption.getTitle());
            answerOption.setRight(Objects.equals(createAnswerOption.getIsRight(), "true"));
            answerOption.setTask(task.get());
            answerOptionRepository.save(answerOption);
            return true;
        }
        return false;
    }

    public boolean updateAnswerOption(Long idAnswerOption, AnswerOption answerOption) {
        Optional<AnswerOption> answerOptionData = answerOptionRepository.findById(idAnswerOption);

        if (answerOptionData.isPresent()) {
            AnswerOption _answerOption = answerOptionData.get();
            _answerOption.setTitle(answerOption.getTitle());
            _answerOption.setRight(answerOption.getRight());
            answerOptionRepository.save(_answerOption);
            return true;
        }
        else {
            return false;
        }
    }

    public void deleteAnswerOption(Long idAnswerOption) { answerOptionRepository.deleteById(idAnswerOption); }
}
