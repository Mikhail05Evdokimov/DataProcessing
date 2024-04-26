package ru.nsu.evdokimov.data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "persons")
public class Persons {

    @XmlElement(name = "person")
    private List<Person> people = new ArrayList<>();

    public void addPerson(Person person) {
        people.add(person);
    }

    public List<Person> getAllPeople() {
        return people;
    }

    public void setPeople(List<Person> people1) {
        people = people1;
    }

    public void clear() {
        people.clear();
    }

}
