package com.course.project.DistantLearning.repository;

import com.course.project.DistantLearning.models.StudentLabWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentLabWorkRepository extends JpaRepository<StudentLabWork, Long> {
}
