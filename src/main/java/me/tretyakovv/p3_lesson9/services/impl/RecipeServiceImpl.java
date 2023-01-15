package me.tretyakovv.p3_lesson8.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.tretyakovv.p3_lesson8.model.Ingredient;
import me.tretyakovv.p3_lesson8.model.Recipe;
import me.tretyakovv.p3_lesson8.services.FilesService;
import me.tretyakovv.p3_lesson8.services.RecipeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Service
public class RecipeServiceImpl implements RecipeService {

    private long lasdId = 0L;

    static HashMap<Long, Recipe> listRecipe = new HashMap<>();

    @Value("${name.of.file.recipes}")
    private String dataFileName;

    private FilesService filesService;

    public RecipeServiceImpl(FilesService filesService) {
        this.filesService = filesService;
    }

    @PostConstruct
    private void init() {
        readFromFile();
    }

    @Override
    public long addRecipe(Recipe recipe) {
        if (recipe.getIngredients() == null) {
            recipe.setIngredients(new HashMap<Long, Ingredient>());
        }
        if (StringUtils.isEmpty(recipe.getTitle())) {
            return -1L;
        }
        listRecipe.put(lasdId, recipe);
        saveToFile();
        return lasdId++;
    }

    @Override
    public Recipe getRecipe(long id) {
        return listRecipe.get(id);
    }

    @Override
    public HashMap<Long, Recipe> getRecipes() {
        return listRecipe;
    }
    @Override
    public Collection<Recipe> getAllRecipe() {
        return listRecipe.values();
    }

    @Override
    public boolean updateRecipe(long id, Recipe recipe) {
        if (listRecipe.containsKey(id)) {
            listRecipe.put(id, recipe);
            saveToFile();
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteRecipe(long id) {
        if (listRecipe.containsKey(id)) {
            listRecipe.remove(id);
            return true;
        }
        return false;
    }

    @Override
    public Recipe findRecipeByIngrediantId(long idIngrediant) {
        for (Recipe recipe : listRecipe.values()) {
            if (recipe.getIngredients().containsKey(idIngrediant)) {
                return recipe;
            }
        }
        return null;
    }

    @Override
    public List<Recipe> findRecipesByIngrediants(long[] arrIngredientsId) {
        HashMap<Long, Ingredient> listIngredients = new HashMap<>();
        List<Recipe> listRecipes = new LinkedList<>();
        boolean recipeFits = false;

        for (Recipe recipe : listRecipe.values()) {
            listIngredients = recipe.getIngredients();
            for (long idIngredient : arrIngredientsId) {
                recipeFits = listIngredients.containsKey(idIngredient);
            }
            if (recipeFits) {
                listRecipes.add(recipe);
            }
        }
        return listRecipes;
    }

    @Override
    public void externalSaveToFile() {
        saveToFile();
    }

    @Override
    public String getDataFileName() {
        return dataFileName;
    }

    private void saveToFile() {
        try {
            String json = new ObjectMapper().writeValueAsString(listRecipe);
            filesService.saveToFile(json, dataFileName);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    private void readFromFile() {
        String json = filesService.readFromFile(dataFileName);
        try {
            listRecipe = new ObjectMapper().readValue(json, new TypeReference<HashMap<Long, Recipe>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        for (Long aLong : listRecipe.keySet()) {
            if (lasdId < aLong) {
                lasdId = aLong;
            }
        }
        if (lasdId > 0L) {
            lasdId++;
        }
    }
}



