package com.course.project.DistantLearning.repository;

import com.course.project.DistantLearning.models.AnswerOption;
import com.course.project.DistantLearning.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerOptionRepository extends JpaRepository<AnswerOption, Long> {
    List<AnswerOption> findByTask(Task task);

    Boolean existsByTitle(String title);
}
