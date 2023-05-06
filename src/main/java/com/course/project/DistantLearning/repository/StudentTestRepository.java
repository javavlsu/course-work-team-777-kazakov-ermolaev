package com.course.project.DistantLearning.repository;

import com.course.project.DistantLearning.models.StudentTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentTestRepository extends JpaRepository<StudentTest, Long> {
}
