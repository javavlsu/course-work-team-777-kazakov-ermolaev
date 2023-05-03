package com.course.project.DistantLearning.repository;

import com.course.project.DistantLearning.models.ERole;
import com.course.project.DistantLearning.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
