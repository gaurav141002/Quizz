package com.example.quizapp.service;

import com.example.quizapp.entity.*;
import com.example.quizapp.dto.SubmitAnswersRequest;
import com.example.quizapp.repository.QuestionRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;

@DataJpaTest
class ScoringServiceTest {

    @Autowired
    QuestionRepository questionRepo;

    ScoringService scoringService;

    @BeforeEach
    void setup() { scoringService = new ScoringService(questionRepo); }

    @Test
    void testSingleChoiceScoring() {
        Question q = new Question("Capital of France?", QuestionType.SINGLE_CHOICE);
        Option o1 = new Option("Paris", true);
        Option o2 = new Option("London", false);
        q.addOption(o1); q.addOption(o2);
        questionRepo.save(q);

        SubmitAnswersRequest req = new SubmitAnswersRequest();
        SubmitAnswersRequest.AnswerItem a = new SubmitAnswersRequest.AnswerItem();
        a.questionId = q.getId();
        a.selectedOptionIds = List.of(o1.getId());
        req.answers = List.of(a);

        var res = scoringService.score(req);
        Assertions.assertEquals(1, res.score);
        Assertions.assertEquals(1, res.total);
    }
}
