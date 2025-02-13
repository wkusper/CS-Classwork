package edu.iu.c322.test1.controllers;

import edu.iu.c322.test1.model.Question;
import edu.iu.c322.test1.repository.FileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/questions")
public class QuestionController {

    private FileRepository fileRepository;

    public QuestionController(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @PostMapping("/add")
    public boolean add(@RequestBody Question question) {
        try {
            return fileRepository.add(question);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/findAll")
    public List<Question> findAll() {
        try {
            return fileRepository.findAll();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/search")
    public List<Question> find( String answer) {
        try {
            return fileRepository.find(answer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}")
    public Question get( int id) {
        try {
            return fileRepository.get(id);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/{id}/postImage")
    public boolean updateImage(int id,
                                MultipartFile file) {
        try {
            return fileRepository.updateImage(id, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}/getImage")
    public ResponseEntity<?> getImage(int id) {
        try {
            byte[] image = fileRepository.getImage(id);
            return ResponseEntity.status(HttpStatus.FOUND)
                    .contentType(MediaType.IMAGE_PNG)
                    .body(image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
