package com.recetas.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

public class Recipe {

    @JsonProperty("_id")
    private String id;

    private String name;
    private String description;
    private double preparationTime;

    private List<RecipeIngredient> ingredients = new ArrayList<>();
    private List<Step> steps = new ArrayList<>();

    public Recipe() {}

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public double getPreparationTime() { return preparationTime; }

    public void setPreparationTime(double preparationTime) { this.preparationTime = preparationTime; }

    public List<RecipeIngredient> getIngredients() { return ingredients; }

    public void setIngredients(List<RecipeIngredient> ingredients) { this.ingredients = ingredients; }

    public List<Step> getSteps() { return steps; }

    public void setSteps(List<Step> steps) { this.steps = steps; }

    public static class Step {

        @JsonProperty("_id")
        private String id;

        private String text;
        private double time;

        public Step() {}

        public Step(String text, double time) {
            this.text = text;
            this.time = time;
        }

        public String getId() { return id; }

        public void setId(String id) { this.id = id; }

        public String getText() { return text; }

        public void setText(String text) { this.text = text; }

        public double getTime() { return time; }

        public void setTime(double time) { this.time = time; }
    }
}