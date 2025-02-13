package iu.edu.wkusper.ducksservice.model;


public class Duck {
    private int id;
    private Type type;


    public Duck(int id, Type type) {
        this.id = id;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public Type getType() {
        return type;
    }
}
