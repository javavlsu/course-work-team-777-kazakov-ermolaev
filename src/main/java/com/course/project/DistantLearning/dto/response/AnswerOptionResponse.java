package com.course.project.DistantLearning.dto.response;

public class AnswerOptionResponse {
    private Long id;
    private String title;
    private String isRight;

    public AnswerOptionResponse() { }
    public AnswerOptionResponse(Long id, String title, String isRight) {
        this.id = id;
        this.title = title;
        this.isRight = isRight;
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

    public String getIsRight() {
        return isRight;
    }

    public void setIsRight(String isRight) {
        this.isRight = isRight;
    }
}
