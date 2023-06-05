package com.course.project.DistantLearning.models;

import jakarta.persistence.*;

@Entity
@Table(name = "answeroption")
public class AnswerOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "isRight")
    private Boolean isRight;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    public AnswerOption() {}

    public AnswerOption(String title, Boolean isRight, Task task) {
        this.title = title;
        this.isRight = isRight;
        this.task = task;
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

    public Boolean getRight() {
        return isRight;
    }

    public void setRight(Boolean right) {
        isRight = right;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
