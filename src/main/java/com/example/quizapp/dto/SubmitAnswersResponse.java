package com.example.quizapp.dto;

public class SubmitAnswersResponse {
    public int score;
    public int total;
    public SubmitAnswersResponse() {}
    public SubmitAnswersResponse(int score, int total) { this.score = score; this.total = total; }
}
