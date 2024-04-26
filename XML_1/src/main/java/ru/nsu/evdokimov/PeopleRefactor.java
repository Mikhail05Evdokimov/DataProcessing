package ru.nsu.evdokimov;

import ru.nsu.evdokimov.data.Person;
import ru.nsu.evdokimov.data.Persons;

import java.util.*;

public class PeopleRefactor {

    private final Persons persons;

    private final List<Person> newPeopleList = new ArrayList<>();

    public PeopleRefactor(Persons persons) {
        this.persons = persons;
    }

    public void startRefactor() {
        System.out.println("Start refactoring");
        newPeopleList.addAll(persons.getAllPeople());
        persons.clear();
        System.out.println("Start Clone check");
        for (int i = 0; i < newPeopleList.size(); i++) {
            for (int j = i+1; j < newPeopleList.size(); j++) {
                if (!Objects.equals(newPeopleList.get(j).id, "NULL")) {
                    cloneCheck(i, j);
                }
            }
        }
        newPeopleList.removeIf(i -> Objects.equals(i.id, "NULL"));

        System.out.println("Clone check finished");

        newPeopleList.parallelStream().forEach(this:: selfCheck);
        int deleteCounter = 0;
        for (var pep : newPeopleList) {
            if (pep.id == null) {
                deleteCounter++;
            }
        }
        System.out.println("No ID deleted: " + deleteCounter);
        newPeopleList.removeIf(i -> Objects.equals(i.id, null));

        System.out.println("Self check finished");

        for (int i = 0; i < newPeopleList.size(); i++) {
            for (int j = 0; j < newPeopleList.size(); j++) {
                if (i != j) {
                    husbandCheck(i, j);
                    wifeCheck(i, j);
                    childCheck(i, j);
                }
            }
            siblingsCheck(newPeopleList.get(i));
        }
        System.out.println("Relative check finished");

        newPeopleList.forEach(this::deepChildCheck);

        System.out.println("Deep relative check finished");

        newPeopleList.parallelStream().forEach(this::nullCheck);

        System.out.println("Null check finished");

        newPeopleList.parallelStream().forEach(this::doubleParentCheck);

        persons.setPeople(newPeopleList);
    }

    private void merge(Person a, Person b) {
        if (a.id == null && b.id != null) {
            a.setId(b.id);
        }
        if (a.gender == null && b.gender != null) {
            a.setGender(b.gender);
        }
        if (a.childrenNumber == null && b.childrenNumber != null) {
            a.setChildrenNumber(b.childrenNumber);
        }
        if (a.siblingNumber == null && b.siblingNumber != null) {
            a.setSiblingNumber(b.siblingNumber);
        }
        if (a.name == null && b.name != null ) {
            a.setName(b.name);
        }
        if (a.name != null && a.name.firstName == null && b.name != null && b.name.firstName != null ) {
            a.name.firstName = b.name.firstName;
        }
        if (a.name != null && a.name.familyName == null && b.name != null && b.name.familyName != null ) {
            a.name.familyName = b.name.familyName;
        }
        for (var x : b.family.others.keySet()) {
            a.addMember(x, b.family.others.get(x));
        }
        if (a.family.children.isEmpty()) {
            a.family.children = b.family.children;
        }
        b.setId("NULL");
    }

    private void cloneCheck(int i, int j) {
        var a = newPeopleList.get(i);
        var b = newPeopleList.get(j);
        if ((Objects.equals(a.id, b.id) && a.id != null)
            || (Objects.equals(a.name.firstName, b.name.firstName)
            && Objects.equals(a.name.familyName, b.name.familyName))
            && (a.id == null || b.id == null)) {
            if (Objects.equals(a.gender, b.gender) || a.gender == null || b.gender == null) {
                if (Objects.equals(a.childrenNumber, b.childrenNumber)
                    || a.childrenNumber == null || b.childrenNumber == null) {
                    if (Objects.equals(a.siblingNumber, b.siblingNumber)
                        || a.siblingNumber == null || b.siblingNumber == null) {
                        merge(a, b);
                    }
                }
            }
        }
    }

