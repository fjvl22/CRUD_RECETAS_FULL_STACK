package com.recetas.view;

import com.recetas.model.Ingredient;
import com.recetas.model.Recipe;
import com.recetas.model.RecipeIngredient;
import com.recetas.service.IngredientService;
import com.recetas.service.RecipeService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

import java.util.ArrayList;
import java.util.List;

public class RecipeFormDialog extends Dialog {

    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final Runnable onSaved;

    private final Recipe recipe;

    private final TextField name = new TextField("Nombre");
    private final TextArea description = new TextArea("Descripción");

    private final ComboBox<Ingredient> ingredientSelector = new ComboBox<>("Ingredientes");

    private final NumberField quantity = new NumberField("Cantidad");
    private final TextField stepText = new TextField("Descripción del paso");
    private final NumberField stepTime = new NumberField("Tiempo (minutos)");

    private final VerticalLayout ingredientList = new VerticalLayout();
    private final VerticalLayout stepList = new VerticalLayout();

    private final List<RecipeIngredient> ingredients = new ArrayList<>();
    private final List<Recipe.Step> steps = new ArrayList<>();

    public RecipeFormDialog(RecipeService service, IngredientService service2, Recipe recipe, Runnable onSaved) {
        this.recipeService = service;
        this.ingredientService = service2;
        this.recipe = recipe;
        this.onSaved = onSaved;

        setWidth("min(760px, 95vw)");
        setHeaderTitle(recipe == null ? "Crear receta" : "Editar receta");

        name.setRequired(true);
        name.setWidthFull();

        description.setWidthFull();
        description.setMaxLength(1000);

        ingredientSelector.setItemLabelGenerator(ingredient -> ingredient.getName()+" ("+ingredient.getUnit()+")");
        ingredientSelector.setWidthFull();

        quantity.setMin(0.01);
        quantity.setStep(1);
        quantity.setWidth("150px");

        Button addIngredient = new Button("Añadir", event -> addIngredient());

        HorizontalLayout ingredientControls = new HorizontalLayout(ingredientSelector, quantity, addIngredient);
        ingredientControls.setAlignItems(FlexComponent.Alignment.END);
        ingredientControls.setWidthFull();

        ingredientList.setPadding(false);
        ingredientList.setSpacing(false);

        stepText.setWidthFull();
        stepTime.setMin(0);
        stepTime.setStep(1);
        stepTime.setWidth("150px");

        Button addStep = new Button("Añadir paso", event -> addStep());

        HorizontalLayout stepControls = new HorizontalLayout(stepText, stepTime, addStep);
        stepControls.setAlignItems(FlexComponent.Alignment.END);
        stepControls.setWidthFull();

        stepList.setPadding(false);
        stepList.setSpacing(false);

        Button cancel = new Button("Cancelar", event -> close());

        Button save = new Button("Guardar receta", event -> saveRecipe());
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        VerticalLayout content = new VerticalLayout(name, description, new H3("Ingredientes"), ingredientControls, ingredientList, new H3("Elaboración"), stepControls, stepList);

        content.setPadding(false);
        content.setSpacing(true);
        content.setWidthFull();

        add(content);
        getFooter().add(cancel, save);

        loadIngredients();
        loadRecipe();
    }

    private void loadIngredients() { try { ingredientSelector.setItems(ingredientService.findAll()); } catch (Exception exception) { Notification.show("No se pudieron cargar los ingredientes"); } }

    private void loadRecipe() {
        if (recipe == null) return;

        name.setValue(recipe.getName()==null?"":recipe.getName());

        description.setValue(recipe.getDescription()==null?"":recipe.getDescription());

        if (recipe.getIngredients()!=null) ingredients.addAll(recipe.getIngredients());

        if (recipe.getSteps()!=null) steps.addAll(recipe.getSteps());

        renderIngredients();
        renderSteps();
    }

    private void addIngredient() {
        Ingredient selected = ingredientSelector.getValue();
        Double amount = quantity.getValue();

        if (selected == null || amount == null || amount <= 0) {
            Notification.show("Selecciona un ingrediente y una cantidad válida");
            return;
        }

        boolean alreadyAdded = ingredients.stream().anyMatch(item -> item.getIngredient()!=null&&selected.getId()!=null&&selected.getId().equals(item.getIngredient().getId()));

        if (alreadyAdded) {
            Notification.show("Ese ingrediente ya está en la receta");
            return;
        }

        ingredients.add(new RecipeIngredient(selected, amount));

        ingredientSelector.clear();
        quantity.clear();

        renderIngredients();
    }

    private void renderIngredients() {
        ingredientList.removeAll();

        for (RecipeIngredient item : ingredients) {
            Ingredient ingredient = item.getIngredient();

            if (ingredient == null) {
                continue;
            }

            Paragraph label = new Paragraph(
                ingredient.getName() + " - " +
                item.getQuantity() + " " +
                ingredient.getUnit()
            );

            Button remove = new Button("Quitar", event -> {
                ingredients.remove(item);
                renderIngredients();
            });

            HorizontalLayout row = new HorizontalLayout(label, remove);
            row.setAlignItems(FlexComponent.Alignment.CENTER);
            row.setWidthFull();

            ingredientList.add(row);
        }
    }

    private void addStep() {
        String text = stepText.getValue().trim();
        Double time = stepTime.getValue();

        if (text.isBlank()||time==null||time<0) {
            Notification.show("Introduce el texto y un tiempo válido para el paso");
            return;
        }

        steps.add(new Recipe.Step(text, time));

        stepText.clear();
        stepTime.clear();

        renderSteps();
    }

    private void renderSteps() {
        stepList.removeAll();

        for (int a = 0; a < steps.size(); a++) {
            Recipe.Step step = steps.get(a);
            int position = a+1;

            Paragraph label = new Paragraph(position+". "+step.getText()+" . "+step.getTime()+" min");

            Button remove = new Button("Quitar", event -> {
                steps.remove(step);
                renderSteps();
            });

            HorizontalLayout row = new HorizontalLayout(label, remove);
            row.setAlignItems(FlexComponent.Alignment.CENTER);
            row.setWidthFull();

            stepList.add(row);
        }
    }

    private void saveRecipe() {
        if (name.getValue().isBlank()) {
            Notification.show("El nombre es obligatorio");
            return;
        }

        if (steps.isEmpty()) {
            Notification.show("Añade al menos un paso");
            return;
        }

        try {
            Recipe result = recipe == null ? new Recipe() : recipe;

            result.setName(name.getValue().trim());
            result.setDescription(description.getValue().trim());
            result.setIngredients(new ArrayList<>(ingredients));
            result.setSteps(new ArrayList<>(steps));

            double totalTime = steps.stream().mapToDouble(Recipe.Step::getTime).sum();

            result.setPreparationTime(totalTime);

            recipeService.save(result);

            Notification.show("Receta guardada correctamente");
            close();
            onSaved.run();

        } catch (Exception exception) {
            Notification.show("No se pudo guardar la receta");
        }
    }
}