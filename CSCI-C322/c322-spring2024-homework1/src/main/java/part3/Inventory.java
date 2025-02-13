package part3;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class Inventory {
    private List<Guitar> guitars = new java.util.ArrayList<>();


    public Guitar getGuitar(String serialNumber) {
        for (Guitar guitar : guitars) {
            if (guitar.getSerialNumber().equals(serialNumber)) {
                return guitar;
            }
        }
        return null;
    }
    public Guitar search(Guitar inputGuitar) {
        for (Guitar guitar : guitars) {
            if (inputGuitar.getBuilder() != null && !inputGuitar.getBuilder().equals("") && !inputGuitar.getBuilder().equals(guitar.getBuilder())) {
                continue;
            }
            if (inputGuitar.getModel() != null && !inputGuitar.getModel().equals("") && !inputGuitar.getModel().equals(guitar.getModel())) {
                continue;
            }
            if (inputGuitar.getType() != null && !inputGuitar.getType().equals("") && !inputGuitar.getType().equals(guitar.getType())) {
                continue;
            }
            if (inputGuitar.getBackWood() != null && !inputGuitar.getBackWood().equals("") && !inputGuitar.getBackWood().equals(guitar.getBackWood())) {
                continue;
            }
            if (inputGuitar.getTopWood() != null && !inputGuitar.getTopWood().equals("") && !inputGuitar.getTopWood().equals(guitar.getTopWood())) {
                continue;
            }
            return guitar;
        }
        return null;
    }
    public void addGuitar(String serialNumber, double price, String builder, String model, String type, String backWood, String topWood) {
        String filename = "guitars_database.txt";
        String guitarInfo = serialNumber + "," + price + "," + builder + "," + model + "," + type + "," + backWood + "," + topWood + "\n";
        guitars.add(new Guitar(serialNumber, price, builder, model, type, backWood, topWood));
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)))) {
            writer.write(guitarInfo);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
