package iu.edu.demo.controllers;

import iu.edu.demo.model.AnimalData;
import org.springframework.web.bind.annotation.*;
import iu.edu.demo.repository.AnimalRepository;

import java.util.List;

@RestController
@RequestMapping("/animals")
public class AnimalController {
    private AnimalRepository animalRepository;

    public AnimalController(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    @PostMapping
    public boolean add(@RequestBody AnimalData animalData) {
        try {
            return animalRepository.add(animalData);
        } catch (Exception e) {
            return false;
        }
    }
    @GetMapping
    public List<AnimalData> findAll() {
        try {
            return animalRepository.findAll();
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/search")
    public List<AnimalData> search (@RequestParam String name, @RequestParam String picture,
                                    @RequestParam String location) {
        try {
            System.out.println(name);
            System.out.println(picture);
            System.out.println(location);
            return animalRepository.find(name, picture, location);
        }
        catch (Exception e) {
            return null;
        }
    }
}
