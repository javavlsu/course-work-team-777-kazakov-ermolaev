package com.course.project.DistantLearning.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "student_has_labwork")
public class StudentLabWork {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "labwork_id")
    private LabWork labwork;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "discipline_id")
    private Discipline discipline;

    @ManyToOne
    @JoinColumn(name = "lector_id")
    private Lector lector;

    @Column(name = "scorelab")
    private float scoreLab;

    @Column(name = "passedDate")
    private Date passedDate;

    public StudentLabWork() {}

    public StudentLabWork(LabWork labwork, Student student, Discipline discipline, Lector lector, float scoreLab, Date passedDate) {
        this.labwork = labwork;
        this.student = student;
        this.discipline = discipline;
        this.lector = lector;
        this.scoreLab = scoreLab;
        this.passedDate = passedDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LabWork getLabwork() {
        return labwork;
    }

    public void setLabwork(LabWork labwork) {
        this.labwork = labwork;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }

    public Lector getLector() {
        return lector;
    }

    public void setLector(Lector lector) {
        this.lector = lector;
    }

    public float getScoreLab() {
        return scoreLab;
    }

    public void setScoreLab(float scoreLab) {
        this.scoreLab = scoreLab;
    }

    public Date getPassedDate() {
        return passedDate;
    }

    public void setPassedDate(Date passedDate) {
        this.passedDate = passedDate;
    }
}
