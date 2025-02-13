package edu.iu.c322.test1.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuestionTest {

    @Test
    void toLine() {
        Question q = new Question(1,
                "Which word matches the image?",
                "elephant",
                new String[]{"tiger", "bear", "elephant"});
        String expected = "1,Which word matches the image?,elephant,tiger,bear,elephant";
        assertEquals(expected, q.toLine());
    }

    @Test
    void fromLine() {
        String line = "1,Which word matches the image?,elephant,tiger,bear,elephant";

        Question expected = new Question(1,
                "Which word matches the image?",
                "elephant",
                new String[]{"tiger", "bear", "elephant"});
        Question output = Question.fromLine(line);
          assertEquals (expected.getId(), output.getId());
          assertEquals (expected.getDescription().trim(), output.getDescription().trim());
          assertEquals (expected.getAnswer().trim(), output.getAnswer().trim());
          assertArrayEquals (expected.getChoices(), output.getChoices());
    }
}