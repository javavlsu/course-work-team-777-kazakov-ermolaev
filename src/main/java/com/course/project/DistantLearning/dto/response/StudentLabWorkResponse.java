package com.course.project.DistantLearning.dto.response;

import com.course.project.DistantLearning.models.User;

public class StudentLabWorkResponse {
    private Long idStudent;
    private float score;
    private User user;

    public StudentLabWorkResponse() {}

    public StudentLabWorkResponse(Long idStudent, float score, User user) {
        this.idStudent = idStudent;
        this.score = score;
        this.user = user;
    }

    public Long getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(Long idStudent) {
        this.idStudent = idStudent;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
