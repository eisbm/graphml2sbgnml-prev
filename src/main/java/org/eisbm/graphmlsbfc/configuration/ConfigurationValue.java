package org.eisbm.graphmlsbfc.configuration;

public class ConfigurationValue {
    ValueType type;
    Object value;
    String mappedLocation;

    public ConfigurationValue(ValueType type, Object value, String mappedLocation) {
        this.type = type;
        this.value = value;
        this.mappedLocation = mappedLocation;
    }

    public ValueType getType() {
        return type;
    }

    public void setType(ValueType type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getMappedLocation() {
        return mappedLocation;
    }

    public void setMappedLocation(String mappedLocation) {
        this.mappedLocation = mappedLocation;
    }
}
