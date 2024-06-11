package ru.nsu.evdokimov;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import ru.nsu.evdokimov.data.FullName;
import ru.nsu.evdokimov.data.Person;
import ru.nsu.evdokimov.data.Persons;

public class Parser extends DefaultHandler{

    private Person person;
    private String mock;
    private final Persons persons;

    public Parser(Persons persons){
        this.persons = persons;
    }

    @Override
    public void startDocument() {
        System.out.println("Start parse XML...");
    }

    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes attributes) {

        switch (qName) {
            case "person":
                person = new Person();
                person.name = new FullName();
                if (attributes.getValue("id") != null) {
                    person.setId(attributes.getValue("id"));
                }
                if (attributes.getValue("name") != null) {
                    person.setName(nameParser(attributes.getValue("name")));
                }
                break;
            case "fullname":
                break;
            case "wife":
                if (attributes.getValue(0) != null) {
                    person.addMember("wife", cutTabs(attributes.getValue(0)));
                }
                break;
            case "husband":
                if (attributes.getValue(0) != null) {
                    person.addMember("husband", cutTabs(attributes.getValue(0)));
                }
                break;
            case "siblings":
                if (attributes.getValue(0) != null) {
                    person.addMember("sibling", cutTabs(attributes.getValue(0)));
                }
                break;
            case "surname":
                if (attributes.getValue(0) != null) {
                    person.name.setFamilyName(cutTabs(attributes.getValue(0)));
                }
                break;
            case "children-number":
                if (attributes.getValue(0) != null) {
                    person.setChildrenNumber(attributes.getValue(0));
                }
                break;
            case "parent":
                if (attributes.getValue(0) != null) {
                    person.addMember("parent", cutTabs(attributes.getValue(0)));
                }
                break;
            case "siblings-number":
                if (attributes.getValue(0) != null) {
                    person.setSiblingNumber(attributes.getValue(0));
                }
                break;
            case "son":
                if (attributes.getValue(0) != null) {
                    person.addChild("son", cutTabs(attributes.getValue(0)));
                }
                break;
            case "spouce":
                if (attributes.getValue(0) != null) {
                    person.addMember("spouce", cutTabs(attributes.getValue(0)));
                }
                break;
            case "gender":
                if (attributes.getValue(0) != null) {
                    person.setGender(cutTabs(attributes.getValue(0)));
                }
                break;
            case "firstname":
                if (attributes.getValue(0) != null) {
                    person.name.setFirstName(cutTabs(attributes.getValue(0)));
                }
                break;
            case "daughter":
                if (attributes.getValue(0) != null) {
                    person.addChild("daughter", cutTabs(attributes.getValue(0)));
                }
                break;
            case "father":
                if (attributes.getValue(0) != null) {
                    person.addMember("father", cutTabs(attributes.getValue(0)));
                }
                break;
            case "brother":
                if (attributes.getValue(0) != null) {
                    person.addMember("brother", cutTabs(attributes.getValue(0)));
                }
                break;
            case "id":
                if (attributes.getValue(0) != null) {
                    person.setId(cutTabs(attributes.getValue(0)));
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

        mock = cutTabs(mock);

        switch (qName) {
            case "person" -> { persons.addPerson(person); }
            case "first", "firstname" -> person.name.setFirstName(mock);
            case "family", "surname", "family-name" -> person.name.setFamilyName(mock);
            case "wife" -> person.addMember("wife", mock);
            case "husband" -> person.addMember("husband", mock);
            case "mother" -> person.addMember("mother", mock);
            case "siblings" -> person.addMember("sibling", mock);
            case "son" -> person.addChild("son", mock);
            case "daughter" -> person.addChild("daughter", mock);
            case "father" -> person.addMember("father", mock);
            case "gender" -> person.setGender(mock);
            case "children-number" -> person.setChildrenNumber(mock);
            case "child" -> person.addChild("child", mock);
            case "parent" -> person.addMember("parent", mock);
            case "sister" -> person.addMember("sister", mock);
            case "spouce" -> person.addMember("spouce", mock);
            case "brother" -> person.addMember("brother", mock);

            default -> {
            }
        }
    }

    @Override
    public void endDocument() {
        System.out.println("Stop parse XML...");
    }

    private FullName nameParser(String name) {
        name = cutTabs(name);
        int i = name.indexOf(" ");
        String first = name.substring(0, i);
        String second = name.substring(i+1);
        return new FullName(first, second);
    }

    private String cutTabs(String word) {
        word = word.replace("  ", " ");
        if(word.startsWith(" ")) {
            word = word.substring(1);
        }
        if(word.endsWith(" ")) {
            word = word.substring(0, word.length()-1);
        }
        return word.replace("  ", " ");
    }


}
