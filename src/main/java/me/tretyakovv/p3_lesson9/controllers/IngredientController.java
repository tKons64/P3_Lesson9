package me.tretyakovv.p3_lesson8.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.tretyakovv.p3_lesson8.model.Ingredient;
import me.tretyakovv.p3_lesson8.model.Recipe;
import me.tretyakovv.p3_lesson8.services.IngredientService;
import me.tretyakovv.p3_lesson8.services.RecipeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ingredient")
@Tag(name = "Ингридиенты", description = "Методы работы с ингридиентами")
public class IngredientController {

    private final IngredientService ingredientService;
    private final RecipeService recipeService;

    public IngredientController(IngredientService ingredientService, RecipeService recipeService) {
        this.ingredientService = ingredientService;
        this.recipeService = recipeService;
    }

    @PostMapping
    @Operation( summary = "Добавить ингридиент в рецепт")
    @Parameters(value = {
            @Parameter(name = "idRecipe", description = "ID рецепта")
    })
    public ResponseEntity<Long> addIngredient(@RequestParam long idRecipe, @RequestBody Ingredient ingredient) {
        Recipe recipe = recipeService.getRecipe(idRecipe);
        if (recipe == null){
            return ResponseEntity.notFound().build();
        }
        long id = ingredientService.addIngredient(recipe, ingredient);
        return ResponseEntity.ok(id);
    }

    @GetMapping("/{id}")
    @Operation( summary = "Найти ингридиент")
    @Parameters(value = {
            @Parameter(name = "id", description = "ID ингридиента")
    })
    public ResponseEntity<Ingredient> getIngredient(@PathVariable long id) {
        Ingredient ingredient = ingredientService.getIngredient(id);
        if (ingredient == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ingredient);
    }

    @GetMapping("/all")
    @Operation( summary = "Вывести все ингридиенты")
    public ResponseEntity<?> getAllIngredient() {
        //List<Ingredient> ingredientList = (List<Ingredient>) ingredientService.getAllIngredient();
        return ResponseEntity.ok(ingredientService.getAllIngredient());
    }

    @PutMapping()
    @Operation( summary = "Обновить ингридиент")
    @Parameters(value = {
            @Parameter(name = "id", description = "ID ингридиента")
    })
    public ResponseEntity<Boolean> updateIngredient(@RequestParam long id, @RequestBody Ingredient ingredient) {
        return ResponseEntity.ok(ingredientService.updateIngredient(id, ingredient));
    }

    @DeleteMapping
    @Operation( summary = "Удалить ингридиент")
    @Parameters(value = {
            @Parameter(name = "id", description = "ID ингридиента")
    })
    public ResponseEntity<Boolean> deleteIngredient(@RequestParam long id) {
       return ResponseEntity.ok(ingredientService.deleteIngredient(id));
    }

    @GetMapping("/page")
    @Parameters(value = {
            @Parameter(name = "numberPage", description = "Номер страницы коотрую необходимо вывести")
    })
    @Operation( summary = "Вевести все ингридиенты постранично (2 шт. на страницу)")
    public ResponseEntity<?> getAllIngredientByPage(@RequestParam int numberPage) {
        return ResponseEntity.ok(ingredientService.getAllIngredientByPage(numberPage));
    }
}
