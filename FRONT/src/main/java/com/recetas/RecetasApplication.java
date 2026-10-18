package com.recetas;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Theme("recetas")
public class RecetasApplication implements AppShellConfigurator { public static void main(String[] args) { SpringApplication.run(RecetasApplication.class, args); } }