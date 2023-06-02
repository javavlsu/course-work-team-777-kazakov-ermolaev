package com.course.project.DistantLearning.dto.response;

import com.course.project.DistantLearning.models.Student;

import java.util.List;

public class UpdateGroupResponse {
    public Long id;

    public String name;

    public List<StudentResponse> studentResponseList;

    public UpdateGroupResponse() {}

    public UpdateGroupResponse(Long id, String name, List<StudentResponse> studentResponseList) {
        this.id = id;
        this.name = name;
        this.studentResponseList = studentResponseList;
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

    public List<StudentResponse> getStudentResponseList() {
        return studentResponseList;
    }

    public void setStudentResponseList(List<StudentResponse> studentResponseList) {
        this.studentResponseList = studentResponseList;
    }
}
