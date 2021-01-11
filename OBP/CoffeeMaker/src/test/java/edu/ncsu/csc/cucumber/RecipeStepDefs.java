package edu.ncsu.csc.cucumber;

import java.util.Vector;

import org.junit.Assert;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.coffee_maker.models.persistent.DomainObject;
import edu.ncsu.csc.coffee_maker.models.persistent.Ingredient;
import edu.ncsu.csc.coffee_maker.models.persistent.IngredientType;
import edu.ncsu.csc.coffee_maker.models.persistent.Recipe;
import edu.ncsu.csc.test_utils.SharedRecipeData;

/**
 * Step Defs (Cucumber) for interacting with the Recipe model to ensure it is
 * appropriately tested.
 *
 * @author Kai Presler-Marshall
 * @author Sarah Elder
 * @author Alex Snezhko
 * @author Dominic Brown
 * @author Selena Chen
 */
public class RecipeStepDefs {

    /**
     * Shared information about the recipes.
     */
    private final SharedRecipeData recipeData;

    /**
     * Constructor for the RecipeStepDefs. Used to keep track of CoffeeMaker's
     * state to ensure that the test completed successfully.
     *
     * @param srd
     *            SharedRecipeData; A backup of the recipes to ensure that the
     *            CoffeeMaker is behaving appropriately.
     */
    public RecipeStepDefs ( final SharedRecipeData srd ) {
        this.recipeData = srd;
    }

