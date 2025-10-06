package com.example.quizapp.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000)
    private String text;

    @Enumerated(EnumType.STRING)
    private QuestionType type;

    @Column(length = 300)
    private String correctAnswerText;

    @ManyToOne(fetch = FetchType.LAZY)
    private Quiz quiz;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Option> options = new ArrayList<>();

    public Question() {}
    public Question(String text, QuestionType type) {
        this.text = text;
        this.type = type;
    }

    public Long getId() { return id; }
    public String getText() { return text; }
    public QuestionType getType() { return type; }
    public String getCorrectAnswerText() { return correctAnswerText; }
    public void setCorrectAnswerText(String correctAnswerText) { this.correctAnswerText = correctAnswerText; }
    public Quiz getQuiz() { return quiz; }
    public void setQuiz(Quiz quiz) { this.quiz = quiz; }
    public List<Option> getOptions() { return options; }
    public void addOption(Option o) { o.setQuestion(this); options.add(o); }
}
