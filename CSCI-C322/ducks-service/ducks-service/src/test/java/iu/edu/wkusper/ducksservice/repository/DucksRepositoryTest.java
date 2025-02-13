package iu.edu.wkusper.ducksservice.repository;

import iu.edu.wkusper.ducksservice.controllers.DucksController;
import iu.edu.wkusper.ducksservice.model.Duck;
import iu.edu.wkusper.ducksservice.model.Type;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DucksRepositoryTest {
    DucksController ducksController = new DucksController();
    @Test
    void addDuck() {
        assertTrue(DucksController.addDuck(new Duck(1, Type.MALLARD)));
        assertTrue(DucksController.addDuck(new Duck(2, Type.RUBBER_DUCK)));
    }

    @Test
    void getDuck() {
        Duck testDuck = new Duck(3, Type.REDHEAD);
        assertTrue(testDuck.getId() == DucksController.getDuck(3).getId());
        assertTrue(testDuck.getType() == DucksController.getDuck(3).getType());
    }

    @Test
    void getAllDucks() {
    }

    @Test
    void searchDucks() {
    }
}