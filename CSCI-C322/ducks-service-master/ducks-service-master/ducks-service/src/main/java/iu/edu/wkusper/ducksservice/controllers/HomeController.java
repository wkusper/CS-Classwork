package iu.edu.wkusper.ducksservice.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
public class HomeController {

    @GetMapping("/")
    public String greeting() {
        return "Welcome to the Ducks Service!";
    }
}