package com.recetas.service;

import com.recetas.model.Ingredient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;

@Service
public class IngredientService {

    private final RestClient client;

    public IngredientService(RestClient client) { this.client = client; }

    public List<Ingredient> findAll() { Ingredient[] result = client.get().uri("/ingredients").retrieve().body(Ingredient[].class);  return result == null ? List.of() : Arrays.asList(result); }

    public Ingredient save(Ingredient ingredient) { if (ingredient.getId() == null) return client.post().uri("/ingredients").body(ingredient).retrieve().body(Ingredient.class); return client.put().uri("/ingredients/{id}", ingredient.getId()).body(ingredient).retrieve().body(Ingredient.class); }

    public void delete(String id) { client.delete().uri("/ingredients/{id}", id).retrieve().toBodilessEntity(); }
}