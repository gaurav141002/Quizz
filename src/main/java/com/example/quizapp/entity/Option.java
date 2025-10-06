package com.example.quizapp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "quiz_option")
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean isCorrect;

    private String text;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;


    public Option() {}
    public Option(String text, boolean isCorrect) { this.text = text; this.isCorrect = isCorrect; }

    public Long getId() { return id; }
    public String getText() { return text; }
    public boolean isCorrect() { return isCorrect; }
    public void setQuestion(Question question) { this.question = question; }
}
