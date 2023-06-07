package com.course.project.DistantLearning.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "files")
public class FileModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "path")
    private String path;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "student_file",
            joinColumns = @JoinColumn(name = "file_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id"))
    private List<Student> student;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "student_file",
            joinColumns = @JoinColumn(name = "file_id"),
            inverseJoinColumns = @JoinColumn(name = "labwork_id"))
    private List<LabWork> labWork;

    public FileModel() {}

    public FileModel(Long id, String name, String path, List<Student> student, List<LabWork> labWork) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.student = student;
        this.labWork = labWork;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setStudent(List<Student> student) {
        this.student = student;
    }

    public void setLabWork(List<LabWork> labWork) {
        this.labWork = labWork;
    }
}
