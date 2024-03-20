package ru.nsu.evdokimov.data;

import java.util.*;

public class Others extends ArrayList<Relative> {

    public String get(String key) {
        for (var i : this) {
            if (Objects.equals(i.getKey(), key)) {
                return i.getValue();
            }
        }
        return null;
    }

    public void put(String key, String value) {
        this.add(new Relative(key, value));
    }

    public Boolean containsKey(String key) {
        for (var i : this) {
            if (Objects.equals(i.getKey(), key)) {
                return true;
            }
        }
        return false;
    }

    public List<String> keySet() {
        List<String> keys = new ArrayList<>();
        for (var i : this) {
            keys.add(i.getKey());
        }
        return keys;
    }

    public void removeByKey(String key) {
        for (var i : this) {
            if (Objects.equals(i.getKey(), key)) {
                this.remove(i);
                break;
            }
        }
    }
}
