package ru.nsu.evdokimov.data;

import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

public class Family {

    @XmlElement(name = "relative")
    public Others others;
    @XmlElement(name = "child")
    public List<Child> children;

    public Family() {
        others = new Others();
        children = new ArrayList<>();
    }

    public void addChild(String gen, String nameOrId) {
        children.add(new Child(gen, nameOrId));
    }

    public void addChild(Child child) {
        children.add(child);
    }

}
