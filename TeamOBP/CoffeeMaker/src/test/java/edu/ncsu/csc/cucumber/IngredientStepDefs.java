package edu.ncsu.csc.cucumber;

import org.junit.Assert;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.coffee_maker.models.persistent.DomainObject;
import edu.ncsu.csc.coffee_maker.models.persistent.Ingredient;
import edu.ncsu.csc.coffee_maker.models.persistent.IngredientType;
import edu.ncsu.csc.test_utils.SharedIngredientData;

/**
 * Step Defs (Cucumber) for interacting with the Ingredient model to ensure it
 * is appropriately tested.
 *
 * @author Alex Snezhko
 * @author Dominic Brown
 * @author Selena Chen
 */
public class IngredientStepDefs {

    /**
     * Shared information about the ingredients.
     */
    private final SharedIngredientData ingredientData;

    /**
     * Constructor for the IngredientStepDefs. Used to keep track of
     * CoffeeMaker's state to ensure that the test completed successfully.
     *
     * @param sid
     *            SharedIngredientData; A backup of the ingredients to ensure
     *            that the CoffeeMaker is behaving appropriately.
     */
    public IngredientStepDefs ( final SharedIngredientData sid ) {
        this.ingredientData = sid;
    }

    /**
     * Deletes the existing batch of ingredients in the CoffeeMaker and then
     * populates it with a new batch of ingredients.
     *
     * @param numIngredients
     *            The number of ingredients that should be in the CoffeeMaker
     * @throws Exception
     *             If the number of ingredients to add is greater than the max
     *             allowed
     */
    @Given ( "^the CoffeeMaker already has (\\d+) ingredients$" )
    public void existingIngredients ( final int numIngredients ) throws Exception {
        // Check current number of ingredients
        final int total = Ingredient.getAll().size();
        if ( total != 0 ) {
            DomainObject.deleteAll( Ingredient.class );
            Assert.assertEquals( 0, Ingredient.getAll().size() );
        }

        if ( numIngredients == 0 ) {
            return; // no need to execute the rest of the code
        }
        else {
            for ( int i = 0; i < numIngredients; i++ ) {
                try {
                    final Ingredient testI = new Ingredient();
                    try {
                        testI.setIngredient( new IngredientType( "Ingredient" + i ) );
                        final int am = i * 10;
                        testI.setAmount( am );
                    }
                    catch ( final Exception e ) {
                        Assert.fail( "Error in creating ingredients" );
                    }
                    testI.save();
                    ingredientData.latestIngredientAdded = Ingredient.getAll().contains( testI );

                }
                catch ( final Exception e ) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Create an ingredient from the valid parameters specified.
     *
     * @param name
     *            Name of the new Ingredient
     * @param amount
     *            Amount of the new Ingredient
     */
    @When ( "^I create a valid ingredient with name: (.+), and initial amount: (\\d+)$" )
    public void addValidIngredient ( final String name, final int amount ) {
        ingredientData.ingredientError = "";
        ingredientData.latestIngredientAdded = false;
        final Ingredient newI = new Ingredient();
        try {
            newI.setIngredient( new IngredientType( name ) );
            newI.setAmount( amount );
        }
        catch ( final Exception e ) {
            ingredientData.ingredientError = e.getMessage();
            Assert.fail( "Unable to create new Ingredient" );
        }
        if ( Ingredient.getByType( newI.getIngredient().getName() ) == null ) {
            ingredientData.currentIngredient = newI;
            newI.save();
            ingredientData.latestIngredientAdded = true;
        }
        else {
            ingredientData.latestIngredientAdded = false;
        }
    }

    /**
     * Unsuccessfully attempts to create an ingredient from the parameters
     * specified.
     *
     * @param name
     *            Name of the new Ingredient
     * @param amount
     *            Amount of the new Ingredient
     */
    @When ( "^I attempt to create an ingredient with name: (.+), and initial amount: (.+)$" )
    public void addInvalidIngredient ( final String name, final String amount ) {
        ingredientData.ingredientError = "";
        ingredientData.latestIngredientAdded = false;
        final Ingredient newI = new Ingredient();
        try {
            newI.setIngredient( new IngredientType( name ) );
            try {
                final int a = Integer.parseInt( amount );
                if ( a < 0 ) {
                    throw new NumberFormatException( "Amount must be a positive integer" );
                }
                newI.setAmount( a );
            }
            catch ( final NumberFormatException nfe ) {
                throw new NumberFormatException( "Amount must be a positive integer" );
            }

            if ( Ingredient.getByType( newI.getIngredient().getName() ) == null ) {
                ingredientData.currentIngredient = newI;
                newI.save();
                ingredientData.latestIngredientAdded = true;
            }
            else {
                ingredientData.latestIngredientAdded = false;
            }
        }
        catch ( final Exception e ) {
            ingredientData.ingredientError = e.getMessage();
        }
    }

    /**
     * Ensure that the Ingredient most recently referenced was added.
     */
    @Then ( "^the ingredient is successfully added$" )
    public void ingredientSuccessfullyAdded () {
        Assert.assertTrue( "Adding Ingredient returns false when it should return true",
                ingredientData.latestIngredientAdded );
        final boolean ingredientInList = Ingredient.getAll().contains( ingredientData.currentIngredient );
        Assert.assertTrue( "Ingredient NOT added to coffee maker when it should be", ingredientInList );
    }

    /**
     * Ensure that the Ingredient was not added.
     */
    @Then ( "^the ingredient is not added$" )
    public void ingredientNotAdded () {
        Assert.assertFalse( "Adding Ingredient returns true when it should return false",
                ingredientData.latestIngredientAdded );
        final boolean ingredientInList = Ingredient.getAll().contains( ingredientData.currentIngredient );
        Assert.assertFalse( "Ingredient added to coffee maker when it should not be", ingredientInList );
    }
}
