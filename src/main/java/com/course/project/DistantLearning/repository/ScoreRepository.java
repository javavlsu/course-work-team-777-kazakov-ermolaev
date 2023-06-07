package com.course.project.DistantLearning.repository;

import com.course.project.DistantLearning.models.Discipline;
import com.course.project.DistantLearning.models.Score;
import com.course.project.DistantLearning.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {
    List<Score> findByDiscipline(Discipline discipline);

    List<Score> findByStudent(Student student);

    Optional<Score> findByStudentAndDiscipline(Student student, Discipline discipline);
}
