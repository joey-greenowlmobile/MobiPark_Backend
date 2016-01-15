package com.greenowl.callisto.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "T_APPLICATION_CONFIG")
public class AppConfig extends AbstractAuditingEntity implements Serializable {

    @Id
    @Column(name = "key_name", nullable = false, unique = true)
    private String key;

    @Column(name = "value", nullable = false)
    private String value;

    @Column(name = "type", nullable = false)
    private String type;

    public AppConfig() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AppConfig appConfig = (AppConfig) o;

        return key.equals(appConfig.key);

    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @Override
    public String toString() {
        return "AppConfig{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
