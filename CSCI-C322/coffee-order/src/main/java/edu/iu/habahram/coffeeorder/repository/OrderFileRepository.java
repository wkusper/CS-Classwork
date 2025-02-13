
package edu.iu.habahram.coffeeorder.repository;

import edu.iu.habahram.coffeeorder.model.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class OrderFileRepository {
    public Receipt add(OrderData order) throws Exception {
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

        Receipt receipt = new Receipt(generateUniqueNum(), beverage.getDescription(), beverage.cost());

        appendToFile(receipt);

        return receipt;
    }

    public List<String> getOrders() throws IOException {
        Path path = Path.of("db.txt");
        return Files.readAllLines(path);
    }

    private int generateUniqueNum() {
        double randomNum = Math.random();
        int uniqueNum = (int) (randomNum * 100000);
        uniqueNum = (int) Math.ceil(uniqueNum);
        return uniqueNum;
    }

    private void appendToFile(Receipt receipt) throws IOException {
        Path path = Path.of("db.txt");
        String data = receipt.toString();
        if (Files.exists(path)) {
            Files.write(path, data.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        }
        else {
            Files.createFile(path);
            Files.write(path, data.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        }

    }
}