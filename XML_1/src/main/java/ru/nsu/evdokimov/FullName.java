package ru.nsu.evdokimov;

public class FullName {

    private String firstName;
    private String familyName;

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
