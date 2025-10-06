package com.example.quizapp.service;

import com.example.quizapp.dto.CreateQuestionRequest;
import com.example.quizapp.entity.*;
import com.example.quizapp.exception.BadRequestException;
import com.example.quizapp.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QuestionService {

    private final QuizRepository quizRepo;
    private final QuestionRepository questionRepo;
    private final OptionRepository optionRepo;

    public QuestionService(QuizRepository quizRepo, QuestionRepository questionRepo, OptionRepository optionRepo) {
        this.quizRepo = quizRepo;
        this.questionRepo = questionRepo;
        this.optionRepo = optionRepo;
    }

    @Transactional
    public Question addQuestion(Long quizId, CreateQuestionRequest req) {
        var quiz = quizRepo.findById(quizId).orElseThrow(() -> new BadRequestException("Quiz not found"));
        QuestionType type;
        try { type = QuestionType.valueOf(req.type); } catch (Exception ex) { throw new BadRequestException("Invalid question type"); }

        if (type == QuestionType.TEXT) {
            if (req.correctAnswerText == null || req.correctAnswerText.trim().isEmpty())
                throw new BadRequestException("correctAnswerText required for TEXT questions");
            if (req.correctAnswerText.length() > 300)
                throw new BadRequestException("Text answer exceeds 300 chars");
        } else {
            if (req.options == null || req.options.isEmpty())
                throw new BadRequestException("Options required for choice questions");
            long correctCount = req.options.stream().filter(o -> o.isCorrect).count();
            if (type == QuestionType.SINGLE_CHOICE && correctCount != 1)
                throw new BadRequestException("SINGLE_CHOICE must have exactly 1 correct option");
            if (type == QuestionType.MULTIPLE_CHOICE && correctCount < 1)
                throw new BadRequestException("MULTIPLE_CHOICE must have at least 1 correct option");
        }

        Question q = new Question(req.text, type);
        if (type == QuestionType.TEXT) q.setCorrectAnswerText(req.correctAnswerText.trim());
        questionRepo.save(q);
        quiz.addQuestion(q);
        quizRepo.save(quiz);

        if (req.options != null) {
            for (var o : req.options) {
                Option option = new Option(o.text, o.isCorrect);
                q.addOption(option);
                optionRepo.save(option);
            }
        }
        return q;
    }
}
