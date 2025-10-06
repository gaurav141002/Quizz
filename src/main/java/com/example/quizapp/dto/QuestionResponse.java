package com.example.quizapp.dto;

import java.util.List;

public class QuestionResponse {
    public Long id;
    public String text;
    public String type;
    public List<OptionResponse> options;

    public static class OptionResponse {
        public Long id;
        public String text;
    }
}
