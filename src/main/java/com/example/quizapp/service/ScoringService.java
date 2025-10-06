package com.example.quizapp.service;

import com.example.quizapp.dto.SubmitAnswersRequest;
import com.example.quizapp.entity.*;
import com.example.quizapp.exception.BadRequestException;
import com.example.quizapp.repository.QuestionRepository;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScoringService {
    private final QuestionRepository questionRepo;
    public ScoringService(QuestionRepository questionRepo) { this.questionRepo = questionRepo; }

    public SubmitResult score(SubmitAnswersRequest req) {
        if (req.answers == null || req.answers.isEmpty()) throw new BadRequestException("No answers provided");
        int total = req.answers.size();
        int score = 0;
        for (var ans : req.answers) {
            var q = questionRepo.findById(ans.questionId).orElseThrow(() -> new BadRequestException("Question not found: " + ans.questionId));
            if (q.getType() == QuestionType.TEXT) {
                String expected = Optional.ofNullable(q.getCorrectAnswerText()).orElse("").trim().toLowerCase();
                String provided = Optional.ofNullable(ans.textAnswer).orElse("").trim().toLowerCase();
                if (!expected.isEmpty() && !provided.isEmpty() && expected.equals(provided)) score++;
            } else {
                Set<Long> correctIds = q.getOptions().stream().filter(Option::isCorrect).map(Option::getId).collect(Collectors.toSet());
                Set<Long> providedIds = ans.selectedOptionIds == null ? Collections.emptySet() : new HashSet<>(ans.selectedOptionIds);
                if (q.getType() == QuestionType.SINGLE_CHOICE) {
                    if (providedIds.size() == 1 && correctIds.equals(providedIds)) score++;
                } else {
                    if (correctIds.equals(providedIds)) score++;
                }
            }
        }
        return new SubmitResult(score, total);
    }

    public static class SubmitResult { public final int score; public final int total; public SubmitResult(int s, int t) { score = s; total = t; } }
}
