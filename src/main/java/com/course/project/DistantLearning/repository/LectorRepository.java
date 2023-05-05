package com.course.project.DistantLearning.repository;

import com.course.project.DistantLearning.models.Lector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectorRepository extends JpaRepository<Lector, Long> {
}
