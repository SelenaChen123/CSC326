package edu.ncsu.csc.cucumber;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Assert;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.coffee_maker.controllers.APICoffeeController;
import edu.ncsu.csc.coffee_maker.models.persistent.DomainObject;
import edu.ncsu.csc.coffee_maker.models.persistent.Ingredient;
import edu.ncsu.csc.coffee_maker.models.persistent.IngredientType;
import edu.ncsu.csc.coffee_maker.models.persistent.Inventory;
import edu.ncsu.csc.coffee_maker.models.persistent.Recipe;
import edu.ncsu.csc.test_utils.SharedCoffeeMakerData;

/**
 * StepDefs (Cucumber) for making coffee.
 *
 * @author Kai Presler-Marshall
 * @author Sarah Elder
 * @author Alex Snezhko
 * @author Dominic Brown
 * @author Selena Chen
 */
public class MakeCoffeeStepDefs {

    /**
     * Shared information about the coffee maker.
     */
    private final SharedCoffeeMakerData makerData;

    /**
     * Constructor for the StepDefs.
     *
     * @param md
     *            The CoffeeMakerData that is used to ensure that actions taken
     *            resulted in the correct result
     */
    public MakeCoffeeStepDefs ( final SharedCoffeeMakerData md ) {
        this.makerData = md;
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
    @SuppressWarnings ( "deprecation" )
    @Given ( "^the CoffeeMaker has (\\d+) Recipes$" )
    public void existingRecipes ( final int numRecipes ) throws Exception {
        // Check current number of recipes in Recipe Book
        DomainObject.deleteAll( Recipe.class );

        if ( numRecipes == 0 ) {
            return; // no need to execute the rest of the code
        }
        if ( numRecipes > 3 ) {
            throw new IllegalArgumentException( "Number of Recipes cannot be greater than 3" );
        }
        else {
            for ( int i = 1; i <= numRecipes; i++ ) {
                try {
                    final Recipe testR = new Recipe();
                    try {
                        testR.setName( "Recipe" + i );
                        final Integer pr = new Integer( i * 10 );
                        testR.setPrice( pr );

                        for ( int j = 0; j < testR.getIngredients().size(); j++ ) {
                            if ( testR.getIngredients().get( j ).getIngredient().getName().equals( "Coffee" ) ) {
                                testR.getIngredients().get( j ).setAmount( new Integer( i ) );
                            }
                            else if ( testR.getIngredients().get( j ).getIngredient().getName().equals( "Milk" ) ) {
                                testR.getIngredients().get( j ).setAmount( 1 );
                            }
                            else if ( testR.getIngredients().get( j ).getIngredient().getName().equals( "Sugar" ) ) {
                                testR.getIngredients().get( j ).setAmount( 1 );
                            }
                            else if ( testR.getIngredients().get( j ).getIngredient().getName()
                                    .equals( "Chocolate" ) ) {
                                testR.getIngredients().get( j ).setAmount( 1 );
                            }
                        }
                    }
                    catch ( final Exception e ) {
                        Assert.fail( "Error in creating recipes" );
                    }
                    testR.save();
                }
                catch ( final Exception e ) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Sets the CoffeeMaker's Inventory to contain the ingredients specified
     * here.
     *
     * @param originalCoffee
     *            The amount of Coffee to set the Inventory to.
     * @param originalMilk
     *            The amount of Milk to set the Inventory to.
     * @param originalSugar
     *            The amount of Sugar to set the Inventory to.
     * @param originalChocolate
     *            The amount of Chocolate to set the Inventory to.
     */
    @Given ( "^(\\d+) coffee, (\\d+) milk, (\\d+) sugar, and (\\d+) chocolate currently in the CoffeeMaker$" )
    public void initialInventory ( final int originalCoffee, final int originalMilk, final int originalSugar,
            final int originalChocolate ) {
        makerData.originalCoffee = originalCoffee;
        makerData.originalMilk = originalMilk;
        makerData.originalSugar = originalSugar;
        makerData.originalChocolate = originalChocolate;

        DomainObject.deleteAll( Inventory.class );
        final Inventory i = Inventory.getInventory();
        final ArrayList<Ingredient> ings = new ArrayList<Ingredient>();
        ings.add( new Ingredient( new IngredientType( "Coffee" ), originalCoffee ) );
        ings.add( new Ingredient( new IngredientType( "Milk" ), originalMilk ) );
        ings.add( new Ingredient( new IngredientType( "Sugar" ), originalSugar ) );
        ings.add( new Ingredient( new IngredientType( "Chocolate" ), originalChocolate ) );
        i.addIngredients( ings );
        i.save();
    }

    /**
     * Creates a new Recipe for the COffeeMaker using the parameters specified.
     *
     * @param name
     *            Name of the new Recipe
     * @param cost
     *            Cost of the new Recipe
     * @param coffeeAmt
     *            Amount of Coffee to be used by the new Recipe
     * @param milkAmt
     *            Amount of Milk to be used by the new Recipe
     * @param sugarAmt
     *            Amount of Sugar to be used by the new Recipe
     * @param chocolateAmt
     *            Amount of Chocolate to be used by the new Recipe
     */
    @When ( "^I submit values for name: (.+); cost: (\\d+); and ingredients: (\\d+) coffee, (\\d+) milk, (\\d+) sugar, (\\d+) chocolate$" )
    public void addSpecificRecipe ( final String name, final int cost, final int coffeeAmt, final int milkAmt,
            final int sugarAmt, final int chocolateAmt ) {
        makerData.errorMessage = "";
        final Recipe newR = new Recipe();
        try {
            newR.setName( name );
            newR.setPrice( cost );

            for ( int j = 0; j < newR.getIngredients().size(); j++ ) {
                if ( newR.getIngredients().get( j ).getIngredient().getName().equals( "Coffee" ) ) {
                    newR.getIngredients().get( j ).setAmount( coffeeAmt );
                }
                else if ( newR.getIngredients().get( j ).getIngredient().getName().equals( "Milk" ) ) {
                    newR.getIngredients().get( j ).setAmount( milkAmt );
                }
                else if ( newR.getIngredients().get( j ).getIngredient().getName().equals( "Sugar" ) ) {
                    newR.getIngredients().get( j ).setAmount( sugarAmt );
                }
                else if ( newR.getIngredients().get( j ).getIngredient().getName().equals( "Chocolate" ) ) {
                    newR.getIngredients().get( j ).setAmount( chocolateAmt );
                }
            }

            newR.save();
            makerData.currentRecipe = newR;
        }
        catch ( final Exception e ) {
            makerData.errorMessage = e.getMessage();
            Assert.fail( "Unable to create new Recipe" );
        }
    }

    /**
     * Execute the CoffeeMaker's MakeCoffee action with the name of the recipe
     * specified and the amount of money paid.
     *
     * @param recipeName
     *            Name of the recipe to use for making Coffee
     * @param sMoney
     *            Amount of money that is paid
     */
    @When ( "^I make coffee with the recipe for (.+) and input (.+) money$" )
    public void validMakeCoffee ( final String recipeName, final String sMoney ) {
        try {
            try {
                final int money = Integer.parseInt( sMoney );
                makerData.moneyGiven = money;
                makerData.change = APICoffeeController.makeCoffee( makerData.currentRecipe, money );
            }
            catch ( final NumberFormatException nfe ) {
                throw new NumberFormatException( "Money must be a positive integer" );
            }
        }
        catch ( final Exception e ) {
            makerData.errorMessage = e.getMessage();
        }
    }

    /**
     * Ensure that the coffee was successfully made and the change given was the
     * correct amount for the recipe price and the amount paid
     */
    @Then ( "^the coffee is successfully made with correct change$" )
    public void validMakeCoffeeResult () {
        Assert.assertEquals( "", makerData.errorMessage );
        final int expectedChange = makerData.moneyGiven - makerData.currentRecipe.getPrice();
        Assert.assertEquals( expectedChange, makerData.change );
    }

    /**
     * Ensure that if the CoffeeMaker failed to make coffee properly that all of
     * the money provided was returned as change
     */
    @Then ( "^the coffee maker returns your money$" )
    public void invalidMakeCoffeeResult () {
        Assert.assertEquals( makerData.moneyGiven, makerData.change );
    }

    /**
     * Ensure that updating the inventory or making coffee results in the
     * expected inventory
     */
    @Then ( "^the inventory is updated correctly$" )
    public void validInventoryUpdate () {
        int expectedCoffee = 0;
        int expectedMilk = 0;
        int expectedSugar = 0;
        int expectedChocolate = 0;

        for ( int i = 0; i < makerData.currentRecipe.getIngredients().size(); i++ ) {
            if ( makerData.currentRecipe.getIngredients().get( i ).getIngredient().getName().equals( "Coffee" ) ) {
                expectedCoffee = makerData.originalCoffee
                        - makerData.currentRecipe.getIngredients().get( i ).getAmount();
            }
            else if ( makerData.currentRecipe.getIngredients().get( i ).getIngredient().getName().equals( "Milk" ) ) {
                expectedMilk = makerData.originalMilk - makerData.currentRecipe.getIngredients().get( i ).getAmount();
            }
            else if ( makerData.currentRecipe.getIngredients().get( i ).getIngredient().getName().equals( "Sugar" ) ) {
                expectedSugar = makerData.originalSugar - makerData.currentRecipe.getIngredients().get( i ).getAmount();
            }
            else if ( makerData.currentRecipe.getIngredients().get( i ).getIngredient().getName()
                    .equals( "Chocolate" ) ) {
                expectedChocolate = makerData.originalChocolate
                        - makerData.currentRecipe.getIngredients().get( i ).getAmount();
            }
        }

        final Inventory inventory = Inventory.getInventory();

        for ( int i = 0; i < makerData.currentRecipe.getIngredients().size(); i++ ) {
            if ( makerData.currentRecipe.getIngredients().get( i ).getIngredient().getName().equals( "Coffee" ) ) {
                assertEquals( expectedCoffee, inventory.getIngredients().get( i ).getAmount() );
            }
            else if ( makerData.currentRecipe.getIngredients().get( i ).getIngredient().getName().equals( "Milk" ) ) {
                assertEquals( expectedMilk, inventory.getIngredients().get( i ).getAmount() );
            }
            else if ( makerData.currentRecipe.getIngredients().get( i ).getIngredient().getName().equals( "Sugar" ) ) {
                assertEquals( expectedSugar, inventory.getIngredients().get( i ).getAmount() );
            }
            else if ( makerData.currentRecipe.getIngredients().get( i ).getIngredient().getName()
                    .equals( "Chocolate" ) ) {
                assertEquals( expectedChocolate, inventory.getIngredients().get( i ).getAmount() );
            }
        }
    }

    /**
     * Ensure that an error occurring results in the inventory not being
     * changed.
     */
    @Then ( "^the inventory is not changed$" )
    public void inventoryNotChanged () {
        final Inventory inventory = Inventory.getInventory();

        for ( int i = 0; i < makerData.currentRecipe.getIngredients().size(); i++ ) {
            if ( makerData.currentRecipe.getIngredients().get( i ).getIngredient().getName().equals( "Coffee" ) ) {
                assertEquals( makerData.originalCoffee, inventory.getIngredients().get( i ).getAmount() );
            }
            else if ( makerData.currentRecipe.getIngredients().get( i ).getIngredient().getName().equals( "Milk" ) ) {
                assertEquals( makerData.originalMilk, inventory.getIngredients().get( i ).getAmount() );
            }
            else if ( makerData.currentRecipe.getIngredients().get( i ).getIngredient().getName().equals( "Sugar" ) ) {
                assertEquals( makerData.originalSugar, inventory.getIngredients().get( i ).getAmount() );
            }
            else if ( makerData.currentRecipe.getIngredients().get( i ).getIngredient().getName()
                    .equals( "Chocolate" ) ) {
                assertEquals( makerData.originalChocolate, inventory.getIngredients().get( i ).getAmount() );
            }
        }
    }

    /**
     * The CoffeeMaker has no (direct) way to remove ingredients from the
     * Inventory it stores, this allows us to effectively do so by creating a
     * recipe with the amount of ingredients to remove, and then making coffee
     * with the Recipe just created.
     *
     * @param removeCoffee
     *            Amount of Coffee to remove from the Inventory
     * @param removeMilk
     *            Amount of Milk to remove from the Inventory
     * @param removeSugar
     *            Amount of Sugar to remove from the Inventory
     * @param removeChocolate
     *            Amount of Chocolate to remove from the Inventory
     */
    public void removeInventoryHelper ( final int removeCoffee, final int removeMilk, final int removeSugar,
            final int removeChocolate ) {
        final Inventory currentInventory = Inventory.getInventory();

        final Recipe r = new Recipe();

        for ( int i = 0; i < r.getIngredients().size(); i++ ) {
            if ( r.getIngredients().get( i ).getIngredient().getName().equals( "Coffee" ) ) {
                r.getIngredients().get( i ).setAmount( removeCoffee );
            }
            else if ( r.getIngredients().get( i ).getIngredient().getName().equals( "Milk" ) ) {
                r.getIngredients().get( i ).setAmount( removeMilk );
            }
            else if ( r.getIngredients().get( i ).getIngredient().getName().equals( "Sugar" ) ) {
                r.getIngredients().get( i ).setAmount( removeSugar );
            }
            else if ( r.getIngredients().get( i ).getIngredient().getName().equals( "Chocolate" ) ) {
                r.getIngredients().get( i ).setAmount( removeChocolate );
            }
        }

        currentInventory.useIngredients( r );
    }
}
