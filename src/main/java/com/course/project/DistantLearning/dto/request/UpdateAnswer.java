package com.course.project.DistantLearning.dto.request;

import com.course.project.DistantLearning.dto.response.AnswerOptionResponse;

import java.util.List;

public class UpdateAnswer {
    List<AnswerOptionResponse> answers;

    public UpdateAnswer() {
    }

    public UpdateAnswer(List<AnswerOptionResponse> answers) {
        this.answers = answers;
    }

    public List<AnswerOptionResponse> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerOptionResponse> answers) {
        this.answers = answers;
    }
}
