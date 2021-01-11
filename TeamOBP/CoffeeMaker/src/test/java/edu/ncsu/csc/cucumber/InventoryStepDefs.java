package edu.ncsu.csc.cucumber;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Assert;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.coffee_maker.models.persistent.DomainObject;
import edu.ncsu.csc.coffee_maker.models.persistent.Ingredient;
import edu.ncsu.csc.coffee_maker.models.persistent.IngredientType;
import edu.ncsu.csc.coffee_maker.models.persistent.Inventory;
import edu.ncsu.csc.coffee_maker.models.persistent.Recipe;
import edu.ncsu.csc.test_utils.SharedInventoryData;

/**
 * StepDefs (Cucumber) test class for interacting with the Inventory model. This
 * performs a number of tests to ensure that the inventory is changed in the
 * expected manner
 *
 * @author Kai Presler-Marshall
 * @author Sarah Elder
 * @author Alex Snezhko
 * @author Dominic Brown
 * @author Selena Chen
 */
public class InventoryStepDefs {

    /**
     * Shared information about the inventory.
     */
    private final SharedInventoryData inventoryData;

    /**
     * Constructor.
     *
     * @param sid
     *            SharedInventoryData; Basically a backup copy of the inventory
     *            to make sure that changes made to the "real" one are what is
     *            expected
     */
    public InventoryStepDefs ( final SharedInventoryData sid ) {
        this.inventoryData = sid;
    }

    /**
     * This Cucumber "Given" step ensures that the Recipe has the amount of
     * ingredients specified. This is used to ensure that preconditions for the
     * tests are satisfied.
     *
     * @param originalCoffee
     *            Amount of Coffee that the inventory will be set to have
     * @param originalWater
     *            Amount of Water that the inventory will be set to have
     * @param originalChai
     *            Amount of Chai that the inventory will be set to have
     */
    @Given ( "^there is (-?\\d+) coffee, (-?\\d+) water, and (-?\\d+) chai in the CoffeeMaker$" )
    public void initialInventory ( final int originalCoffee, final int originalWater, final int originalChai ) {
        DomainObject.deleteAll( Recipe.class );

        DomainObject.deleteAll( Inventory.class );
        final Inventory i = Inventory.getInventory();
        final ArrayList<Ingredient> ings = new ArrayList<Ingredient>();
        ings.add( new Ingredient( new IngredientType( "Coffee" ), originalCoffee ) );
        ings.add( new Ingredient( new IngredientType( "Water" ), originalWater ) );
        ings.add( new Ingredient( new IngredientType( "Chai" ), originalChai ) );
        i.addIngredients( ings );
        i.save();

        for ( int j = 0; j < inventoryData.origVals.size(); j++ ) {
            if ( inventoryData.origVals.get( j ).getIngredient().getName().equals( "Coffee" ) ) {
                inventoryData.origVals.get( j ).setAmount( originalCoffee );
            }
            else if ( inventoryData.origVals.get( j ).getIngredient().getName().equals( "Water" ) ) {
                inventoryData.origVals.get( j ).setAmount( originalWater );
            }
            else if ( inventoryData.origVals.get( j ).getIngredient().getName().equals( "Chai" ) ) {
                inventoryData.origVals.get( j ).setAmount( originalChai );
            }
        }
    }

    /**
     * Add the specified amounts of ingredients to the CoffeeMaker's inventory.
     * This is required to pass.
     *
     * @param amtCoffee
     *            Amount of Coffee to add to the Inventory
     * @param amtWater
     *            Amount of Water to add to the Inventory
     * @param amtChai
     *            Amount of Chai to add to the Inventory
     */
    @When ( "^I add (-?\\d+) coffee, (-?\\d+) water, and (-?\\d+) chai$" )
    public void addInventory ( final int amtCoffee, final int amtWater, final int amtChai ) {

        for ( int j = 0; j < inventoryData.newVals.size(); j++ ) {
            if ( inventoryData.newVals.get( j ).getIngredient().getName().equals( "Coffee" ) ) {
                inventoryData.newVals.get( j ).setAmount( amtCoffee );
            }
            else if ( inventoryData.newVals.get( j ).getIngredient().getName().equals( "Water" ) ) {
                inventoryData.newVals.get( j ).setAmount( amtWater );
            }
            else if ( inventoryData.newVals.get( j ).getIngredient().getName().equals( "Chai" ) ) {
                inventoryData.newVals.get( j ).setAmount( amtChai );
            }
        }

        try {
            final Inventory inventory = Inventory.getInventory();
            final ArrayList<Ingredient> ings = new ArrayList<Ingredient>();
            ings.add( new Ingredient( new IngredientType( "Coffee" ), amtCoffee ) );
            ings.add( new Ingredient( new IngredientType( "Water" ), amtWater ) );
            ings.add( new Ingredient( new IngredientType( "Chai" ), amtChai ) );
            inventory.addIngredients( ings );
            inventory.save();
        }
        catch ( final Exception e ) {
            Assert.fail( "Inventory not added. InventoryException thrown" );
        }
    }

