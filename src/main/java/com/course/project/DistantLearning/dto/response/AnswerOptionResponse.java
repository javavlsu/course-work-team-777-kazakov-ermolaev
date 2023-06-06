package com.course.project.DistantLearning.dto.response;

public class AnswerOptionResponse {
    private Long id;
    private String title;
    private String isRight;
    private Long idTask;

    public AnswerOptionResponse() { }

    public AnswerOptionResponse(Long id, String title, String isRight, Long idTask) {
        this.id = id;
        this.title = title;
        this.isRight = isRight;
        this.idTask = idTask;
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

    public Long getIdTask() {
        return idTask;
    }

    public void setIdTask(Long idTask) {
        this.idTask = idTask;
    }
}
