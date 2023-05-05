package com.course.project.DistantLearning.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "discipline")
public class Discipline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "discipline_lector",
            joinColumns = @JoinColumn(name = "discipline_id"),
            inverseJoinColumns = @JoinColumn(name = "lector_id"))
    private List<Lector> lectorId;

    public Discipline() {}

    public Discipline(String title, List<Lector> lectorId) {
        this.title = title;
        this.lectorId = lectorId;
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

    public List<Lector> getLectorId() {
        return lectorId;
    }

    public void setLectorId(List<Lector> lectorId) {
        this.lectorId = lectorId;
    }
}
