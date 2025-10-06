package com.example.quizapp.controller;

import com.example.quizapp.dto.*;
import com.example.quizapp.entity.*;
import com.example.quizapp.repository.*;
import com.example.quizapp.service.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {
    private final QuizRepository quizRepo;
    private final QuestionService questionService;
    private final ScoringService scoringService;

    public QuizController(QuizRepository quizRepo, QuestionService questionService, ScoringService scoringService) {
        this.quizRepo = quizRepo;
        this.questionService = questionService;
        this.scoringService = scoringService;
    }

    @PostMapping
    public ResponseEntity<?> createQuiz(@RequestBody CreateQuizRequest req) {
        Quiz q = new Quiz(req.title);
        quizRepo.save(q);
        return ResponseEntity.ok(java.util.Map.of("id", q.getId(), "title", q.getTitle()));
    }

    @GetMapping
    public ResponseEntity<?> listQuizzes() {
        var list = quizRepo.findAll().stream().map(x -> java.util.Map.of("id", x.getId(), "title", x.getTitle())).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @PostMapping("/{quizId}/questions")
    public ResponseEntity<?> addQuestion(@PathVariable Long quizId, @RequestBody CreateQuestionRequest req) {
        var q = questionService.addQuestion(quizId, req);
        return ResponseEntity.ok(java.util.Map.of("id", q.getId(), "text", q.getText(), "type", q.getType()));
    }

    @GetMapping("/{quizId}/questions")
    public ResponseEntity<?> getQuestionsForQuiz(@PathVariable Long quizId) {
        var quiz = quizRepo.findById(quizId).orElseThrow(() -> new RuntimeException("Quiz not found"));
        var responses = quiz.getQuestions().stream().map(q -> {
            QuestionResponse r = new QuestionResponse();
            r.id = q.getId();
            r.text = q.getText();
            r.type = q.getType().name();
            r.options = q.getOptions().stream().map(o -> {
                QuestionResponse.OptionResponse or = new QuestionResponse.OptionResponse();
                or.id = o.getId();
                or.text = o.getText();
                return or;
            }).collect(Collectors.toList());
            return r;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/{quizId}/submit")
    public ResponseEntity<SubmitAnswersResponse> submit(@PathVariable Long quizId, @RequestBody SubmitAnswersRequest req) {
        var quiz = quizRepo.findById(quizId).orElseThrow(() -> new RuntimeException("Quiz not found"));
        var qIds = quiz.getQuestions().stream().map(Question::getId).collect(Collectors.toSet());
        for (var ans : req.answers) {
            if (!qIds.contains(ans.questionId)) throw new RuntimeException("Question " + ans.questionId + " not part of quiz " + quizId);
        }
        var result = scoringService.score(req);
        return ResponseEntity.ok(new SubmitAnswersResponse(result.score, result.total));
    }
}
