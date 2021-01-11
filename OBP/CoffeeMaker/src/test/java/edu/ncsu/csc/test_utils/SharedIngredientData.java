package edu.ncsu.csc.test_utils;

import java.util.List;
import java.util.Vector;

import edu.ncsu.csc.coffee_maker.models.persistent.Ingredient;

/**
 * Shared information about ingredients for Cucumber tests.
 *
 * @author Alex Snezhko
 * @author Dominic Brown
 * @author Selena Chen
 */
public class SharedIngredientData {

    /** Last ingredient added. */
    public boolean          latestIngredientAdded;

    /** Current ingredient. */
    public Ingredient       currentIngredient;

    /** List of ingredients. */
    public List<Ingredient> currentIngredientList;

    /** Error messages. */
    public String           ingredientError;

    /**
     * Initializes the shared data.
     */
    public SharedIngredientData () {
        latestIngredientAdded = false;
        currentIngredient = new Ingredient();
        currentIngredientList = new Vector<Ingredient>();
        ingredientError = "";
    }
}
