package ru.nsu.evdokimov;

import org.xml.sax.Attributes;

public class Element {

    public String name;
    public Attributes attributes;

    public Element(String newName, Attributes newAttributes) {
        name = newName;
        attributes = newAttributes;
    }

    public boolean nameEquals(Object obj) {
        return obj.equals(name);
    }

}
