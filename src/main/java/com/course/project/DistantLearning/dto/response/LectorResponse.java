package com.course.project.DistantLearning.dto.response;

import com.course.project.DistantLearning.models.Discipline;

import java.util.List;

public class LectorResponse {

    private Long id;
    private String name;
    private String email;
    private List<Discipline> disciplineList;

    public LectorResponse() {

    }

    public LectorResponse(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Discipline> getDisciplineList() {
        return disciplineList;
    }

    public void setDisciplineList(List<Discipline> disciplineList) {
        this.disciplineList = disciplineList;
    }
}
