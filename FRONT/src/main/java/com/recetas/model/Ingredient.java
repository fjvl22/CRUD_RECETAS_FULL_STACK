package com.recetas.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Ingredient {

    @JsonProperty("_id")
    private String id;

    private String name;
    private String unit;
    private String description;

    public Ingredient() {}

    public Ingredient(String id, String name, String unit, String description) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.description = description;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getUnit() { return unit; }

    public void setUnit(String unit) { this.unit = unit; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() { return name; }
}