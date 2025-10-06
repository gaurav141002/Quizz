package com.example.quizapp.dto;

import java.util.List;

public class CreateQuestionRequest {
    public String text;
    public String type;
    public List<OptionDTO> options;
    public String correctAnswerText;

    public static class OptionDTO {
        public String text;
        public boolean isCorrect;
    }
}
