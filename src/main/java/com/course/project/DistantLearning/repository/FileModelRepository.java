package com.course.project.DistantLearning.repository;

import com.course.project.DistantLearning.models.FileModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileModelRepository extends JpaRepository<FileModel, Long> {
}
