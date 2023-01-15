package me.tretyakovv.p3_lesson8.controllers;

import me.tretyakovv.p3_lesson8.services.FilesService;
import me.tretyakovv.p3_lesson8.services.IngredientService;
import me.tretyakovv.p3_lesson8.services.RecipeService;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@RestController
@RequestMapping("/files")
public class FilesController {

    private final FilesService filesService;

    private final RecipeService recipeService;

    private final IngredientService ingredientService;

    public FilesController(FilesService filesService, RecipeService recipeService, IngredientService ingredientService) {
        this.filesService = filesService;
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
    }

    public ResponseEntity<InputStreamResource> downloadDataFile(String dataFileName) throws FileNotFoundException {
        File file = filesService.getDataFile(dataFileName);

        if (file.exists()) {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; ")
                    .contentLength(file.length())
                    .body(resource);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    public ResponseEntity<Void> uploadDataFile(MultipartFile file, String dataFileName){

        File dataFile = filesService.getDataFile(dataFileName);
        filesService.cleanDataFile(dataFileName);

        try (FileOutputStream fos = new FileOutputStream(dataFile)){
            IOUtils.copy(file.getInputStream(), fos);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping(value = "/exportRecipe",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InputStreamResource> downloadRecipe() throws FileNotFoundException {
        return downloadDataFile(recipeService.getDataFileName());
    }

    @GetMapping(value = "/exportIngredient",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InputStreamResource> downloadIngredient() throws FileNotFoundException {
        return downloadDataFile(ingredientService.getDataFileName());
    }


    @PostMapping(value = "/importRecipe", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadRecipe(@RequestParam MultipartFile file){
        return uploadDataFile(file, recipeService.getDataFileName());
    }

    @PostMapping(value = "/importIngredient", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadIngredient(@RequestParam MultipartFile file){
        return uploadDataFile(file, ingredientService.getDataFileName());
    }

}
