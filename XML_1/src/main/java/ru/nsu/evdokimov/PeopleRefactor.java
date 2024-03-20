package ru.nsu.evdokimov;

import ru.nsu.evdokimov.data.Person;
import ru.nsu.evdokimov.data.Persons;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PeopleRefactor {
    private int genId = 13;
    private final Persons persons;

    private final List<Person> newPeopleList = new ArrayList<>();

    public PeopleRefactor(Persons persons) {
        this.persons = persons;
    }

    public void startRefactor() {
        System.out.println("Start refactoring");
        newPeopleList.addAll(persons.getAllPeople());
        for (int i = 0; i < newPeopleList.size(); i++) {
            for (int j = i+1; j < newPeopleList.size(); j++) {
                cloneCheck(i, j);
            }
        }
        newPeopleList.removeIf(i -> Objects.equals(i.id, "NULL"));

        System.out.println("Clone check finished");

        newPeopleList.parallelStream().forEach(this:: selfCheck);
        newPeopleList.parallelStream().forEach(this:: idCheck);

        System.out.println("Self check finished");

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
            siblingsCheck(newPeopleList.get(i));
        }
        System.out.println("Relative check finished");

        newPeopleList.forEach(this::deepChildCheck);

        System.out.println("Deep relative check finished");

        newPeopleList.parallelStream().forEach(this::nullCheck);

        persons.setPeople(newPeopleList);
    }

    private void merge(int i, int j) {
        var a = newPeopleList.get(i);
        var b = newPeopleList.get(j);
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
            a.setGender(b.siblingNumber);
        }
        if (a.name == null && b.name != null ) {
            a.setName(b.name);
        }
        for (var x : b.family.others.keySet()) {
            a.addMember(x, b.family.others.get(x));
        }
        if (a.family.children.isEmpty()) {
            a.family.children = b.family.children;
        }
        newPeopleList.set(i, a);
        newPeopleList.set(j, new Person("NULL"));
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
                        merge(i, j);
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
            if (newPeopleList.get(j).id != null) {
                newPeopleList.get(i).family.others.put("wife", newPeopleList.get(j).id);
            }
            else {
                newPeopleList.get(i).family.others.put("wife", newPeopleList.get(j).name.toString());
            }
        }
    }

    private void husbandCheck(int i, int j) {
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

    private void childCheck(int i, int j) {
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

    public void idCheck(Person p) {
        if (p.id == null) {
            p.id = generateID();
        }
    }

    public String generateID() {
        genId++;
        return "ID" + genId;
    }

    public void siblingsCheck(Person p) {
        if (p.family.others.containsKey("sibling")) {
            var s = findById(p.family.others.get("sibling"));
            if (s != null && !(s.family.others.containsKey("sibling"))) {
                s.family.others.put("sibling", p.id);
            }
        }
    }


}
