package com.recetas.service;

import com.recetas.model.Recipe;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;

@Service
public class RecipeService {

    private final RestClient client;

    public RecipeService(RestClient client) { this.client = client; }

    public List<Recipe> findAll() { Recipe[] result = client.get().uri("/recipes").retrieve().body(Recipe[].class);  return result == null ? List.of() : Arrays.asList(result); }

    public Recipe findById(String id) { return client.get().uri("/recipes/{id}", id).retrieve().body(Recipe.class); }

    public Recipe save(Recipe recipe) { if (recipe.getId() == null) return client.post().uri("/recipes").body(recipe).retrieve().body(Recipe.class); return client.put().uri("/recipes/{id}", recipe.getId()).body(recipe).retrieve().body(Recipe.class); }

    public void delete(String id) { client.delete().uri("/recipes/{id}", id).retrieve().toBodilessEntity(); }
}