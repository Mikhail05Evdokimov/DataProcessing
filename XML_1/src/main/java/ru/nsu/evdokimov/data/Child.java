package ru.nsu.evdokimov.data;

import javax.xml.bind.annotation.*;

public class Child {

    @XmlAttribute(name = "role")
    public String childRole;
    @XmlValue
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