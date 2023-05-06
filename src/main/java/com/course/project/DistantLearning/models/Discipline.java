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
    private List<Lector> lectorList;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "discipline_group",
            joinColumns = @JoinColumn(name = "discipline_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id"))
    private List<Group> groupList;

    public Discipline() {}

    public Discipline(String title, List<Lector> lectorList) {
        this.title = title;
        this.lectorList = lectorList;
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

    public List<Lector> getLector() {
        return lectorList;
    }

    public void setLector(List<Lector> lectorList) {
        this.lectorList = lectorList;
    }

    public List<Group> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<Group> groupList) {
        this.groupList = groupList;
    }
}
