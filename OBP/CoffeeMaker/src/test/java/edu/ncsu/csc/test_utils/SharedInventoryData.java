package edu.ncsu.csc.test_utils;

import java.util.ArrayList;

import edu.ncsu.csc.coffee_maker.models.persistent.Ingredient;
import edu.ncsu.csc.coffee_maker.models.persistent.IngredientType;

/**
 * Shared information about the inventory for Cucumber tests.
 *
 * @author Kai Presler-Marshall
 * @author Elizabeth Gilbert
 * @author Alex Snezhko
 * @author Dominic Brown
 * @author Selena Chen
 */
public class SharedInventoryData {

    /** New inventory values. */
    public ArrayList<Ingredient> newVals;

    /** Original inventory values. */
    public ArrayList<Ingredient> origVals;

    /** Error messages. */
    public String                errorMessage;

    /**
     * Initializes the shared data.
     */
    public SharedInventoryData () {
        newVals = new ArrayList<Ingredient>();
        origVals = new ArrayList<Ingredient>();
        newVals.add( new Ingredient( new IngredientType( "Coffee" ), 0 ) );
        newVals.add( new Ingredient( new IngredientType( "Water" ), 0 ) );
        newVals.add( new Ingredient( new IngredientType( "Chai" ), 0 ) );
        origVals.add( new Ingredient( new IngredientType( "Coffee" ), 0 ) );
        origVals.add( new Ingredient( new IngredientType( "Water" ), 0 ) );
        origVals.add( new Ingredient( new IngredientType( "Chai" ), 0 ) );
        errorMessage = "";
    }
}
