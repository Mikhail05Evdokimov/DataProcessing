package ru.nsu.evdokimov;

import javafx.util.Pair;

public class Child {
    public String childRole;
    public String name;

    public Child(String gen, String nameOrId) {
        if (gen == null) {
            gen = "child";
        }
        else {
            switch (gen) {
                case "M" -> gen = "son";
                case "F" -> gen = "daughter";
                default -> gen = "child";
            }
        }
        childRole = gen;
        name = nameOrId;
    }
}