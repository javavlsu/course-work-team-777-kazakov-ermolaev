package com.course.project.DistantLearning.dto.request;

import com.course.project.DistantLearning.dto.response.LectorResponse;
import com.course.project.DistantLearning.models.Group;

import java.util.List;

public class CreateOrUpdateDisciplineRequest {

    private String title;
    private List<LectorResponse> lectorResponseList;
    private List<Group> groupList;

    public CreateOrUpdateDisciplineRequest() {
    }

    public CreateOrUpdateDisciplineRequest(String title, List<LectorResponse> lectorResponseList, List<Group> groupList) {
        this.title = title;
        this.lectorResponseList = lectorResponseList;
        this.groupList = groupList;
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

    public List<Group> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<Group> groupList) {
        this.groupList = groupList;
    }
}
