package com.course.project.DistantLearning.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "student_has_test")
public class StudentTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "test_id")
    private Test test;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "discipline_id")
    private Discipline discipline;

    @Column(name = "scoretest")
    private float scoretest;

    @Column(name = "passedDate")
    private Date passedDate;

    public StudentTest() {}

    public StudentTest(Test test, Student student, Discipline discipline, float scoretest, Date passedDate) {
        this.test = test;
        this.student = student;
        this.discipline = discipline;
        this.scoretest = scoretest;
        this.passedDate = passedDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
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

    public float getScoretest() {
        return scoretest;
    }

    public void setScoretest(float scoretest) {
        this.scoretest = scoretest;
    }

    public Date getPassedDate() {
        return passedDate;
    }

    public void setPassedDate(Date passedDate) {
        this.passedDate = passedDate;
    }
}
