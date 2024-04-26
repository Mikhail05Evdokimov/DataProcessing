package ru.nsu.evdokimov.data;

import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        if (notContainsChild(nameOrId)) {
            children.add(new Child(gen, nameOrId));
        }
    }

    public void addChild(Child child) {
        if (notContainsChild(child.name)) {
            children.add(child);
        }
    }

    public String findChild(String role) {
        for (var c : children) {
            if (Objects.equals(c.childRole, role)) {
                return c.name;
            }
        }
        return null;
    }

    public boolean notContainsChild(String name) {
        for (var c : children) {
            if (Objects.equals(c.name, name)) {
                return false;
            }
        }
        return true;
    }

}
