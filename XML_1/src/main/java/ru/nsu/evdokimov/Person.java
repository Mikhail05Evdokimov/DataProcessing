package ru.nsu.evdokimov;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "person")
public class Person {
    public String id;
    public FullName name;
    public String gender;
    public String childrenNumber;
    public String siblingNumber;
    public Family family;

    public Person() {
        name = new FullName();
        family = new Family();
    }

    public Person(String nullId) {
        id = nullId;
        name = new FullName();
        family = new Family();
    }

    public void addMember(String member, String value) {
        if (!(value.contains("\n"))) {
            family.others.put(member, value);
        }
    }

    public void addChild(String member, String value) {
        if (!(value.contains("\n"))) {
            family.addChild(member, value);
        }
    }

    public void addChild(Child child) {
        family.addChild(child);
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
