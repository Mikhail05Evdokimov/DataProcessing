package ru.nsu.evdokimov;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PeopleRefactor {

    private static final List<Person> newPeopleList = new ArrayList<>();

    public static void startRefactor() {
        newPeopleList.addAll(Persons.getAllPeople());
        for (int i = 0; i < newPeopleList.size(); i++) {
            for (int j = i+1; j < newPeopleList.size(); j++) {
                cloneCheck(i, j);
            }
        }
        newPeopleList.removeIf(i -> Objects.equals(i.id, "NULL"));
        for (var i : newPeopleList) {
            selfCheck(i);
        }
        for (int i = 0; i < newPeopleList.size(); i++) {
            for (int j = 0; j < newPeopleList.size(); j++) {
                if (i != j) {
                    if (newPeopleList.get(i).id != null) {
                        husbandCheck(i, j);
                        wifeCheck(i, j);
                        childCheck(i, j);
                    }
                }
            }
        }
        for (var i : newPeopleList) {
            deepChildCheck(i);
        }
        Persons.setPeople(newPeopleList);
    }

    private static void merge(int i, int j) {
        var a = newPeopleList.get(i);
        var b = newPeopleList.get(j);
        if (a.id == null && b.id != null) {
            a.setId(b.id);
        }
        if (a.gender == null && b.gender != null) {
            a.setGender(b.gender);
        }
        if (a.childrenNumber == null && b.childrenNumber != null) {
            a.setGender(b.childrenNumber);
        }
        if (a.siblingNumber == null && b.siblingNumber != null) {
            a.setGender(b.siblingNumber);
        }
        if (a.name == null && b.name != null ) {
            a.setName(b.name);
        }
        if (a.family.others.isEmpty()) {
            a.family.others = b.family.others;
        }
        if (a.family.children.isEmpty()) {
            a.family.children = b.family.children;
        }
        newPeopleList.set(i, a);
        newPeopleList.set(j, new Person("NULL"));
    }

    private static void cloneCheck(int i, int j) {
        var a = newPeopleList.get(i);
        var b = newPeopleList.get(j);
        if ((Objects.equals(a.id, b.id) && a.id != null)
            || (Objects.equals(a.name.firstName, b.name.firstName)
            && Objects.equals(a.name.familyName, b.name.familyName))
            && (a.id == null || b.id == null)) {
            merge(i, j);
        }
    }

    private static void selfCheck(Person p) {
        if (p.id != null) {
            for (var i : p.family.others.keySet()) {
                if (Objects.equals(p.family.others.get(i), p.id)) {
                    p.family.others.remove(i);
                }
            }
            p.family.children.removeIf(i -> Objects.equals(i.name, p.id));
        }
    }

    private static void wifeCheck(int i, int j) {
        if (Objects.equals(newPeopleList.get(i).id, newPeopleList.get(j).family.others.get("husband")) &&
            !(newPeopleList.get(i).family.others.containsKey("wife"))) {
            if (newPeopleList.get(j).id != null) {
                newPeopleList.get(i).family.others.put("wife", newPeopleList.get(j).id);
            }
            else {
                newPeopleList.get(i).family.others.put("wife", newPeopleList.get(j).name.toString());
            }
        }
    }

    private static void husbandCheck(int i, int j) {
        if (Objects.equals(newPeopleList.get(i).id, newPeopleList.get(j).family.others.get("wife")) &&
            !(newPeopleList.get(i).family.others.containsKey("husband"))) {
            if (newPeopleList.get(j).id != null) {
                newPeopleList.get(i).family.others.put("husband", newPeopleList.get(j).id);
            }
            else {
                newPeopleList.get(i).family.others.put("husband", newPeopleList.get(j).name.toString());
            }
        }
    }

    private static void childCheck(int i, int j) {
        var a = newPeopleList.get(i);
        var b = newPeopleList.get(j);
        if (Objects.equals(a.id, b.family.others.get("father")) ||
            Objects.equals(a.id, b.family.others.get("mother"))) {
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
            else {
                if (Objects.equals(b.gender, "M")) {
                    a.family.addChild("son", b.name.firstName + ' ' + b.name.familyName);
                }
                else {
                    if (Objects.equals(newPeopleList.get(j).gender, "F")) {
                        a.family.addChild("daughter", b.name.firstName + ' ' + b.name.familyName);
                    }
                    else {
                        a.family.addChild("child", b.name.firstName + ' ' + b.name.familyName);
                    }
                }
            }
        }
    }

    private static void deepChildCheck(Person b) {
        Person c = new Person();
        if (b.family.others.containsKey("wife")) {
            c = findById(b.family.others.get("wife"));
        }
        if (b.family.others.containsKey("husband")) {
            c = findById(b.family.others.get("husband"));
        }
        if (c!= null && b.family.children.isEmpty() && !(c.family.children.isEmpty())) {
            b.family.children = c.family.children;
        }
    }

    public static Person findById(String id) {
        for (var i : newPeopleList) {
            if (Objects.equals(i.id, id)) {
                return i;
            }
        }
        return null;
    }

}
