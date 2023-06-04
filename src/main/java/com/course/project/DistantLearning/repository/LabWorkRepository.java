package com.course.project.DistantLearning.repository;

import com.course.project.DistantLearning.models.Discipline;
import com.course.project.DistantLearning.models.LabWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabWorkRepository extends JpaRepository<LabWork, Long> {
    List<LabWork> findByDiscipline(Discipline discipline);

    Boolean existsByTitle(String title);
}
