package com.course.project.DistantLearning.models;

import jakarta.persistence.*;

@Entity
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @ManyToOne
    @JoinColumn(name = "test_id")
    private Test test;

    public Task() {}

    public Task(String title, Test test) {
        this.title = title;
        this.test = test;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test testId) {
        this.test = test;
    }
}
