package ru.nsu.evdokimov;

import ru.nsu.evdokimov.data.Child;
import ru.nsu.evdokimov.data.Person;
import ru.nsu.evdokimov.data.Persons;
import ru.nsu.evdokimov.data.Relative;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class PeopleRefactor {

    private final Persons persons;

    private final Map<String, Person> newPeopleList = new HashMap<>();

    public PeopleRefactor(Persons persons) {
        System.out.println("Start refactoring");
        this.persons = persons;
    }

    public void startRefactor() {
        System.out.println("Start Clone check");
        var unmerged = new ArrayList<Person>(5000);
        for (var per : persons.people) {
            if (Objects.equals(per.id, "UNKNOWN") || Objects.equals(per.id, "NULL")) {
                per.id = null;
            }
            if (per.id != null) {
                if (newPeopleList.containsKey(per.id)) {
                    merge(newPeopleList.get(per.id), per);
                }
                else {
                    newPeopleList.put(per.id, per);
                }
            }
            else {
                unmerged.add(per);
            }
        }
        persons.clear();
        System.out.println("Clone check 1 finished");

        var noId = new ArrayList<Person>(2000);
        for (var per : unmerged) {
            boolean flag = true;
            for (var p1 : newPeopleList.values()) {
                if(cloneCheck(p1, per)) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                noId.add(per);
            }
        }
        unmerged.clear();
        System.out.println("Clone check 2 finished");
        noId = noIdMerge(noId);
        noId.forEach(this::noId);
        int deleteCounter = 0;
        try (FileWriter fileWriter = new FileWriter("deleted.txt")) {
            for (var pep : noId) {
                if (pep.id == null) {
                    deleteCounter++;
                    if (pep.name != null) {
                        fileWriter.write(pep.name.firstName + " " + pep.name.familyName + "\n"
                        + pep.siblingNumber + " " + pep.childrenNumber + " " + pep.gender);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Writing error");
        }
        System.out.println("No ID deleted: " + deleteCounter);
        noId.clear();

        newPeopleList.values().parallelStream().forEach(this::selfCheck);

        System.out.println("Self check finished");

        for (Person i : newPeopleList.values()) {
            if (i.family != null) {
                if (i.family.others != null) {
                    for (var mem : i.family.others) {
                        var id = mem.getValue1();
                        if (newPeopleList.containsKey(id)) {
                            var rel = newPeopleList.get(id);
                            if (Objects.equals(mem.getKey(), "husband")) {
                                husbandCheck(i, rel);
                            }
                            if (Objects.equals(mem.getKey(), "wife")) {
                                wifeCheck(i, rel);
                            }
                            parentCheck(i, rel, mem.getKey());
                            siblingsCheck(i, rel, mem.getKey());
                        }
                    }
                }
                if (i.family.children != null) {
                    for (var mem : i.family.children) {
                        var id = mem.name;
                        if (newPeopleList.containsKey(id)) {
                            childCheck(i, newPeopleList.get(id), mem.childRole);
                        }
                    }
                }
            }
        }
        System.out.println("Relative check finished");

        newPeopleList.values().forEach(this::deepChildCheck);

        System.out.println("Deep relative check finished");

        newPeopleList.values().parallelStream().forEach(this::nullCheck);

        System.out.println("Null check finished");

        newPeopleList.values().parallelStream().forEach(this::doubleParentCheck);

        newPeopleList.values().forEach(this::siblingsAndChildCountCheck);

        persons.setPeople(newPeopleList.values().stream().toList());
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
            switch (x) {
                case "sibling", "siblings", "brother", "sister" -> {
                    if (a.siblingNumber == null || a.getCurrentSiblings() < Integer.parseInt(a.siblingNumber)) {
                        a.addMember(x, b.family.others.get(x));
                    }
                }
                default -> a.addMember(x, b.family.others.get(x));
            }
        }

        for (var c : b.family.children) {
            if (a.childrenNumber == null || Integer.parseInt(a.childrenNumber) > a.getCurrentChildren()) {
                a.family.addChild(c);
            }
            else {
                break;
            }
        }

    }

    private boolean cloneCheck(Person a, Person b) {
        if ((Objects.equals(a.name.firstName, b.name.firstName)
            && Objects.equals(a.name.familyName, b.name.familyName))
            ) {
            if (Objects.equals(a.gender, b.gender) || a.gender == null || b.gender == null) {
                if (Objects.equals(a.childrenNumber, b.childrenNumber)
                    || a.childrenNumber == null || b.childrenNumber == null) {
                    if (Objects.equals(a.siblingNumber, b.siblingNumber)
                        || a.siblingNumber == null || b.siblingNumber == null) {
                        if (!theyHaveSomeTrouble(a, b)) {
                            merge(a, b);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean theyHaveSomeTrouble(Person p1, Person p2) {
        var f1 = p1.family.others;
        var f2 = p2.family.others;
        for (Relative relative1 : f1) {
            for (Relative relative2 : f2) {
                switch (relative1.getKey()) {
                    case "father", "mother", "husband", "wife" -> {
                        if (relative1.getKey().equals(relative2.getKey())) {
                            if (!(relative1.getValue1().equals(relative2.getValue1()))) {
                                if (!(relative1.getValue1().contains(" "))) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private void selfCheck(Person p) {
        List<Relative> trash = new ArrayList<>();
        var family = p.family.others;
        for (int i = 0; i < family.size(); i++) {
            var member = family.get(i);
            if (Objects.equals(member.getValue1(), "UNKNOWN")
                || Objects.equals(member.getValue1(), "NONE")) {
                trash.add(member);
            }
            if (member.getValue1().matches(".*\\d.*") && member.getValue1().contains(" ")) {
                trash.add(member);
                for(var newMem : member.getValue1().split(" ")) {
                    p.family.others.add(new Relative(member.getKey(), newMem));
                }
            }
        }
        if (p.id != null) {
            for (var i : family) {
                if (Objects.equals(i.getValue1(), p.id)) {
                    trash.add(i);
                }
            }
        }
        for (var i : trash) {
            p.family.others.remove(i);
        }
        p.family.children.removeIf(i -> Objects.equals(i.name, p.id));
        if (p.gender != null) {
            switch (p.gender) {
                case "male" -> p.setGender("M");
                case "female" -> p.setGender("F");
            }
        }
    }

    private ArrayList<Person> noIdMerge(List<Person> personList) {
        var map = new HashMap<String, Person>();
        for (var p : personList) {
            if (map.containsKey(p.name.toString())) {
                cloneCheck(map.get(p.name.toString()), p);
            }
            else {
                map.put(p.name.toString(), p);
            }
        }
        var list = new ArrayList<Person>(57);
        list.addAll(map.values());
        return list;
    }

    private void noId(Person p) {
        for (var c : p.family.children) {
            var ci = newPeopleList.get(c.name);
            if (ci != null) {
                if (ci.family.others.containsKey("father") && (p.gender == null || p.gender.equals("M"))) {
                    var fId = ci.family.others.get("father");
                    if (!newPeopleList.containsKey(fId) && !fId.contains(" ")) {
                        p.id = fId;
                        newPeopleList.put(fId, p);
                    }
                }
                else {
                    if (ci.family.others.containsKey("mother") && (p.gender == null || p.gender.equals("F"))) {
                        var mId = ci.family.others.get("mother");
                        if (!newPeopleList.containsKey(mId) && !mId.contains(" ")) {
                            p.id = mId;
                            newPeopleList.put(mId, p);
                        }
                    }
                }
            }
        }
    }

    private void wifeCheck(Person i, Person j) {
        if (!(j.family.others.containsKey("husband"))) {
            j.family.others.add(new Relative("husband", i.id));
        }
    }

    private void husbandCheck(Person i, Person j) {
        if (!(j.family.others.containsKey("wife"))) {
            j.family.others.add(new Relative("wife", i.id));
        }
    }

    private void childCheck(Person i, Person j, String role) {
        String parent = "parent";
        if (Objects.equals(i.gender, "M")) {
            parent = "father";
        }
        if (Objects.equals(i.gender, "F")) {
            parent = "mother";
        }
        switch (role) {
            case "son", "daughter", "child" -> j.addMember(parent, i.id);
        }
    }

    private void parentCheck(Person i, Person j, String role) {
        String child = "child";
        if (i.gender != null) {
            switch (i.gender) {
                case "F" -> child = "daughter";
                case "M" -> child = "son";
            }
        }
        switch (role) {
            case "father", "mother", "parent" -> j.addChild(new Child(child, i.id));
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
                        }
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

    private void siblingsAndChildCountCheck(Person p) {
        if (p.family != null && p.family.children != null) {
            if (p.childrenNumber == null) {
                p.childrenNumber = String.valueOf(p.family.children.size());
            }
            else {
                if (p.family.children.size() > Integer.parseInt(p.childrenNumber)) {
                    p.family.children.removeIf(x -> x.name.contains(" "));
                }
            }
            while (p.getCurrentChildren() > Integer.parseInt(p.childrenNumber)) {
                p.family.children.remove(p.family.children.size() - 1);
            }
        }
        if (p.family != null && p.family.others != null) {
            int cnt = 0;
            for (var x : p.family.others) {
                if (Objects.equals(x.getKey(), "sister") ||
                    Objects.equals(x.getKey(), "sibling") ||
                    Objects.equals(x.getKey(), "brother")) {
                    cnt++;
                }
            }
            if (p.siblingNumber == null) {
                p.siblingNumber = String.valueOf(cnt);
            }
            else {
                if (Integer.parseInt(p.siblingNumber) < cnt) {
                    p.family.others.removeIf(x -> (x.getKey().equals("sister") ||
                        x.getKey().equals("brother") ||
                        x.getKey().equals("sibling") ) && x.getValue1().contains(" "));
                }
                while (p.getCurrentSiblings() > Integer.parseInt(p.siblingNumber) && p.family.others.getO("sibling") != null) {
                    p.family.others.remove(p.family.others.getO("sibling"));
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
        return newPeopleList.getOrDefault(id, null);
    }

    public Person findByName(String name) {
        for (var i : newPeopleList.values()) {
            if (Objects.equals(i.name.firstName + ' ' + i.name.familyName, name)) {
                return i;
            }
        }
        return null;
    }

    public void siblingsCheck(Person p, Person p2, String role) {
        String sibling = "sibling";
        if (p.gender != null) {
            switch (p.gender) {
                case "M" -> sibling = "brother";
                case "F" -> sibling = "sister";
            }
        }
        switch (role) {
            case "sibling", "sister", "brother" -> p2.addMember(sibling, p.id);
        }
    }


}
