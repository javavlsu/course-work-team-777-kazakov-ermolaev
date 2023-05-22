package com.course.project.DistantLearning.dto.request;

import com.course.project.DistantLearning.dto.response.LectorResponse;

import java.util.List;

public class CreateDisciplineRequest {

    private String title;
    private List<LectorResponse> lectorResponseList;

    public CreateDisciplineRequest() {
    }

    public CreateDisciplineRequest(String title, List<LectorResponse> lectorResponseList) {
        this.title = title;
        this.lectorResponseList = lectorResponseList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<LectorResponse> getLectorResponseList() {
        return lectorResponseList;
    }

    public void setLectorResponseList(List<LectorResponse> lectorResponseList) {
        this.lectorResponseList = lectorResponseList;
    }
}
