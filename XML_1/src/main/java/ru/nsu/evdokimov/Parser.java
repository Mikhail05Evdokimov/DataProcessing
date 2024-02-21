package ru.nsu.evdokimov;

import com.google.gson.Gson;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class Parser extends DefaultHandler{

    private Person person;
    private FullName fullName;
    private String mock;

    @Override
    public void startDocument() {
        System.out.println("Start parse XML...");
    }

    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes attributes) {

        switch (qName) {
            case "person":
                person = new Person();
                fullName = new FullName();
                if (attributes.getValue("id") != null) {
                    person.setId(attributes.getValue("id"));
                }
                if (attributes.getValue("name") != null) {
                    person.setName(nameParser(attributes.getValue("name")));
                }
                break;
            case "fullname":
                fullName = new FullName();
                break;
            case "wife":
                if (attributes.getValue(0) != null) {
                    person.addMember("wife", attributes.getValue(0));
                }
                break;
            case "husband":
                if (attributes.getValue(0) != null) {
                    person.addMember("husband", attributes.getValue(0));
                }
                break;
            case "siblings":
                if (attributes.getValue(0) != null) {
                    person.addMember("siblings", attributes.getValue(0));
                }
                break;
            case "surname":
                if (attributes.getValue(0) != null) {
                    fullName.setFamilyName(attributes.getValue(0));
                    person.setName(fullName);
                }
                break;
            case "children-number":
                if (attributes.getValue(0) != null) {
                    person.setChildrenNumber(attributes.getValue(0));
                }
                break;
            case "parent":
                if (attributes.getValue(0) != null) {
                    person.addMember("parent", attributes.getValue(0));
                }
                break;
            case "siblings-number":
                if (attributes.getValue(0) != null) {
                    person.setSiblingNumber(attributes.getValue(0));
                }
                break;
            case "son":
                if (attributes.getValue(0) != null) {
                    person.addChild("son", attributes.getValue(0));
                }
                break;
            case "spouce":
                if (attributes.getValue(0) != null) {
                    person.addMember("spouce", attributes.getValue(0));
                }
                break;
            case "gender":
                if (attributes.getValue(0) != null) {
                    person.setGender(attributes.getValue(0));
                }
                break;
            case "firstname":
                if (attributes.getValue(0) != null) {
                    fullName.setFirstName(attributes.getValue(0));
                    person.setName(fullName);
                }
                break;
            case "daughter":
                if (attributes.getValue(0) != null) {
                    person.addChild("daughter", attributes.getValue(0));
                }
                break;
            case "father":
                if (attributes.getValue(0) != null) {
                    person.addMember("father", attributes.getValue(0));
                }
                break;
        }

    }

    @Override
    public void characters(char[] ch, int start, int length) {

        mock = new String(ch, start, length);

    }

    @Override
    public void endElement(String namespaceURI, String localName, String qName) {

        switch (qName) {
            case "person" -> Persons.addPerson(person);
            case "fullname" -> person.setName(fullName);
            case "first" -> fullName.setFirstName(mock);
            case "family" -> fullName.setFamilyName(mock);
            case "wife" -> person.addMember("wife", mock);
            case "husband" -> person.addMember("husband", mock);
            case "mother" -> person.addMember("mother", mock);
            case "siblings" -> person.addMember("siblings", mock);
            case "son" -> person.addChild("son", mock);
            case "daughter" -> person.addChild("daughter", mock);
            case "surname" -> {
                fullName.setFamilyName(mock);
                person.setName(fullName);
            }
            case "father" -> person.addMember("father", mock);
            case "gender" -> person.setGender(mock);
            default -> {
            }
        }
    }

    @Override
    public void endDocument() {
        System.out.println("Stop parse XML...");
        PeopleRefactor.startRefactor();
        //try(FileWriter writer = new FileWriter(Objects.requireNonNull(this.getClass().getResource("/myFile.json")).getPath())) {
        try(FileWriter writer = new FileWriter("myFile.json")) {
            Gson gson = new Gson();
            gson.toJson(Persons.getAllPeople(), writer);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private FullName nameParser(String name) {
        String first = "";
        int i = 0;
        while (name.charAt(i) != ' ') {
            first = first.concat(String.valueOf(name.charAt(i)));
            i++;
        }
        String second = name.substring(i+1);
        return new FullName(first, second);
    }


}
