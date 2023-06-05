package com.course.project.DistantLearning.dto.request;

public class CreateAnswerOption {
    private String title;
    private String isRight;

    public CreateAnswerOption(String title, String isRight) {
        this.title = title;
        this.isRight = isRight;
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
