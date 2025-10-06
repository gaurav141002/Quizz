package com.example.quizapp.dto;

import java.util.List;

public class SubmitAnswersRequest {
    public List<AnswerItem> answers;

    public static class AnswerItem {
        public Long questionId;
        public List<Long> selectedOptionIds;
        public String textAnswer;
    }
}
