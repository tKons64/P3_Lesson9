package me.tretyakovv.p3_lesson8.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.tretyakovv.p3_lesson8.model.Recipe;
import me.tretyakovv.p3_lesson8.services.RecipeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recipe")
@Tag(name = "Рецепты", description = "Методы работы с рецептами")
public class RecipeController {

    private RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping
    @Operation( summary = "Добавить рецепт")
    public ResponseEntity<Long> addRecipe(@RequestBody Recipe recipe) {
        long id = recipeService.addRecipe(recipe) ;
        return ResponseEntity.ok(id);
    }

    @GetMapping("/{id}")
    @Operation( summary = "Найти рецепт")
    @Parameters(value = {
            @Parameter(name = "id", description = "ID рецепта")
    })
    public ResponseEntity<Recipe> getRecipe(@PathVariable long id) {
        Recipe recipe = recipeService.getRecipe(id);
        if (recipe == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(recipe);
    }

    @GetMapping("/all")
    @Operation( summary = "Вывести все рецепты")
    public ResponseEntity<?> getAllRecipe() {
        return ResponseEntity.ok(recipeService.getAllRecipe());
    }

    @PutMapping()
    @Operation( summary = "Обновить рецепт")
    @Parameters(value = {
            @Parameter(name = "id", description = "ID рецепта")
    })
    public ResponseEntity<Boolean> updateRecipe(@RequestParam long id, @RequestBody Recipe recipe) {
        if (recipeService.updateRecipe(id, recipe)) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.internalServerError().build();
    }

    @DeleteMapping
    @Operation( summary = "Удалить рецепт")
    @Parameters(value = {
            @Parameter(name = "id", description = "ID рецепта")
    })
    public ResponseEntity<Boolean> deleteIngredient(@RequestParam long id) {
        if (recipeService.deleteRecipe(id)) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.internalServerError().build();
    }

    @GetMapping("/find/byIngredient")
    @Operation( summary = "Найти рецепт по ID ингридиента")
    @Parameters(value = {
            @Parameter(name = "idIngrediant", description = "ID ингридиента")
    })
    public ResponseEntity<Recipe> findRecipeByIngrediantId(@RequestParam long idIngrediant) {
        Recipe recipe = recipeService.findRecipeByIngrediantId(idIngrediant);
        if (recipe == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(recipe);
    }

    @GetMapping("/find/byIngredients")
    @Parameters(value = {
            @Parameter(name = "idIngrediants", description = "Масссив ID ингридиентов", example = "[2,4,7]")
    })
    @Operation( summary = "Найти рецепты по нескольким ингридиентам")
    public ResponseEntity<?> findRecipesByIngrediantsId(@RequestParam long[] idIngrediants) {
        return ResponseEntity.ok(recipeService.findRecipesByIngrediants(idIngrediants));
    }

}
