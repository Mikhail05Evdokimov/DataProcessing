package ru.nsu.evdokimov.data;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

public class Relative {

    @XmlAttribute(name = "role")
    private final String key;
    @XmlValue
    private final String value;

    public Relative(String k, String v) {
        key = k;
        value = v;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
