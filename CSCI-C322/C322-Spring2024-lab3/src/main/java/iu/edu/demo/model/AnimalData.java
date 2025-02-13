package iu.edu.demo.model;

public class AnimalData {
    private String name;
    private String picture;
    private String location;

    public AnimalData() {
    }

    public AnimalData(String name, String picture, String location) {
        this.name = name;
        this.picture = picture;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
