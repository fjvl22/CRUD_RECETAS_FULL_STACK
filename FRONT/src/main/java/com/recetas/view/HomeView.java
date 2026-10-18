package com.recetas.view;

import com.recetas.layout.MainLayout;
import com.recetas.service.RecipeService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Inicio | Sazón")
public class HomeView extends VerticalLayout {

    public HomeView(RecipeService recipeService) {
        addClassName("page-content");
        setSizeFull();
        setPadding(true);
        setSpacing(true);

        H1 title = new H1("Tu cocina, tu inspiración.");
        title.addClassName("hero-title");

        Paragraph description = new Paragraph("Organiza tus recetas favoritas, descubre nuevos sabores y ten todos tus ingredientes a mano.");
        description.addClassName("hero-description");

        Button recipesButton = new Button("Explorar recetas", VaadinIcon.ARROW_RIGHT.create());

        recipesButton.addClassName("primary-button");
        recipesButton.addClickListener(event -> getUI().ifPresent(ui -> ui.navigate(RecipesView.class)));

        VerticalLayout heroText = new VerticalLayout(new Paragraph("BIENVENIDO A SAZÓN"), title, description, recipesButton);

        heroText.setPadding(false);
        heroText.setSpacing(true);

        Div hero = new Div(heroText);
        hero.addClassName("hero-panel");

        int recipeCount = recipeService.findAll().size();

        Div recipesStat = createStat("RECETAS GUARDADAS", String.valueOf(recipeCount), VaadinIcon.BOOK);

        Div ingredientsStat = createStat("A TU MANERA", "100%", VaadinIcon.HEART);

        HorizontalLayout stats = new HorizontalLayout(recipesStat, ingredientsStat);

        stats.addClassName("stats-grid");
        stats.setWidthFull();

        H3 sectionTitle = new H3("Empieza por aquí");

        Paragraph sectionDescription = new Paragraph("Gestiona tu colección culinaria desde un único lugar.");

        add(hero, stats, sectionTitle, sectionDescription);
    }

    private Div createStat(String label, String value, VaadinIcon icon) {
        Div card = new Div();
        card.addClassName("stat-card");

        Div iconContainer = new Div(icon.create());
        iconContainer.addClassName("stat-icon");

        Paragraph labelText = new Paragraph(label);
        labelText.addClassName("stat-label");

        H3 valueText = new H3(value);
        valueText.addClassName("stat-value");

        card.add(iconContainer, labelText, valueText);

        return card;
    }
}