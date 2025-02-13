package iu.edu.wkusper.ducksservice.controllers;

import iu.edu.wkusper.ducksservice.model.Duck;
import iu.edu.wkusper.ducksservice.model.Type;
import iu.edu.wkusper.ducksservice.repository.DucksRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ducks")
public class DucksController {



    @PostMapping("/add")
    public static boolean addDuck(@RequestBody Duck data) {
        try {
            DucksRepository ducksRepository = new DucksRepository();
            ducksRepository.add(data);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
    @GetMapping("/all")
    public List<Duck> getAllDucks() {
        try {
            DucksRepository ducksRepository = new DucksRepository();
            return ducksRepository.getAllDucks();
        }
        catch (Exception e) {
            return null;
        }
    }
    @GetMapping("/{id}")
    public static Duck getDuck(@PathVariable int id) {
        try {
            DucksRepository ducksRepository = new DucksRepository();
            return ducksRepository.get(id);
        }
        catch (Exception e) {
            return null;
        }
    }
    @PostMapping("/search")
    public List<Duck> search(@RequestParam(required = false, defaultValue = "") String type) {
        try {
            DucksRepository ducksRepository = new DucksRepository();
            Type typeEnum = Type.valueOf(type.toUpperCase());
            return ducksRepository.searchDucks(typeEnum);
        }
        catch (Exception e) {
            return null;
        }
    }

    @PostMapping("{id}/setImage")
    public boolean setImage(@PathVariable int id, @RequestBody String image) {
        return true;
    }
    @GetMapping("{id}/getImage")
    public String getImage(@PathVariable int id) {
        return "image";
    }
    @PostMapping("{id}/setAudio")
    public boolean setAudio(@PathVariable int id, @RequestBody String audio) {
        return true;
    }
    @GetMapping("{id}/getAudio")
    public String getAudio(@PathVariable int id) {
        return "audio";
    }
}
