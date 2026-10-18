package com.recetas.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RecipeIngredient {

    @JsonProperty("_id")
    private String id;

    private Ingredient ingredient;
    private double quantity;

    public RecipeIngredient() {}

    public RecipeIngredient(Ingredient ingredient, double quantity) {
        this.ingredient = ingredient;
        this.quantity = quantity;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public Ingredient getIngredient() { return ingredient; }

    public void setIngredient(Ingredient ingredient) { this.ingredient = ingredient; }

    public double getQuantity() { return quantity; }

    public void setQuantity(double quantity) { this.quantity = quantity; }
}