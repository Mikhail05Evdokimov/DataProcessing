package ru.nsu.evdokimov.data;

public class FullName {

    public String firstName;
    public String familyName;

    public FullName(String name, String surName) {
        familyName = surName;
        firstName = name;
    }

    public FullName() {}

    @Override
    public String toString() {
        return this.firstName + " " + this.familyName;
    }

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
