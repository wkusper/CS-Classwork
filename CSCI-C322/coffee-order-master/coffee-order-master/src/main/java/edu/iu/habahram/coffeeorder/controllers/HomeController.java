package edu.iu.habahram.coffeeorder.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String greetings() {
        return "Welcome to the coffee order system!";
    }
}