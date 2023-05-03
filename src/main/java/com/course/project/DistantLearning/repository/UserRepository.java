package com.course.project.DistantLearning.repository;

import com.course.project.DistantLearning.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    List<User> findByUsernameContaining(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
