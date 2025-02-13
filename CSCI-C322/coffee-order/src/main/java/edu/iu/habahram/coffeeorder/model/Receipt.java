package edu.iu.habahram.coffeeorder.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(schema = "coffees")
public final class Receipt {

    @Id
    private int id;
    private String description;
    private float cost;

    public Receipt() {

    }

    public Receipt(int id, String description, float cost) {
        this.id = id;
        this.description = description;
        this.cost = cost;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public float getCost() {
        return cost;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }


    public String toString() {
        return id + ", "+ description + ", " + cost + System.lineSeparator();
    }
}