    private void selfCheck(Person p) {
        List<String> trash = new ArrayList<>();
        if (p.id != null) {
            for (var i : p.family.others.keySet()) {
                if (Objects.equals(p.family.others.get(i), p.id)) {
                    //p.family.others.remove(i); // OK? // NOT OK, damn
                    trash.add(i);
                }
            }
        }
        for (var i : p.family.others.keySet()) {
            if (Objects.equals(p.family.others.get(i), "UNKNOWN")
                || Objects.equals(p.family.others.get(i), "NONE")) {
                trash.add(i);
            }
        }
        for (var i : trash) {
            p.family.others.removeByKey(i);
        }
        p.family.children.removeIf(i -> Objects.equals(i.name, p.id));
        if (p.gender != null) {
            switch (p.gender) {
                case "male" -> p.setGender("M");
                case "female" -> p.setGender("F");
            }
        }
    }

    private void wifeCheck(int i, int j) {
        if (Objects.equals(newPeopleList.get(i).id, newPeopleList.get(j).family.others.get("husband")) &&
            !(newPeopleList.get(i).family.others.containsKey("wife"))) {
                newPeopleList.get(i).family.others.put("wife", newPeopleList.get(j).id);
        }
    }

    private void husbandCheck(int i, int j) {
        if (Objects.equals(newPeopleList.get(i).id, newPeopleList.get(j).family.others.get("wife")) &&
            !(newPeopleList.get(i).family.others.containsKey("husband"))) {
                newPeopleList.get(i).family.others.put("husband", newPeopleList.get(j).id);
        }
    }

    private void childCheck(int i, int j) {
        var a = newPeopleList.get(i);
        var b = newPeopleList.get(j);
        if (Objects.equals(a.id, b.family.others.get("father")) ||
            Objects.equals(a.id, b.family.others.get("mother")) ||
            Objects.equals(a.id, b.family.others.get("parent"))){
            if (b.id != null) {
                if (Objects.equals(b.gender, "M")) {
                    a.family.addChild("son", b.id);
                }
                else {
                    if (Objects.equals(b.gender, "F")) {
                        a.family.addChild("daughter", b.id);
                    }
                    else {
                        a.family.addChild("child", b.id);
                    }
                }
            }
        }
        else {
            if (Objects.equals(a.id, b.family.findChild("daughter")) ||
                Objects.equals(a.id, b.family.findChild("son")) ||
                Objects.equals(a.id, b.family.findChild("child"))) {
                if (Objects.equals(b.gender, "M")) {
                    a.addMember("father", b.id);
                }
                else {
                    if (Objects.equals(b.gender, "F")) {
                        a.addMember("mother", b.id);
                    }
                    else {
                        a.addMember("parent", b.id);
                    }
                }
            }
        }
    }

    private void deepChildCheck(Person b) {
        Person c = new Person();
        if (b.family.others.containsKey("wife")) {
            c = findById(b.family.others.get("wife"));
        }
        if (b.family.others.containsKey("husband")) {
            c = findById(b.family.others.get("husband"));
        }
        if (c!= null && !(c.family.children.isEmpty())) {
            for (var i : c.family.children) {
                if (!(b.family.children.contains(i))) {
                    b.addChild(i);
                    var child = findById(i.name);
                    String parentRole = "parent";
                    if (b.gender != null) {
                        switch (b.gender) {
                            case "M" -> parentRole = "father";
                            case "F" -> parentRole = "mother";
                            default -> parentRole = "parent";
                        };
                    }
                    if (child != null) {
                        child.family.others.put(parentRole, b.id);
                    }
                    else {
                        child = findByName(i.name);
                        if (child != null) {
                            child.family.others.put(parentRole, b.id);
                        }
                    }
                }
            }
        }
    }

