package ru.nsu.evdokimov;

public class Child {
    public String childRole;
    public String name;

    public Child(String gen, String nameOrId) {
        if (gen == null) {
            gen = "child";
        }
        else {
            switch (gen) {
                case "M", "son" -> gen = "son";
                case "daughter", "F" -> gen = "daughter";
                default -> gen = "child";
            }
        }
        childRole = gen;
        name = nameOrId;
    }
}