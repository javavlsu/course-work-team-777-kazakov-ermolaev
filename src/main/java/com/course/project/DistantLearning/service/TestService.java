package com.course.project.DistantLearning.service;

import com.course.project.DistantLearning.dto.request.UpdateAnswer;
import com.course.project.DistantLearning.dto.response.AnswerOptionResponse;
import com.course.project.DistantLearning.dto.response.StudentResponse;
import com.course.project.DistantLearning.models.*;
import com.course.project.DistantLearning.repository.*;
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

    @Autowired
    StudentTestRepository studentTestRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    ScoreRepository scoreRepository;

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
            _task.setType(task.getType());
            taskRepository.save(_task);
            return true;
        }
        else {
            return false;
        }
    }

    public void deleteTask(Long idTask) { taskRepository.deleteById(idTask); }



    public Boolean answerOptionExistsByTitle(String title) { return answerOptionRepository.existsByTitle(title); }

    public List<AnswerOptionResponse> geyAllAnswerOptionInTest(Long idTest) {
        List<AnswerOption> answerOptions = new ArrayList<>();
        List<AnswerOptionResponse> answerOptionResponses = new ArrayList<>();
        Optional<Test> test = testRepository.findById(idTest);

        if (test.isPresent()) {
            List<Task> tasks = taskRepository.findByTest(test.get());
            for (var task: tasks) {
                answerOptions.addAll(answerOptionRepository.findByTask(task));
            }
        }

        for (var answer: answerOptions) {
            var answerResponse = new AnswerOptionResponse();

            answerResponse.setId(answer.getId());
            answerResponse.setTitle(answer.getTitle());
            answerResponse.setIdTask(answer.getTask().getId());
            if (answer.getRight()) {
                answerResponse.setIsRight("true");
            } else {
                answerResponse.setIsRight("false");
            }
            answerOptionResponses.add(answerResponse);

        }
        return answerOptionResponses;
    }

    public List<AnswerOptionResponse> getAnswerOptionByTask(Long idTask) {
        List<AnswerOption> answerOptions = new ArrayList<>();
        List<AnswerOptionResponse> answerOptionResponses = new ArrayList<>();

        Optional<Task> task = taskRepository.findById(idTask);
        task.ifPresent(value -> answerOptions.addAll(answerOptionRepository.findByTask(value)));

        for(var answer: answerOptions) {
            var answerResponse = new AnswerOptionResponse();

            answerResponse.setId(answer.getId());
            answerResponse.setTitle(answer.getTitle());
            if (answer.getRight()) {
                answerResponse.setIsRight("true");
            } else {
                answerResponse.setIsRight("false");
            }
            answerOptionResponses.add(answerResponse);
        }

        return answerOptionResponses.stream().sorted(Comparator.comparingLong(AnswerOptionResponse::getId)).toList();
    }

    public AnswerOptionResponse getAnswerOptionById(Long idAnswerOption) {
        Optional<AnswerOption> answerOption = answerOptionRepository.findById(idAnswerOption);
        AnswerOptionResponse answerOptionResponse = new AnswerOptionResponse();
        if (answerOption.isPresent()) {
            answerOptionResponse.setId(answerOption.get().getId());
            answerOptionResponse.setTitle(answerOption.get().getTitle());
            if (answerOption.get().getRight()) {
                answerOptionResponse.setIsRight("true");
            } else {
                answerOptionResponse.setIsRight("false");
            }
        }
        return answerOptionResponse;
    }

    public boolean createAnswerOption(Long idTask, AnswerOptionResponse answerOptionResponse) {
        Optional<Task> task = taskRepository.findById(idTask);
        if (task.isPresent()) {
            AnswerOption answerOption = new AnswerOption();
            answerOption.setTitle(answerOptionResponse.getTitle());
            answerOption.setRight(Objects.equals(answerOptionResponse.getIsRight(), "true"));
            answerOption.setTask(task.get());
            answerOptionRepository.save(answerOption);
            return true;
        }
        return false;
    }

    public boolean isOnlyOneAnswerUpdate(UpdateAnswer updateAnswer) {
        Optional<AnswerOptionResponse> answerOptionResponse = updateAnswer.getAnswers().stream().findAny();
        if (answerOptionResponse.isPresent()) {
            Task task = answerOptionRepository.findById(answerOptionResponse.get().getId()).get().getTask();
            if (Objects.equals(task.getType(), "radio")) {
                var i = 0;
                for(var answer: updateAnswer.getAnswers()) {
                    if (Objects.equals(answer.getIsRight(), "true"))
                        i++;
                }
                return i < 2 & i > 0;
            }
            return true;
        }
        return false;
    }

    public boolean isOnlyOneAnswerCreate(Long idTask, AnswerOptionResponse answerOptionResponse) {
        Optional<Task> task = taskRepository.findById(idTask);
        if (task.isPresent()) {
            if (Objects.equals(task.get().getType(), "radio")) {
                if (Objects.equals(answerOptionResponse.getIsRight(), "true")) {
                    for (var answer: answerOptionRepository.findByTask(task.get())) {
                        if (answer.getRight())
                            return false;
                    }
                }
            }
            return true;
        }
        return true;
    }

    public boolean updateAnswerOption(UpdateAnswer updateAnswer) {
        List<AnswerOptionResponse> answerOptionResponses = updateAnswer.getAnswers();

        if (!answerOptionResponses.isEmpty()) {
            for (var answer: answerOptionResponses) {
                var answerOption = answerOptionRepository.findById(answer.getId());
                if(answerOption.isPresent()) {
                    var _answerOption = answerOption.get();
                    _answerOption.setTitle(answer.getTitle());
                    _answerOption.setRight(Objects.equals(answer.getIsRight(), "true"));
                    answerOptionRepository.save(_answerOption);
                }
            }
            return true;
        }
        else {
            return false;
        }
    }

    public void deleteAnswerOption(Long idAnswerOption) { answerOptionRepository.deleteById(idAnswerOption); }


    public boolean checkStudentAnswer(User user, Long idTest, UpdateAnswer updateAnswer) {
        try {
            List<AnswerOption> answerOptions = new ArrayList<>();
            List<Task> tasks = new ArrayList<>();
            testRepository.findById(idTest).ifPresent(value -> tasks.addAll(taskRepository.findByTest(value)));

            for (var answer: updateAnswer.getAnswers()) {
                answerOptionRepository.findById(answer.getId()).ifPresent(answerOptions::add);
            }

            var score = 0;
            for (var task: tasks) {
                for(var answer: answerOptions) {
                    if (answer.getTask().getId().equals(task.getId())) {
                        if (Objects.equals(task.getType(), "radio")) {
                            if (answer.getRight()) {
                                score++;
                                break;
                            }
                        }
                        else {
                            var i = 0;
                            if (answer.getRight()) i++;
                            else i--;

                            if (i < 0) i = 0;

                            score += i;
                        }
                    }
                }
            }

            Optional<Student> student = studentRepository.findByUser(user);
            Optional<Test> test = testRepository.findById(idTest);
            if (student.isPresent() && test.isPresent()) {
                Optional<Score> scoreEntity = scoreRepository.findByStudentAndDiscipline(student.get(), test.get().getDiscipline());
                Optional<StudentTest> studentTestData = studentTestRepository.findByStudentAndTest(student.get(), test.get());
                if (scoreEntity.isPresent()) {
                    Score _scoreEntity = scoreEntity.get();
                    if (studentTestData.isEmpty()) {
                        StudentTest studentTest = new StudentTest();
                        studentTest.setScoreTest(score);
                        studentTest.setStudent(student.get());
                        studentTest.setTest(test.get());
                        studentTest.setDiscipline(test.get().getDiscipline());
                        studentTest.setPassedDate(new Date());
                        studentTestRepository.save(studentTest);

                        _scoreEntity.setScore(scoreEntity.get().getScore() + studentTest.getScoreTest());
                    } else {
                        StudentTest studentTest = studentTestData.get();
                        studentTest.setScoreTest(score);
                        studentTest.setPassedDate(new Date());
                        studentTestRepository.save(studentTest);

                        _scoreEntity.setScore(scoreEntity.get().getScore() - (studentTestData.get().getScoreTest() - studentTest.getScoreTest()));
                    }
                    scoreRepository.save(_scoreEntity);
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Optional<StudentTest> getStudentScoreForTest(Long idTest, User user) {
        Optional<Student> student = studentRepository.findByUser(user);
        Optional<Test> test = testRepository.findById(idTest);
        if (student.isPresent() && test.isPresent()) {
            return studentTestRepository.findByStudentAndTest(student.get(), test.get());
        }

        return Optional.empty();
    }

    public List<StudentResponse> getScoresForTest(Long idTest) {
        List<StudentResponse> studentResponseList = new ArrayList<>();
        Optional<Test> test = testRepository.findById(idTest);
        if (test.isPresent()) {
            for (var student: studentRepository.findAll()) {
                Optional<StudentTest> studentTest = studentTestRepository.findByStudentAndTest(student, test.get());
                if (studentTest.isPresent()) {
                    StudentResponse studentResponse = new StudentResponse();
                    studentResponse.setId(student.getId());
                    studentResponse.setName(student.getUser().getFullName());
                    studentResponse.setGroupName(student.getGroup().getName());
                    studentResponse.setEmail(student.getUser().getEmail());
                    studentResponse.setScoreTest(studentTest.get().getScoreTest());
                    studentResponseList.add(studentResponse);
                }
            }
        }
        return studentResponseList;
    }

    public void getMoreChance(StudentResponse studentResponse, Long idTest, Long idDiscipline) {
        Optional<Student> student = studentRepository.findById(studentResponse.getId());
        Optional<Test> test = testRepository.findById(idTest);
        Optional<Discipline> discipline = disciplineService.getDisciplineById(idDiscipline);

        if (student.isPresent() & test.isPresent() & discipline.isPresent()) {
            Optional<Score> scoreData = scoreRepository.findByStudentAndDiscipline(student.get(), discipline.get());
            Optional<StudentTest> studentTest = studentTestRepository.findByStudentAndTest(student.get(), test.get());

            if (scoreData.isPresent() && studentTest.isPresent()) {
                Score score = scoreData.get();
                score.setScore(scoreData.get().getScore() - studentTest.get().getScoreTest());
                scoreRepository.save(score);
                studentTestRepository.deleteById(studentTest.get().getId());
            }
        }
    }
}
