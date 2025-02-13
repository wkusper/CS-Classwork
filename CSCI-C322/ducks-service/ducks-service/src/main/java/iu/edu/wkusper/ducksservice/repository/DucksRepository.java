package iu.edu.wkusper.ducksservice.repository;

import iu.edu.wkusper.ducksservice.model.Duck;
import iu.edu.wkusper.ducksservice.model.Type;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

@Component
public class DucksRepository {
    private static final String NEW_LINE = System.lineSeparator();
    private static void appendToFile(Path path, String content) throws IOException {
        Files.write(path, content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }
    public void add(Duck duck) throws IOException {
        Path path = Path.of("ducks-database.txt");
        String content = duck.getId() + "," + duck.getType().toString();
        appendToFile(path, content + NEW_LINE);
    }
    public Duck get(int id) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("ducks-database.txt"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (Integer.parseInt(parts[0]) == id) {
                    return new Duck(Integer.parseInt(parts[0]), Type.valueOf(parts[1].toUpperCase()));
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Duck> getAllDucks() {
        List<Duck> ducks = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("ducks-database.txt"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                ducks.add(new Duck(Integer.parseInt(parts[0]), Type.valueOf(parts[1].toUpperCase())));
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return ducks;
    }
    public List<Duck> searchDucks(Type type) {
        List<Duck> ducks = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("ducks-database.txt"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (Type.valueOf(parts[1].toUpperCase()) == type) {
                    ducks.add(new Duck(Integer.parseInt(parts[0]), Type.valueOf(parts[1].toUpperCase())));
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return ducks;
    }

}
