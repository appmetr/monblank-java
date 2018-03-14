package com.appmetr.monblank;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MonitorKey {

    private final String name;
    private final Map<String, String> properties;
    private final int precalcHash;

    public MonitorKey(String name) {
        this.name = name;
        this.properties = new HashMap<>();
        precalcHash = precalcHash();
    }

    public MonitorKey(String name, Map<String, String> properties) {
        this.name = name;
        this.properties = Collections.unmodifiableMap(properties == null ? new HashMap<>() : properties);
        precalcHash = precalcHash();
    }

    public String getName() {
        return name;
    }

    public String get(String name) {
        return properties.get(name);
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MonitorKey that = (MonitorKey) o;

        if (!name.equals(that.name)) return false;
        if (!properties.equals(that.properties)) return false;

        return true;
    }

    private int precalcHash() {
        int result = name.hashCode();
        return 31 * result + properties.hashCode();
    }

    @Override
    public int hashCode() {
        return precalcHash;
    }

    @Override public String toString() {
        return "MonitorKey{" +
                "name='" + name + '\'' +
                ", precalcHash=" + precalcHash +
                ", properties=" + properties +
                '}';
    }
}
