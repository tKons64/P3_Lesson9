package me.tretyakovv.p3_lesson8.services;

import me.tretyakovv.p3_lesson8.model.Recipe;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public interface RecipeService {

    long addRecipe(Recipe recipe);

    Recipe getRecipe(long id);

    HashMap<Long, Recipe> getRecipes();

    Collection<Recipe> getAllRecipe();

    boolean updateRecipe(long id, Recipe recipe);

    boolean deleteRecipe(long id);

    Recipe findRecipeByIngrediantId(long idIngrediant);

    List<Recipe> findRecipesByIngrediants(long[] arrIngredientsId);

    void externalSaveToFile();

    String getDataFileName();
}
