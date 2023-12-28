package ru.nsu.evdokimov;

import java.util.HashMap;
import java.util.Map;

public class Person {
    private String id;
    private FullName name;
    private String gender;
    private String childrenNumber;
    private String siblingNumber;
    private Map<String, String> family;

    public Person() {
        name = new FullName();
        family = new HashMap<>();
    }

    public void addMember(String member, String value) {
        if (!(value.contains("\n"))) {
            family.put(member, value);
        }
    }

    public void setId(String value) {
        if (!(value.contains("\n"))) {
            id = value;
        }
    }

    public void setName(FullName value) {
        name = value;
    }

    public void setGender(String value) {
        if (!(value.contains("\n"))) {
            gender = value;
        }
    }

    public void setChildrenNumber(String value) {
        if (!(value.contains("\n"))) {
            childrenNumber = value;
        }
    }

    public void setSiblingNumber(String value) {
        if (!(value.contains("\n"))) {
            siblingNumber = value;
        }
    }

}
