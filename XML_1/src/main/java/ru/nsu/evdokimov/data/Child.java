package ru.nsu.evdokimov.data;

import javax.xml.bind.annotation.*;

public class Child {

    @XmlAttribute(name = "role")
    public String childRole;
    @XmlValue
    public String name;

    public Child(String gen, String nameOrId) {
        childRole = gen;
        name = nameOrId;
    }
}