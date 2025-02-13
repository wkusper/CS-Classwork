package part3;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GuitarTests {

    @Test
    public void testAddGuitar() {
        Inventory inventory = new Inventory();
        inventory.addGuitar("V95693", 1499.95, "Fender", "Stratocastor", "electric", "Alder", "Alder");
        inventory.addGuitar("V9512", 1549.95, "Fender", "Stratocastor", "electric", "Alder", "Alder");
        assertTrue(inventory.getGuitar("V95693") instanceof Guitar);
        assertTrue(inventory.getGuitar("V9512") instanceof Guitar);
    }
    @Test
    public void testGetGuitar() {
        Inventory inventory = new Inventory();
        inventory.addGuitar("V95693", 1499.95, "Fender", "Stratocastor", "electric", "Alder", "Alder");
        inventory.addGuitar("V9512", 1529.95, "Fender", "Stratocastor", "electric", "Alder", "Alder");
        assertEquals("V95693", inventory.getGuitar("V95693").getSerialNumber());
        assertEquals("V9512", inventory.getGuitar("V9512").getSerialNumber());
    }
    @Test
    public void testSearch() {
        Inventory inventory = new Inventory();
        inventory.addGuitar("V95693", 1499.95, "Fender", "Stratocastor", "electric", "Alder", "Alder");
        inventory.addGuitar("V9512", 1549.95, "Fender", "Stratocastor", "electric", "Alder", "Alder");
        Guitar searchGuitar = new Guitar("V95693", 1499.95, "Fender", "Stratocastor", "electric", "Alder", "Alder");
        assertEquals(null, inventory.search(new Guitar("X2114", 0, "Collings", "CJ", "acoustic", "Indian Rosewood", "Sitka")));
        assertTrue(inventory.search(searchGuitar) instanceof Guitar);
        assertEquals("V95693", inventory.search(searchGuitar).getSerialNumber());
    }
}
