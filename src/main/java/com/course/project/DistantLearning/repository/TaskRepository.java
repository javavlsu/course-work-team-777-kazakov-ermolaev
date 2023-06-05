package com.course.project.DistantLearning.repository;

import com.course.project.DistantLearning.models.Task;
import com.course.project.DistantLearning.models.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByTest(Test test);

    Boolean existsByTitle(String title);
}
