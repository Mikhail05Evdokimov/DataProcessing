package ru.nsu.evdokimov;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Family {

    @XmlElementWrapper(name = "others")
    @XmlElement(name = "relative")
    public Map<String, String> others;
    @XmlElementWrapper(name = "children")
    @XmlElement(name = "child")
    public List<Child> children;

    public Family() {
        others = new HashMap<>();
        children = new ArrayList<>();
    }

    public void addChild(String gen, String nameOrId) {
        children.add(new Child(gen, nameOrId));
    }

    public void addChild(Child child) {
        children.add(child);
    }

}