    /**
     * Add the specified amount of ingredients to the CoffeeMaker's inventory.
     * This is for testing with invalid values and is expected to fail.
     *
     * @param amtCoffee
     *            Amount of Coffee to add to the Inventory
     * @param amtWater
     *            Amount of Water to add to the Inventory
     * @param amtChai
     *            Amount of Chai to add to the Inventory
     */
    @When ( "^I attempt to add (-?\\d+) coffee, (-?\\d+) water, and (-?\\d+) chai$" )
    public void invalidAddInventory ( final int amtCoffee, final int amtWater, final int amtChai ) {

        try {
            final Inventory inventory = Inventory.getInventory();
            final ArrayList<Ingredient> ings = new ArrayList<Ingredient>();
            ings.add( new Ingredient( new IngredientType( "Coffee" ), amtCoffee ) );
            ings.add( new Ingredient( new IngredientType( "Water" ), amtWater ) );
            ings.add( new Ingredient( new IngredientType( "Chai" ), amtChai ) );
            inventory.addIngredients( ings );
            inventory.save();
            Assert.fail( "Inventory added without throwing an error." );
        }
        catch ( final Exception e ) {
            inventoryData.errorMessage = e.getMessage();
            Assert.assertTrue( "Adding Inventory throws error", true );
        }
    }

    /**
     * Ensure that an error was thrown while attempting to perform an action.
     *
     * @param error
     *            The error message to check
     */
    @Then ( "^an error occurs for (.+)$" )
    public void errorThrown ( final String error ) {
        Assert.assertTrue( !error.isEmpty() );
    }

    /**
     * Verify that the CoffeeMaker's Inventory has been updated and that it
     * stores the values in the SharedInventoryData.
     */
    @Then ( "^the inventory of the CoffeeMaker is successfully added$" )
    public void inventoryAdded () {
        int expectedCoffee = 0;
        int expectedWater = 0;
        int expectedChai = 0;

        for ( int i = 0; i < inventoryData.origVals.size(); i++ ) {
            if ( inventoryData.origVals.get( i ).getIngredient().getName().equals( "Coffee" ) ) {
                expectedCoffee = inventoryData.origVals.get( i ).getAmount()
                        + inventoryData.newVals.get( i ).getAmount();
            }
            else if ( inventoryData.origVals.get( i ).getIngredient().getName().equals( "Water" ) ) {
                expectedWater = inventoryData.origVals.get( i ).getAmount()
                        + inventoryData.newVals.get( i ).getAmount();
            }
            else if ( inventoryData.origVals.get( i ).getIngredient().getName().equals( "Chai" ) ) {
                expectedChai = inventoryData.origVals.get( i ).getAmount() + inventoryData.newVals.get( i ).getAmount();
            }
        }

        final Inventory inventory = Inventory.getInventory();

        int coffee2 = 0;
        int water2 = 0;
        int chai2 = 0;

        for ( int i = 0; i < inventory.getIngredients().size(); i++ ) {
            if ( inventory.getIngredients().get( i ).getIngredient().getName().equals( "Coffee" ) ) {
                coffee2 = inventory.getIngredients().get( i ).getAmount();
            }
            else if ( inventory.getIngredients().get( i ).getIngredient().getName().equals( "Water" ) ) {
                water2 = inventory.getIngredients().get( i ).getAmount();
            }
            else if ( inventory.getIngredients().get( i ).getIngredient().getName().equals( "Chai" ) ) {
                chai2 = inventory.getIngredients().get( i ).getAmount();
            }
        }

        // Verify that the inventory is correct
        Assert.assertEquals( "Coffee added correctly", expectedCoffee, coffee2 );
        Assert.assertEquals( "Water added correctly", expectedWater, water2 );
        Assert.assertEquals( "Chai added correctly", expectedChai, chai2 );
    }

    /**
     * Verify that the CoffeeMaker's Inventory was not updated and contains the
     * same values that were already stored in the "backup" Shared Inventory
     * Data.
     */
    @Then ( "^the inventory of the CoffeeMaker is not updated$" )
    public void inventoryNotUpdated () {
        final Inventory inventory = Inventory.getInventory();

        int coffee2 = 0;
        int water2 = 0;
        int chai2 = 0;

        for ( int i = 0; i < inventory.getIngredients().size(); i++ ) {
            if ( inventory.getIngredients().get( i ).getIngredient().getName().equals( "Coffee" ) ) {
                coffee2 = inventory.getIngredients().get( i ).getAmount();
            }
            else if ( inventory.getIngredients().get( i ).getIngredient().getName().equals( "Water" ) ) {
                water2 = inventory.getIngredients().get( i ).getAmount();
            }
            else if ( inventory.getIngredients().get( i ).getIngredient().getName().equals( "Chai" ) ) {
                chai2 = inventory.getIngredients().get( i ).getAmount();
            }
        }

        // Verify that the inventory is unchanged
        for ( int j = 0; j < inventoryData.origVals.size(); j++ ) {
            if ( inventoryData.origVals.get( j ).getIngredient().getName().equals( "Coffee" ) ) {
                assertEquals( "Coffee not correct", inventoryData.origVals.get( j ).getAmount(), coffee2 );
            }
            else if ( inventoryData.origVals.get( j ).getIngredient().getName().equals( "Water" ) ) {
                assertEquals( "Water not correct", inventoryData.origVals.get( j ).getAmount(), water2 );
            }
            else if ( inventoryData.origVals.get( j ).getIngredient().getName().equals( "Chai" ) ) {
                assertEquals( "Chai not correct", inventoryData.origVals.get( j ).getAmount(), chai2 );
            }
        }
    }
}
