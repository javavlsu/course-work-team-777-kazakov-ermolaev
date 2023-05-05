package com.course.project.DistantLearning.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "labwork")
public class LabWork {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "deadline")
    private Date deadline;

    @Column(name = "manual")
    private String manual;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "discipline_id")
    private Discipline disciplineId;

    public LabWork() {}

    public LabWork(String title, Date deadline, String manual, String status, Discipline disciplineId) {
        this.title = title;
        this.deadline = deadline;
        this.manual = manual;
        this.status = status;
        this.disciplineId = disciplineId;
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

    public String getManual() {
        return manual;
    }

    public void setManual(String manual) {
        this.manual = manual;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Discipline getDisciplineId() {
        return disciplineId;
    }

    public void setDisciplineId(Discipline disciplineId) {
        this.disciplineId = disciplineId;
    }
}
