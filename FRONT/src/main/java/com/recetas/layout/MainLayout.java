package com.recetas.layout;

import com.recetas.view.HomeView;
import com.recetas.view.IngredientsView;
import com.recetas.view.RecipesView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;

public class MainLayout extends AppLayout {

    public MainLayout() {
        addClassName("main-layout");
        setPrimarySection(Section.DRAWER);

        DrawerToggle toggle = new DrawerToggle();

        H2 title = new H2("Sazón");
        title.addClassName("brand-title");

        Span subtitle = new Span("TU RECETARIO PERSONAL");
        subtitle.addClassName("brand-subtitle");

        VerticalLayout brand = new VerticalLayout(title, subtitle);
        brand.setPadding(false);
        brand.setSpacing(false);

        HorizontalLayout header = new HorizontalLayout(toggle, brand);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.addClassName("top-header");
        header.setWidthFull();

        addToNavbar(header);

        VerticalLayout menu = new VerticalLayout(
            createLink("Inicio", VaadinIcon.HOME, HomeView.class),
            createLink("Mis recetas", VaadinIcon.BOOK, RecipesView.class),
            createLink("Ingredientes", VaadinIcon.CUBES, IngredientsView.class)
        );

        menu.addClassName("side-menu");
        menu.setPadding(true);
        menu.setSpacing(true);
        menu.setWidthFull();

        addToDrawer(menu);
    }

    private RouterLink createLink(String text, VaadinIcon icon, Class<? extends Component> view) {

        RouterLink link = new RouterLink();
        link.addClassName("menu-link");

        Span iconSpan = new Span(icon.create());
        iconSpan.addClassName("menu-icon");

        Span label = new Span(text);

        link.add(iconSpan, label);
        link.setRoute(view);

        return link;
    }
}