package edu.iu.wkusper.guitar.model;

public enum Type {
    ACOUSTIC, ELECTRIC;

    public String toString() {
        switch (this) {
            case ACOUSTIC:
                return "acoustic";
            case ELECTRIC:
                return "electric";
            default:
                return "Unspecified";
        }
    }
}
