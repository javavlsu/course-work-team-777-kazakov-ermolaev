package com.course.project.DistantLearning.repository;

import com.course.project.DistantLearning.models.Student;
import com.course.project.DistantLearning.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUser (User user);
}
