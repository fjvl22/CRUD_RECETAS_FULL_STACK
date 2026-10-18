package com.recetas.view;

import com.recetas.layout.MainLayout;
import com.recetas.model.Recipe;
import com.recetas.service.IngredientService;
import com.recetas.service.RecipeService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;
import java.util.Locale;

@Route(value ="recipes", layout = MainLayout.class)
@PageTitle("Mis recetas | Sazón")
public class RecipesView extends VerticalLayout {

    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final Div recipeGrid = new Div();

    public RecipesView(RecipeService recipeService, IngredientService ingredientService) {

        this.recipeService = recipeService;
        this.ingredientService = ingredientService;

        addClassName("page-content");
        setPadding(true);
        setSpacing(true);
        setWidthFull();

        H1 title = new H1("Mis recetas");
        title.addClassName("page-title");

        Paragraph subtitle = new Paragraph("Todos tus platos, organizados en un solo lugar.");
        subtitle.addClassName("page-subtitle");

        TextField search = new TextField();
        search.setPlaceholder("Buscar una receta...");
        search.setPrefixComponent(VaadinIcon.SEARCH.create());
        search.setClearButtonVisible(true);
        search.setWidth("min(100%, 420px)");

        Button addButton = new Button("Nueva receta", VaadinIcon.PLUS.create());

        addButton.addClassName("primary-button");
        addButton.addClickListener(event -> openForm(null));

        HorizontalLayout toolbar = new HorizontalLayout(search, addButton);
        toolbar.setWidthFull();
        toolbar.setAlignItems(FlexComponent.Alignment.END);
        toolbar.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        toolbar.addClassName("recipes-toolbar");

        recipeGrid.addClassName("recipe-grid");
        recipeGrid.setWidthFull();

        search.addValueChangeListener(event -> renderRecipes(event.getValue()));

        add(title, subtitle, toolbar, recipeGrid);
        renderRecipes("");
    }

    private void renderRecipes(String searchText) {
        recipeGrid.removeAll();

        String query = searchText == null ? "" : searchText.toLowerCase(Locale.ROOT);

        List<Recipe> recipes = recipeService.findAll().stream().filter(recipe -> recipe.getName()!=null&&recipe.getName().toLowerCase(Locale.ROOT).contains(query)).toList();

        if (recipes.isEmpty()) {
            Div empty = new Div();
            empty.addClassName("empty-state");

            H3 emptyTitle = new H3("Todavía no hay recetas");
            Paragraph emptyText = new Paragraph("Crea tu primera receta y empieza tu colección.");

            empty.add(VaadinIcon.CUTLERY.create(), emptyTitle, emptyText);
            recipeGrid.add(empty);
            return;
        }

        for (Recipe r : recipes) recipeGrid.add(createRecipeCard(r));
    }
    
    private Div createRecipeCard(Recipe recipe) {
        Div card = new Div();
        card.addClassName("recipe-card");

        Div cover = new Div();
        cover.addClassName("recipe-cover");
        cover.add(VaadinIcon.CUTLERY.create());

        Div details = new Div();
        details.addClassName("recipe-details");

        H3 name = new H3(recipe.getName());
        name.addClassName("recipe-name");

        Paragraph description = new Paragraph(recipe.getDescription() == null ? "" : recipe.getDescription());
        description.addClassName("recipe-description");

        Paragraph time = new Paragraph("◷  "+recipe.getPreparationTime()+" min");
        time.addClassName("recipe-time");

        Button editButton = new Button(VaadinIcon.EDIT.create(), event -> openForm(recipe));
        editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        Button deleteButton = new Button(VaadinIcon.TRASH.create(), event -> confirmDelete(recipe));
        deleteButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);

        HorizontalLayout actions = new HorizontalLayout(time, editButton, deleteButton);

        actions.setAlignItems(FlexComponent.Alignment.CENTER);
        actions.addClassName("recipe-actions");

        details.add(name, description, actions);
        card.add(cover, details);

        return card;
    }

    private void openForm(Recipe recipe) {
        RecipeFormDialog dialog = new RecipeFormDialog(recipeService, ingredientService, recipe, this::refreshRecipes);

        dialog.open();
    }

    private void confirmDelete(Recipe recipe) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Eliminar receta");

        Paragraph message = new Paragraph("¿Seguro que quieres eliminar la receta <<"+recipe.getName()+">>?");

        Button cancel = new Button("Cancelar", event -> dialog.close());

        Button delete = new Button("Eliminar", event -> {
            try {
                recipeService.delete(recipe.getId());
                dialog.close();
                refreshRecipes();
                Notification.show("Receta eliminada");
            } catch (Exception exception) {
                Notification.show("No se pudo eliminar la receta");
            }
        });

        delete.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);

        dialog.add(message);
        dialog.getFooter().add(cancel, delete);
        dialog.open();
    }

    private void refreshRecipes() { renderRecipes(""); }
}