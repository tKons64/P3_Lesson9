package me.tretyakovv.p3_lesson8.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Recipe {

    private String title;

    private int preparationTime;

    private HashMap<Long, Ingredient> ingredients;

    private List<String> steps;

}
