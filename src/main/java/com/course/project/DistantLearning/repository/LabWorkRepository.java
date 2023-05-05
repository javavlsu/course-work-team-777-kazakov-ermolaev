package com.course.project.DistantLearning.repository;

import com.course.project.DistantLearning.models.LabWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabWorkRepository extends JpaRepository<LabWork, Long> {
}
