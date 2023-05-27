package com.course.project.DistantLearning.repository;

import com.course.project.DistantLearning.models.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    Optional<Group> findByName(String name);

    Boolean existsByName(String name);
}
