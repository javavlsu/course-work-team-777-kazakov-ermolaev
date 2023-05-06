package com.course.project.DistantLearning.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "test")
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "deadline")
    private Date deadline;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "discipline_id")
    private Discipline discipline;

    public Test() {}

    public Test(String title, Date deadline, String status, Discipline discipline) {
        this.title = title;
        this.deadline = deadline;
        this.status = status;
        this.discipline = discipline;
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

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }
}
