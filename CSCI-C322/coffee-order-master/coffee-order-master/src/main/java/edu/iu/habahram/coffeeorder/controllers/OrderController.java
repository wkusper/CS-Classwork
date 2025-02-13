package edu.iu.habahram.coffeeorder.controllers;

import edu.iu.habahram.coffeeorder.model.*;
import edu.iu.habahram.coffeeorder.repository.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/orders")
public class OrderController {
    private OrderRepository orderRepository;

    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody OrderData order) {
        try {
            Beverage beverage = null;

            switch (order.beverage().toLowerCase().strip()) {
                case "dark roast":
                    beverage = new DarkRoast();
                    break;
                case "espresso":
                    beverage = new Espresso();
                    break;
                case "house blend":
                    beverage = new HouseBlend();
                    break;
                case "decaf":
                    beverage = new Decaf();
                    break;
                default:
                    throw new Exception("Beverage type '%s' is not valid!".formatted(order.beverage()));
            }


            for(String condiment : order.condiments()) {
                switch (condiment.toLowerCase().strip()) {
                    case "milk":
                        beverage = new Milk(beverage);
                        break;
                    case "mocha":
                        beverage = new Mocha(beverage);
                        break;
                    case "soy":
                        beverage = new Soy(beverage);
                        break;
                    case "whip":
                        beverage = new Whip(beverage);
                        break;
                    default:
                        throw new Exception("Condiment type '%s' is not valid".formatted(condiment));
                }
            }
            Receipt r = new Receipt(generateUniqueNum(), beverage.getDescription(), beverage.cost());
            Receipt receipt = orderRepository.save(r);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(receipt);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    private int generateUniqueNum() {
        double randomNum = Math.random();
        int uniqueNum = (int) (randomNum * 100000);
        uniqueNum = (int) Math.ceil(uniqueNum);
        return uniqueNum;
    }
}