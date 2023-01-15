package me.tretyakovv.p3_lesson8.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.tretyakovv.p3_lesson8.model.Ingredient;
import me.tretyakovv.p3_lesson8.model.Recipe;
import me.tretyakovv.p3_lesson8.services.FilesService;
import me.tretyakovv.p3_lesson8.services.IngredientService;
import me.tretyakovv.p3_lesson8.services.RecipeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Service
public class IngredientServiceImpl implements IngredientService {

    private long lasdId = 0L;

    static HashMap<Long, Ingredient> listIngredient = new HashMap<>();

    @Value("${name.of.file.ingredients}")
    private String dataFileName;

    private RecipeService recipeService;

    private FilesService filesService;

    public IngredientServiceImpl(RecipeService recipeService, FilesService filesService) {
        this.recipeService = recipeService;
        this.filesService = filesService;
    }

    @PostConstruct
    private void init() {
        readFromFile();
    }

    @Override
    public long addIngredient(Recipe recipe, Ingredient ingredient) {
        if (StringUtils.isEmpty(ingredient.getTitle())) {
            return -1L;
        }
        HashMap<Long, Ingredient> recipeIngredients = recipe.getIngredients();
        if (recipeIngredients == null) {
            recipeIngredients = new HashMap<>();
        }
        recipeIngredients.put(lasdId, ingredient);
        recipe.setIngredients(recipeIngredients);
        recipeService.externalSaveToFile();

        listIngredient.put(lasdId, ingredient);
        saveToFile();
        return lasdId++;
    }

    @Override
    public Ingredient getIngredient(long id) {
        for (Recipe recipe : recipeService.getAllRecipe()) {
            if (recipe.getIngredients().containsKey(id)) {
                return recipe.getIngredients().get(id);
            }
        }
        return null;
    }

    @Override
    public List<Ingredient> getAllIngredient() {
        List<Ingredient> listIngredient = new LinkedList<>();

        for (Recipe recipe : recipeService.getAllRecipe()) {
            listIngredient.addAll(recipe.getIngredients().values());
        }
        return listIngredient;
    }

    @Override
    public boolean updateIngredient(long id, Ingredient igredient) {
        for (Recipe recipe : recipeService.getAllRecipe()) {
            if (recipe.getIngredients().containsKey(id)) {
                recipe.getIngredients().put(id, igredient);
                saveToFile();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteIngredient(long id) {
        for (Recipe recipe : recipeService.getAllRecipe()) {
            if (recipe.getIngredients().containsKey(id)) {
                recipe.getIngredients().remove(id);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Ingredient> getAllIngredientByPage(int numberPage) {
        List<Ingredient> listIngredientLimited = new LinkedList<>();

        int onePage = 2;
        int start = onePage * numberPage - onePage;
        int end = onePage * numberPage;

        int counter = 0;

        for (Ingredient ingredient : getAllIngredient()) {
            if (counter >= start && counter < end) {
                listIngredientLimited.add(ingredient);
            }
            counter++;
        }
        return listIngredientLimited;
    }

    @Override
    public String getDataFileName() {
        return dataFileName;
    }

    private void saveToFile() {
        try {
            String json = new ObjectMapper().writeValueAsString(listIngredient);
            filesService.saveToFile(json, dataFileName);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    private void readFromFile() {
        String json = filesService.readFromFile(dataFileName);
        try {
            listIngredient = new ObjectMapper().readValue(json, new TypeReference<HashMap<Long, Ingredient>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        for (Long aLong : listIngredient.keySet()) {
            if (lasdId < aLong) {
                lasdId = aLong;
            }
        }
        if (lasdId > 0L) {
            lasdId++;
        }
    }
}

