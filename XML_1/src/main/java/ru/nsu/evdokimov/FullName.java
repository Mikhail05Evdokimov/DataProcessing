package ru.nsu.evdokimov;

public class FullName {

    public String firstName;
    public String familyName;

    public FullName(String name, String surName) {
        familyName = surName;
        firstName = name;
    }

    public FullName() {}

    public void setFirstName(String name) {
        if (!(name.contains("\n"))) {
            firstName = name;
        }
    }

    public void setFamilyName(String name) {
        if (!(name.contains("\n"))) {
            familyName = name;
        }
    }

}
