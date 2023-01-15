package me.tretyakovv.p3_lesson9.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ingredient {

    private String title;

    private int quantity;

    private String measureUnit;

    @Override
    public String toString() {
        return title + " - " +
                quantity + " " +
                measureUnit ;
    }
}
