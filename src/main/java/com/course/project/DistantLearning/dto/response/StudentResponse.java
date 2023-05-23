package com.course.project.DistantLearning.dto.response;

public class StudentResponse {
    private Long id;
    private String name;
    private String groupName;

    public StudentResponse() {}

    public StudentResponse(Long id, String name, String groupName) {
        this.id = id;
        this.name = name;
        this.groupName = groupName;
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

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
