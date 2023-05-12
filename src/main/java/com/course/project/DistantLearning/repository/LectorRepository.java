package com.course.project.DistantLearning.repository;

import com.course.project.DistantLearning.models.Lector;
import com.course.project.DistantLearning.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LectorRepository extends JpaRepository<Lector, Long> {
    Optional<Lector> findByUser (User user);
}
