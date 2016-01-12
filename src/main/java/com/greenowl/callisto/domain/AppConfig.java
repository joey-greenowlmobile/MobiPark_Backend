package com.greenowl.callisto.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "T_APPLICATION_CONFIG")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Access(AccessType.PROPERTY)
    private Long id;

    @NotNull
    @Size(min = 5, max = 250)
    @Column(unique = true, nullable = false)
    private String key;

    @Size(min = 5, max = 250)
    @Column(unique = true, nullable = false)
    private String value;

    private String type;

    public AppConfig() {
    }

    public AppConfig(String key, String value, String type) {
        this.key = key;
        this.value = value;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
    public String toString() {
        return "AppConfig{" +
                "id=" + id +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
