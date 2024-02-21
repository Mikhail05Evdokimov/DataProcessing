package ru.nsu.evdokimov;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Family {

    public Map<String, String> others;
    public List<Child> children;

    public Family() {
        others = new HashMap<>();
        children = new ArrayList<>();
    }

    public void addChild(String gen, String nameOrId) {
        children.add(new Child(gen, nameOrId));
    }

}