    public void doubleParentCheck(Person p) {
        if (p.family != null && p.family.others != null) {
            for (int i = 0; i < p.family.others.size(); i++) {
                for (int j = i + 1; j < p.family.others.size(); j++) {
                    if (p.family.others.get(i).getValue1().equals(p.family.others.get(j).getValue1())) {
                        p.family.others.get(j).setValue("NULL");
                    }
                    if (p.family.others.get(i).getKey().equals(p.family.others.get(j).getKey())
                        && (p.family.others.get(i).getKey().equals("father") || p.family.others.get(i).getKey().equals("mother"))) {
                        p.family.others.get(j).setValue("NULL");
                    }
                }
            }
            p.family.others.removeIf(i -> i.getValue1().equals("NULL"));
            if (p.family.others.containsKey("parent") &&
                p.family.others.containsKey("mother") &&
                p.family.others.containsKey("father")) {
                var parent = findById(p.family.others.get("parent"));
                if (parent != null) {
                    if (parent.gender == null) {
                        if (parent.name != null) {
                            if (p.family.others.get("father").equals(parent.name.familyName + ' ' + parent.name.familyName)) {
                                p.family.others.getO("father").setValue("NULL");
                            } else {
                                if ((p.family.others.get("mother").equals(parent.name.familyName + ' ' + parent.name.familyName))) {
                                    p.family.others.getO("mother").setValue("NULL");
                                } else {
                                    badDays(p);
                                }
                            }
                        }
                        else {
                            badDays(p);
                        }
                    }
                    else {
                        if (parent.gender.equals("M")) {
                            p.family.others.getO("father").setValue("NULL");
                        }
                        else { p.family.others.getO("mother").setValue("NULL"); }
                    }
                }
                else {
                    badDays(p);
                }
            }
            p.family.others.removeIf(i -> i.getValue1().equals("NULL"));
        }
    }

    private void badDays(Person p) {
        var parent = p.family.others.getO("parent");
        if (parent.getValue1().contains(" ")) {
            parent.setValue("NULL");
        }
        var mother = p.family.others.getO("mother");
        if (mother.getValue1().contains(" ")) {
            mother.setValue("NULL");
        }
        var father = p.family.others.getO("father");
        if (father.getValue1().contains(" ")) {
            father.setValue("NULL");
        }
    }

    public void nullCheck(Person p) {
        if (p.name.familyName == null && p.name.firstName == null) {
            p.name = null;
        }
        if (p.family.others.isEmpty()) {
            p.family.others = null;
        }
        if (p.family.children.isEmpty()) {
            p.family.children = null;
        }
        if (p.family.others == null && p.family.children == null) {
            p.family = null;
        }
    }


    public Person findById(String id) {
        for (var i : newPeopleList) {
            if (Objects.equals(i.id, id)) {
                return i;
            }
        }
        return null;
    }

    public Person findByName(String name) {
        for (var i : newPeopleList) {
            if (Objects.equals(i.name.firstName + ' ' + i.name.familyName, name)) {
                return i;
            }
        }
        return null;
    }

    public void siblingsCheck(Person p) {
        if (p.family.others.containsKey("sibling")) {
            var s = findById(p.family.others.get("sibling"));
            if (s != null && s.family.others.notContainsSibling(p.id)) {
                s.family.others.put("sibling", p.id);
            }
        }
        if (p.family.others.containsKey("sister")) {
            var s = findById(p.family.others.get("sister"));
            if (s != null && s.family.others.notContainsSibling(p.id)) {
                s.family.others.put("sibling", p.id);
            }
        }
        if (p.family.others.containsKey("brother")) {
            var s = findById(p.family.others.get("brother"));
            if (s != null && s.family.others.notContainsSibling(p.id)) {
                s.family.others.put("sibling", p.id);
            }
        }
    }


}
