package com.recetas.view;

import com.recetas.layout.MainLayout;
import com.recetas.model.Ingredient;
import com.recetas.service.IngredientService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "Ingredients", layout = MainLayout.class)
@PageTitle("Ingredientes | Sazón")
public class IngredientsView extends VerticalLayout {

    private final IngredientService service;
    private final Grid<Ingredient> grid = new Grid<>(Ingredient.class, false);

    public IngredientsView(IngredientService service) {
        this.service = service;

        addClassName("page-content");
        setPadding(true);
        setSpacing(true);
        setWidthFull();

        H1 title = new H1("Ingredientes");
        title.addClassName("page-title");

        Paragraph subtitle = new Paragraph("Administra los ingredientes que utilizas en tus recetas.");

        Button add = new Button("Nuevo ingrediente", VaadinIcon.PLUS.create());

        add.addClassName("primary-button");
        add.addClickListener(event -> openForm(null));

        HorizontalLayout toolbar = new HorizontalLayout(subtitle, add);
        toolbar.setWidthFull();
        toolbar.setAlignItems(FlexComponent.Alignment.CENTER);
        toolbar.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        grid.addColumn(Ingredient::getName).setHeader("Nombre").setFlexGrow(2);

        grid.addColumn(Ingredient::getUnit).setHeader("Unidad").setFlexGrow(1);

        grid.addColumn(Ingredient::getDescription).setHeader("Descripción").setFlexGrow(3);

        grid.addComponentColumn(ingredient -> {
            Button edit = new Button(VaadinIcon.EDIT.create(), event -> openForm(ingredient));
            edit.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

            Button delete = new Button(VaadinIcon.TRASH.create(), event -> confirmDelete(ingredient));
            delete.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);

            return new HorizontalLayout(edit, delete);
        }).setHeader("Acciones");

        grid.addClassName("ingredient-grid");
        grid.setWidthFull();

        add(title, toolbar, grid);

        refresh();
    }

    private void refresh() {
        try {
            grid.setItems(service.findAll());
        } catch (Exception exception) {
            Notification.show("No se pudieron cargar los ingredientes");
        }
    }

    private void openForm(Ingredient ingredient) {
        Dialog dialog = new Dialog();

        dialog.setHeaderTitle(ingredient == null ? "Nuevo ingrediente" : "Editar ingrediente");
        dialog.setWidth("min(500px, 95vw)");

        TextField name = new TextField("Nombre");
        name.setRequired(true);
        name.setWidthFull();

        TextField unit = new TextField("Unidad de medida");
        unit.setPlaceholder("g, ml, unidades...");
        unit.setRequired(true);
        unit.setWidthFull();

        TextArea description = new TextArea("Descripción");
        description.setWidthFull();

        if (ingredient != null) {
            name.setValue(ingredient.getName()==null?"":ingredient.getName());

            description.setValue(ingredient.getDescription()==null?"":ingredient.getDescription());
        }

        FormLayout form = new FormLayout(name, unit, description);

        Button cancel = new Button("Cancelar", event -> dialog.close());

        Button save = new Button("Guardar", event -> {
            if (name.getValue().isBlank()||unit.getValue().isBlank()) {
                Notification.show("Nombre y unidad son obligatorios.");
                return;
            }

            Ingredient result = ingredient == null ? new Ingredient() : ingredient;

            result.setName(name.getValue().trim());
            result.setUnit(unit.getValue().trim());
            result.setDescription(description.getValue().trim());

            try {
                service.save(result);
                dialog.close();
                refresh();
                Notification.show("Ingrediente guardado");
            } catch (Exception exception) {
                Notification.show("No se pudo guardar el ingrediente");
            }
        });

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        dialog.add(form);
        dialog.getFooter().add(cancel, save);
        dialog.open();
    }

    private void confirmDelete(Ingredient ingredient) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Eliminar ingrediente");

        dialog.add(new Paragraph("¿Quieres eliminar el ingrediente "+ingredient.getName()+"?"));

        Button cancel = new Button("Cancelar", event -> dialog.close());

        Button delete = new Button("Eliminar", event -> {
            try {
                service.delete(ingredient.getId());
                dialog.close();
                refresh();
                Notification.show("Ingrediente eliminado");
            } catch (Exception exception) {
                Notification.show("No se pudo eliminar el ingrediente. Comprueba si está siendo utilizado.");
            }
        });

        delete.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);

        dialog.getFooter().add(cancel, delete);
        dialog.open();
    }
}