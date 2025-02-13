package edu.iu.c322.test1.repository;

import edu.iu.c322.test1.model.Question;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileRepositoryTest {


    @BeforeAll
    static void setup() throws IOException {
        Files.deleteIfExists(Paths.get("questions.txt"));
    }

    @AfterAll
    static void cleanup() throws IOException {
        Files.delete(Paths.get("questions.txt"));
    }

    @Test
    void addQuestion1() {
        Question q = new Question(1,
                "Which word matches the image?",
                "elephant",
                new String[]{"tiger", "bear", "elephant"});

        addToDB(q);
    }

    @Test
    void addQuestion2() {
        Question q = new Question(1,
                "Which word matches the image?",
                "leopard",
                new String[]{"tiger", "lion", "leopard"});

        addToDB(q);
    }

    @Test
    void addQuestion3() {
        Question q = new Question(1,
                "Which word matches the image?",
                "lion",
                new String[]{"cheetah", "lion", "leopard"});

        addToDB(q);
    }

    void addToDB(Question question) {
        FileRepository fileRepository = new FileRepository();
        boolean result = false;
        try {
            result = fileRepository.add(question);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            fail();
        }
        assertTrue(result);
    }




    @Test
    void findAll() {
        FileRepository fileRepository = new FileRepository();
        try {
            List<Question> data = fileRepository.findAll();
            assertEquals(3, data.size());
        } catch (IOException e) {
            fail();
        }
    }
}