package iu.edu.wkusper.ducksservice.model;

public enum Type {
    MALLARD, REDHEAD, RUBBER_DUCK, DECOY_DUCK;

    public String toString() {
        switch (this) {
            case MALLARD:
                return "Mallard";
            case REDHEAD:
                return "Redhead";
            case RUBBER_DUCK:
                return "Rubber Duck";
            case DECOY_DUCK:
                return "Decoy Duck";
            default:
                return "Unknown";
        }
    }
}