    /**
     * Deletes the existing batch of recipes in the CoffeeMaker and then
     * populates it with a new batch of Recipes.
     *
     * @param numRecipes
     *            The number of recipes that should be in the RecipeBook
     * @throws Exception
     *             If the number of recipes to add is greater than the max
     *             allowed
     */
    @Given ( "^the CoffeeMaker already has (\\d+) Recipes$" )
    public void existingRecipes ( final int numRecipes ) throws Exception {
        // Check current number of recipes in Recipe Book
        final int total = Recipe.getAll().size();
        if ( total != 0 ) {
            DomainObject.deleteAll( Recipe.class );
            Assert.assertEquals( 0, Recipe.getAll().size() );
        }

        if ( numRecipes == 0 ) {
            return; // no need to execute the rest of the code
        }
        if ( numRecipes > 3 ) {
            throw new IllegalArgumentException( "Number of Recipes cannot be greater than 3" );
        }
        else {
            for ( int i = 0; i < numRecipes; i++ ) {
                try {
                    final Recipe testR = new Recipe();
                    try {
                        testR.setName( "Recipe" + i );
                        final int pr = i * 10;
                        testR.setPrice( pr );
                        testR.addIngredient( new Ingredient( new IngredientType( "Coffee" ), i ) );
                        testR.addIngredient( new Ingredient( new IngredientType( "Milk" ), i ) );
                        testR.addIngredient( new Ingredient( new IngredientType( "Sugar" ), i ) );
                        testR.addIngredient( new Ingredient( new IngredientType( "Water" ), i ) );
                    }
                    catch ( final Exception e ) {
                        Assert.fail( "Error in creating recipes" );
                    }
                    testR.save();
                    recipeData.latestRecipeAdded = Recipe.getAll().contains( testR );

                }
                catch ( final Exception e ) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Creates a recipe with the name provided.
     *
     * @param name
     *            Name of the new Recipe to create.
     */
    @Given ( "^the Coffeemaker has a recipe named (.+)$" )
    public void addRecipeWithName ( final String name ) {
        addNewSpecificRecipe( name, 10, 3, 0, 0, 0 );
    }

    /**
     * Create a recipe from the valid parameters specified.
     *
     * @param name
     *            Name of the new Recipe
     * @param cost
     *            Cost of the new Recipe
     * @param coffeeAmt
     *            Amount of Coffee needed for the new Recipe
     * @param milkAmt
     *            Amount of Milk needed for the new Recipe
     * @param sugarAmt
     *            Amount of Sugar needed for the new Recipe
     * @param waterAmt
     *            Amount of Water needed for the new Recipe
     */
    @When ( "^I submit valid values for name: (.+); cost: (\\d+); and ingredients: (\\d+) coffee, (\\d+) milk, (\\d+) sugar, (\\d+) chocolate$" )
    public void addOldSpecificRecipe ( final String name, final int cost, final int coffeeAmt, final int milkAmt,
            final int sugarAmt, final int chocolateAmt ) {
        recipeData.recipeError = "";
        recipeData.latestRecipeAdded = false;
        final Recipe newR = new Recipe();
        try {
            newR.setName( name );
            newR.setPrice( cost );
            newR.addIngredient( new Ingredient( new IngredientType( "Coffee" ), coffeeAmt ) );
            newR.addIngredient( new Ingredient( new IngredientType( "Milk" ), milkAmt ) );
            newR.addIngredient( new Ingredient( new IngredientType( "Sugar" ), sugarAmt ) );
            newR.addIngredient( new Ingredient( new IngredientType( "Chocolate" ), chocolateAmt ) );
        }
        catch ( final Exception e ) {
            recipeData.recipeError = e.getMessage();
            Assert.fail( "Unable to create new Recipe" );
        }
        if ( Recipe.getByName( newR.getName() ) == null && Recipe.getAll().size() < 3 ) {
            recipeData.currentRecipe = newR;
            newR.save();
            recipeData.latestRecipeAdded = true;
        }
        else {
            recipeData.latestRecipeAdded = false;
        }
    }

    /**
     * Create a recipe from the valid parameters specified.
     *
     * @param name
     *            Name of the new Recipe
     * @param cost
     *            Cost of the new Recipe
     * @param coffeeAmt
     *            Amount of Coffee needed for the new Recipe
     * @param milkAmt
     *            Amount of Milk needed for the new Recipe
     * @param sugarAmt
     *            Amount of Sugar needed for the new Recipe
     * @param waterAmt
     *            Amount of Water needed for the new Recipe
     */
    @When ( "^I submit valid values for name: (.+); cost: (\\d+); and ingredients: (\\d+) coffee, (\\d+) milk, (\\d+) sugar, (\\d+) water$" )
    public void addNewSpecificRecipe ( final String name, final int cost, final int coffeeAmt, final int milkAmt,
            final int sugarAmt, final int waterAmt ) {
        recipeData.recipeError = "";
        recipeData.latestRecipeAdded = false;
        final Recipe newR = new Recipe();
        try {
            newR.setName( name );
            newR.setPrice( cost );
            newR.addIngredient( new Ingredient( new IngredientType( "Coffee" ), coffeeAmt ) );
            newR.addIngredient( new Ingredient( new IngredientType( "Milk" ), milkAmt ) );
            newR.addIngredient( new Ingredient( new IngredientType( "Sugar" ), sugarAmt ) );
            newR.addIngredient( new Ingredient( new IngredientType( "Water" ), waterAmt ) );
        }
        catch ( final Exception e ) {
            recipeData.recipeError = e.getMessage();
            Assert.fail( "Unable to create new Recipe" );
        }
        if ( Recipe.getByName( newR.getName() ) == null && Recipe.getAll().size() < 3 ) {
            recipeData.currentRecipe = newR;
            newR.save();
            recipeData.latestRecipeAdded = true;
        }
        else {
            recipeData.latestRecipeAdded = false;
        }
    }

    /**
     * Add a recipe to the Recipe Book.
     */
    @When ( "^I try to add another recipe$" )
    public void addRecipe () {
        addNewSpecificRecipe( "HotWater", 50, 0, 1, 1, 4 );
    }

    /**
     * Unsuccessfully attempt to add a new Recipe with the name provided.
     *
     * @param name
     *            Name of the new Recipe that is to be added
     */
    @When ( "^I try to add another recipe named (.+)$" )
    public void addAnotherRecipeWithName ( final String name ) {
        invalidRecipe( name, "10", "4", "0", "0", "0" );
    }

    /**
     * Unsuccessfully attempt to add a new Recipe to the system.
     *
     * @param name
     *            Name of the new Recipe
     * @param cost
     *            Cost of the new Recipe
     * @param coffeeAmt
     *            Amount of Coffee needed for the new Recipe
     * @param milkAmt
     *            Amount of Milk needed for the new Recipe
     * @param sugarAmt
     *            Amount of Sugar needed for the new Recipe
     * @param waterAmt
     *            Amount of Water needed for the new Recipe
     */
    @When ( "^I attempt to submit the following recipe values - name: (.+); cost: (.+); and ingredients: (.+) coffee, (.+) milk, (.+) sugar, (.+) water$" )
    public void invalidRecipe ( final String name, final String cost, final String coffeeAmt, final String milkAmt,
            final String sugarAmt, final String waterAmt ) {
        recipeData.recipeError = "";
        recipeData.latestRecipeAdded = false;
        final Recipe newR = new Recipe();
        try {
            newR.setName( name );
            try {
                final int c = Integer.parseInt( cost );
                if ( c < 0 ) {
                    throw new NumberFormatException( "Price must be a positive integer" );
                }
                newR.setPrice( c );
            }
            catch ( final NumberFormatException nfe ) {
                throw new NumberFormatException( "Price must be a positive integer" );
            }
            try {
                final int cAmt = Integer.parseInt( coffeeAmt );
                final int mAmt = Integer.parseInt( milkAmt );
                final int sAmt = Integer.parseInt( sugarAmt );
                final int wAmt = Integer.parseInt( waterAmt );

                if ( cAmt < 0 || mAmt < 0 || sAmt < 0 || wAmt < 0 ) {
                    throw new NumberFormatException( "Amount must be a positive integer" );
                }

                newR.addIngredient( new Ingredient( new IngredientType( "Coffee" ), Integer.parseInt( coffeeAmt ) ) );
                newR.addIngredient( new Ingredient( new IngredientType( "Milk" ), Integer.parseInt( milkAmt ) ) );
                newR.addIngredient( new Ingredient( new IngredientType( "Sugar" ), Integer.parseInt( sugarAmt ) ) );
                newR.addIngredient( new Ingredient( new IngredientType( "Water" ), Integer.parseInt( waterAmt ) ) );
            }
            catch ( final Exception e ) {
                throw new NumberFormatException( "Units of must be a positive integer" );
            }

            if ( Recipe.getByName( newR.getName() ) == null && Recipe.getAll().size() < 3 ) {
                recipeData.currentRecipe = newR;
                newR.save();
                recipeData.latestRecipeAdded = true;
            }
            else {
                recipeData.latestRecipeAdded = false;
            }
        }
        catch ( final Exception e ) {
            recipeData.recipeError = e.getMessage();
        }
    }

    /**
     * Deletes the Recipe with the name provided.
     *
     * @param name
     *            Name of the recipe to delete
     */
    @When ( "^I delete the recipe called (.+)$" )
    public void validDeleteByName ( final String name ) {
        final Recipe toDelete = Recipe.getByName( name );
        recipeData.currentRecipe = toDelete;
        toDelete.delete();
    }

    /**
     * Attempts to delete a recipe with the name provided. The recipe won't
     * exist and this will fail.
     *
     * @param name
     *            Name of the recipe to attempt to delete
     */
    @When ( "^I attempt to delete a nonexistent recipe called (.+)$" )
    public void invalidDeleteByName ( final String name ) {
        recipeData.currentRecipeList = Recipe.getAll();

        final Recipe toDelete = new Recipe();
        toDelete.setName( name );

        try {
            toDelete.delete();
        }
        catch ( final Exception e ) {
            recipeData.recipeError = e.getMessage();
        }
    }

    /**
     * Edit the most recently referenced recipe and give it the new parameters
     * provided.
     *
     * @param cost
     *            New cost of the Recipe
     * @param coffeeAmt
     *            New amount of Coffee needed for the Recipe
     * @param milkAmt
     *            New amount of Milk needed for the Recipe
     * @param sugarAmt
     *            New amount of Sugar needed for the Recipe
     * @param chocolateAmt
     *            New amount of Chocolate needed for the Recipe
     */
    @When ( "^I edit that recipe to have cost: (\\d+); and ingredients: (\\d+) coffee, (\\d+) milk, (\\d+) sugar, (\\d+) chocolate$" )
    public void validEditRecipe ( final int cost, final int coffeeAmt, final int milkAmt, final int sugarAmt,
            final int chocolateAmt ) {
        recipeData.recipeError = "";
        final Recipe newR = recipeData.currentRecipe;

        try {
            newR.setPrice( cost );
            newR.addIngredient( new Ingredient( new IngredientType( "Coffee" ), coffeeAmt ) );
            newR.addIngredient( new Ingredient( new IngredientType( "Milk" ), milkAmt ) );
            newR.addIngredient( new Ingredient( new IngredientType( "Sugar" ), sugarAmt ) );
            newR.addIngredient( new Ingredient( new IngredientType( "Chocolate" ), chocolateAmt ) );
        }
        catch ( final Exception e ) {
            recipeData.recipeError = e.getMessage();
            Assert.fail( "Unable to edit Recipe" );
        }
        recipeData.currentRecipe = newR;

        newR.save();
    }

    /**
     * Unsuccessfully attempt to edit the most recently referenced recipe with
     * the new parameters provided.
     *
     * @param cost
     *            New cost of the Recipe
     * @param coffeeAmt
     *            New amount of Coffee needed for the Recipe
     * @param milkAmt
     *            New amount of Milk needed for the Recipe
     * @param sugarAmt
     *            New amount of Sugar needed for the Recipe
     * @param chocolateAmt
     *            New amount of Chocolate needed for the Recipe
     */
    @When ( "^I invalidly edit that recipe to have cost: (.+); and ingredients: (.+) coffee, (.+) milk, (.+) sugar, (.+) chocolate$" )
    public void invalidEditRecipe ( final String cost, final String coffeeAmt, final String milkAmt,
            final String sugarAmt, final String chocolateAmt ) {
        recipeData.recipeError = "";
        final Recipe newR = new Recipe();
        try {
            try {
                newR.setPrice( Integer.parseInt( cost ) );
            }
            catch ( final NumberFormatException nfe ) {
                throw new NumberFormatException( "Price must be a positive integer" );
            }
            try {
                newR.addIngredient( new Ingredient( new IngredientType( "Coffee" ), Integer.parseInt( coffeeAmt ) ) );
                newR.addIngredient( new Ingredient( new IngredientType( "Milk" ), Integer.parseInt( milkAmt ) ) );
                newR.addIngredient( new Ingredient( new IngredientType( "Sugar" ), Integer.parseInt( sugarAmt ) ) );
                newR.addIngredient(
                        new Ingredient( new IngredientType( "Chocolate" ), Integer.parseInt( chocolateAmt ) ) );
            }
            catch ( final Exception e ) {
                throw new NumberFormatException( "Units of must be a positive integer" );
            }
        }
        catch ( final Exception e ) {
            recipeData.recipeError = e.getMessage();
        }
        recipeData.currentRecipe = newR;
    }

    /**
     * Ensure that the Recipe most recently referenced was added to the
     * RecipeBook.
     */
    @Then ( "^the recipe is successfully added$" )
    public void recipeSuccessfullyAdded () {
        Assert.assertTrue( "Adding Recipe returns false when it should return true", recipeData.latestRecipeAdded );
        final boolean recipeInList = Recipe.getAll().contains( recipeData.currentRecipe );
        Assert.assertTrue( "Recipe NOT added to coffee maker when it should be", recipeInList );
    }

    /**
     * Ensure that the Recipe was not added to the book.
     */
    @Then ( "^the recipe is not added$" )
    public void recipeNotAdded () {
        Assert.assertFalse( "Adding Recipe returns true when it should return false", recipeData.latestRecipeAdded );
        final boolean recipeInList = Recipe.getAll().contains( recipeData.currentRecipe );
        Assert.assertFalse( "Recipe added to coffee maker when it should not be", recipeInList );
    }

    /**
     * Ensure that another recipe was not added to the CofffeeMaker's recipe
     * book.
     */
    @Then ( "^a second recipe is not added$" )
    public void secondRecipeNotAdded () {
        Assert.assertFalse( "Adding Recipe returns true when it should return false", recipeData.latestRecipeAdded );
        int numRecipeInList = 0;
        for ( final Recipe r : Recipe.getAll() ) {
            if ( r.equals( recipeData.currentRecipe ) ) {
                numRecipeInList++;
            }
        }
        Assert.assertTrue( "Recipe added more/less than once", ( numRecipeInList == 1 ) );
    }

    /**
     * Ensure that an invalid price error was produced.
     */
    @Then ( "^the error for an invalid recipe price occurs$" )
    public void invalidPriceError () {
        Assert.assertFalse( recipeData.recipeError.isEmpty() );
    }

    /**
     * Ensure that an error occurred for adding the inventory specified.
     *
     * @param ingredient
     *            The ingredient that should trigger the error
     */
    @Then ( "^the error for an invalid amount of (.+) in a recipe occurs$" )
    public void invalidIngredientError ( final String ingredient ) {
        Assert.assertTrue( !ingredient.isEmpty() );
    }

    /**
     * Ensure that the Recipe with the name provided was deleted.
     */
    @Then ( "^the recipe is successfully deleted by name$" )
    public void recipeDeletedByName () {
        Assert.assertNull( "Recipe still exists in Recipe Book",
                Recipe.getByName( recipeData.currentRecipe.getName() ) );
    }

    /**
     * Ensure that the RecipeBook of the CoffeeMaker was unchanged.
     */
    @Then ( "^the recipe book of the CoffeeMaker is not changed$" )
    public void recipeNotDeleted () {
        Assert.assertEquals( "The recipe book changed when it shouldn't have", recipeData.currentRecipeList,
                Recipe.getAll() );
    }

    /**
     * Ensure that the recipe at the index provided was deleted.
     */
    @Then ( "^the recipe is successfully deleted by index$" )
    public void recipeDeletedByIndex () {
        final Vector<Recipe> recipes = (Vector<Recipe>) Recipe.getAll();
        if ( recipes.size() > 0 ) { // If size is zero, successfully deleted it.
            if ( recipeData.index < recipes.size() ) {
                Assert.assertNotEquals( "Recipe was not successfully deleted by index", recipeData.currentRecipe,
                        recipes.get( recipeData.index ) );
            }
            else {
                Assert.assertNotEquals( "Recipe was not successfully deleted by index", recipeData.currentRecipe,
                        recipes.get( recipeData.index - 1 ) );
            }
        }
    }

    /**
     * Ensure that an error occurred when trying to perform an action.
     */
    @Then ( "^an error occurs$" )
    public void errorOccurs () {
        Assert.assertNotEquals( "", recipeData.recipeError );
    }

    /**
     * Ensure that a recipe was edited as expected by comparing the actual
     * results in the CoffeeMaker to what is stored in the backup.
     */
    @Then ( "^the recipe is successfully edited$" )
    public void recipeEditedSuccessfully () {
        Assert.assertEquals( "Recipe was not edited correctly", recipeData.currentRecipe,
                Recipe.getByName( recipeData.currentRecipe.getName() ) );
    }

    /**
     * Ensure that a recipe was not edited as expected by comparing the actual
     * results in the CoffeeMaker to what is stored in the backup.
     */
    @Then ( "^the recipe is not edited$" )
    public void recipeEditedUnsuccessfully () {
        final Recipe current = recipeData.currentRecipe;
        final Recipe other = Recipe.getByName( recipeData.currentRecipe.getName() );
        Assert.assertNotEquals( "Recipe was edited when it shouldn't have been", current, other );
    }
}
