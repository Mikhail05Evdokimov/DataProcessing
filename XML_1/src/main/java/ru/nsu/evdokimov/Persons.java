package ru.nsu.evdokimov;

import java.util.ArrayList;
import java.util.List;

public class Persons {

    private static final List<Person> people = new ArrayList<>();

    public static void addPerson(Person person) {
        people.add(person);
    }

    public static List<Person> getAllPeople() {
        return people;
    }

}
