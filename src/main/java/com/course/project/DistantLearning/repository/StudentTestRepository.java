package com.course.project.DistantLearning.repository;

import com.course.project.DistantLearning.models.Student;
import com.course.project.DistantLearning.models.StudentTest;
import com.course.project.DistantLearning.models.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentTestRepository extends JpaRepository<StudentTest, Long> {
    Optional<StudentTest> findByStudentAndTest(Student student, Test test);

    Optional<StudentTest> findByStudent (Student student);
}
