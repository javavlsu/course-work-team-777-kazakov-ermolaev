package com.course.project.DistantLearning.repository;

import com.course.project.DistantLearning.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentLabWorkRepository extends JpaRepository<StudentLabWork, Long> {
    Optional<StudentLabWork> findByStudentAndLabwork(Student student, LabWork labwork);
}
