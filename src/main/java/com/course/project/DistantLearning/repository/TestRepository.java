package com.course.project.DistantLearning.repository;

import com.course.project.DistantLearning.models.Discipline;
import com.course.project.DistantLearning.models.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {
    List<Test> findByDiscipline(Discipline discipline);

    Boolean existsByTitle(String title);
}
