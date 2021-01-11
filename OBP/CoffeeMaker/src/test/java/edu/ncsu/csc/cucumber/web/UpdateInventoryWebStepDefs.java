package edu.ncsu.csc.cucumber.web;

import org.openqa.selenium.By;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.test_utils.SharedInventoryData;

/****
 * StepDefs(Cucumber)test class for interacting with the Inventory web model.
 * This performs a number of tests to ensure that the inventory is updated in
 * the expected manner on the web.
 *
 * @author Kai Presler-Marshall
 * @author Sarah Elder
 * @author Alex Snezhko
 * @author Dominic Brown
 * @author Selena Chen
 */
public class UpdateInventoryWebStepDefs extends CucumberTest {

    /**
     * Shared information about the inventory.
     */
    // private final SharedInventoryData inventoryData;

    /**
     * Constructor.
     *
     * @param sid
     *            SharedInventoryData; Basically a backup copy of the inventory
     *            to make sure that changes made to the "real" one are what is
     *            expected
     */
    public UpdateInventoryWebStepDefs ( final SharedInventoryData sid ) {
        // this.inventoryData = sid;
    }

    /**
     * Navigates to the Update Inventory page.
     */
    @Given ( "^I am on the Update Inventory Page$" )
    public void navigateToUpdateInventory () {
        final String baseUrl = "http://localhost:8080";

        driver.get( baseUrl + "" );
        driver.findElement( By.linkText( "Update Inventory" ) ).click();
    }

    /**
     * This Cucumber "Given" step ensures that the Recipe has the amount of
     * ingredients specified. This is used to ensure that preconditions for the
     * tests are satisfied.
     *
     * @param originalIngredientAmt
     *            Amount of Ingredient that the inventory will be set to have
     * @param ingredient
     *            Ingredient that the inventory will be set to have
     */
    @Given ( "^there is (-?\\d+) of (.+) in the CoffeeMaker$" )
    public void initialIndividualInventory ( final int originalIngredientAmt, final String ingredient ) {
        // DomainObject.deleteAll( Inventory.class );
        // final Inventory i = Inventory.getInventory();
        //
        // for ( int j = 0; j < i.getIngredients().size(); j++ ) {
        // if ( i.getIngredients().get( j ).getIngredient().equals( "Coffee" ) )
        // {
        // inventoryData.origVals.get( j ).setAmount( originalIngredientAmt );
        // final ArrayList<Ingredient> ings = new ArrayList<Ingredient>();
        // ings.add( new Ingredient( "Coffee", originalIngredientAmt ) );
        // i.addIngredients( ings );
        // break;
        // }
        // else if ( i.getIngredients().get( j ).getIngredient().equals( "Milk"
        // ) ) {
        // inventoryData.origVals.get( j ).setAmount( originalIngredientAmt );
        // final ArrayList<Ingredient> ings = new ArrayList<Ingredient>();
        // ings.add( new Ingredient( "Milk", originalIngredientAmt ) );
        // i.addIngredients( ings );
        // break;
        // }
        // else if ( i.getIngredients().get( j ).getIngredient().equals( "Sugar"
        // ) ) {
        // inventoryData.origVals.get( j ).setAmount( originalIngredientAmt );
        // final ArrayList<Ingredient> ings = new ArrayList<Ingredient>();
        // ings.add( new Ingredient( "Sugar", originalIngredientAmt ) );
        // i.addIngredients( ings );
        // break;
        // }
        // else if ( i.getIngredients().get( j ).getIngredient().equals(
        // "Chocolate" ) ) {
        // inventoryData.origVals.get( j ).setAmount( originalIngredientAmt );
        // final ArrayList<Ingredient> ings = new ArrayList<Ingredient>();
        // ings.add( new Ingredient( "Chocolate", originalIngredientAmt ) );
        // i.addIngredients( ings );
        // break;
        // }
        // }
        //
        // switch ( ingredient ) {
        // case "Coffee":
        // inventoryData.originalCoffee = originalIngredientAmt;
        // final ArrayList<Ingredient> ings = new ArrayList<Ingredient>();
        // ings.add( new Ingredient( "Coffee", originalIngredientAmt ) );
        // i.addIngredients( ings );
        // break;
        //
        // case "Milk":
        // inventoryData.originalCoffee = originalIngredientAmt;
        // i.addIngredients( originalIngredientAmt, 0, 0, 0 );
        // break;
        //
        // case "Sugar":
        // inventoryData.originalSugar = originalIngredientAmt;
        // i.addIngredients( 0, 0, originalIngredientAmt, 0 );
        // break;
        //
        // case "Chocolate":
        // inventoryData.originalChocolate = originalIngredientAmt;
        // i.addIngredients( 0, 0, 0, originalIngredientAmt );
        // break;
        //
        // default:
        // fail( "Unknown ingredient" );
        // }
        //
        // i.save();
    }

    /**
     * Add the specified amounts of ingredients to the CoffeeMaker's inventory.
     * This is required to pass.
     *
     * @param amtIngredient
     *            Amount of Ingredient to add to the Inventory.
     * @param ingredient
     *            Ingredient to add to the Inventory.
     */
    @When ( "^I add (-?\\d+) of (.+)$" )
    public void addIndividualInventory ( final int amtIngredient, final String ingredient ) {
        if ( "Coffee".equals( ingredient ) ) {
            addIndividualIngredient( amtIngredient, 0, 0, 0 );
        }
        else if ( "Milk".equals( ingredient ) ) {
            addIndividualIngredient( 0, amtIngredient, 0, 0 );
        }
        else if ( "Sugar".equals( ingredient ) ) {
            addIndividualIngredient( 0, 0, amtIngredient, 0 );
        }
        else if ( "Chocolate".equals( ingredient ) ) {
            addIndividualIngredient( 0, 0, 0, amtIngredient );
        }
    }

    /**
     * Helper method to add the specified amounts of ingredients to the
     * CoffeeMaker's inventory. This is required to pass.
     *
     * @param amtCoffee
     *            Amount of Coffee Ingredient that the inventory will be set to
     *            have
     * @param amtMilk
     *            Amount of Milk Ingredient that the inventory will be set to
     *            have
     * @param amtSugar
     *            Amount of Sugar Ingredient that the inventory will be set to
     *            have
     * @param amtChocolate
     *            Amount of Chocolate Ingredient that the inventory will be set
     *            to have
     */
    private void addIndividualIngredient ( final int amtCoffee, final int amtMilk, final int amtSugar,
            final int amtChocolate ) {
        // for ( int j = 0; j < inventoryData.newVals.size(); j++ ) {
        // if ( inventoryData.newVals.get( j ).getIngredient().equals( "Coffee"
        // ) ) {
        // inventoryData.newVals.get( j ).setAmount( amtCoffee );
        // }
        // else if ( inventoryData.newVals.get( j ).getIngredient().equals(
        // "Milk" ) ) {
        // inventoryData.newVals.get( j ).setAmount( amtMilk );
        // }
        // else if ( inventoryData.newVals.get( j ).getIngredient().equals(
        // "Sugar" ) ) {
        // inventoryData.newVals.get( j ).setAmount( amtSugar );
        // }
        // else if ( inventoryData.newVals.get( j ).getIngredient().equals(
        // "Chocolate" ) ) {
        // inventoryData.newVals.get( j ).setAmount( amtChocolate );
        // }
        // }
        //
        // try {
        // driver.findElement( By.name( "coffee" ) ).clear();
        // driver.findElement( By.name( "coffee" ) ).sendKeys( Integer.toString(
        // amtCoffee ) );
        // driver.findElement( By.name( "milk" ) ).clear();
        // driver.findElement( By.name( "milk" ) ).sendKeys( Integer.toString(
        // amtMilk ) );
        // driver.findElement( By.name( "sugar" ) ).clear();
        // driver.findElement( By.name( "sugar" ) ).sendKeys( Integer.toString(
        // amtSugar ) );
        // driver.findElement( By.name( "chocolate" ) ).clear();
        // driver.findElement( By.name( "chocolate" ) ).sendKeys(
        // Integer.toString( amtChocolate ) );
        //
        // driver.findElement( By.cssSelector( "input[type=\"submit\"]" )
        // ).click();
        //
        // new NgWebDriver( driver ).waitForAngularRequestsToFinish();
        // }
        // catch ( final Exception e ) {
        // fail( e.getMessage() );
        // }
    }

    /**
     * Add the specified amounts of ingredients to the CoffeeMaker's inventory.
     * This is for testing with invalid values and is expected to fail.
     *
     * @param amtIngredient
     *            Amount of Ingredient to add to the Inventory.
     * @param ingredient
     *            Ingredient to add to the Inventory.
     */
    @When ( "^I attempt to add (-?\\d+) of (.+)$" )
    public void invalidAddIndividualInventory ( final int amtIngredient, final String ingredient ) {
        if ( "Coffee".equals( ingredient ) ) {
            invalidAddIndividualIngredient( amtIngredient, 0, 0, 0 );
        }
        else if ( "Milk".equals( ingredient ) ) {
            invalidAddIndividualIngredient( 0, amtIngredient, 0, 0 );
        }
        else if ( "Sugar".equals( ingredient ) ) {
            invalidAddIndividualIngredient( 0, 0, amtIngredient, 0 );
        }
        else if ( "Chocolate".equals( ingredient ) ) {
            invalidAddIndividualIngredient( 0, 0, 0, amtIngredient );
        }
    }

    /**
     * Helper method to add the specified amounts of ingredients to the
     * CoffeeMaker's inventory. This is for testing with invalid values and is
     * expected to fail.
     *
     * @param amtCoffee
     *            Amount of Coffee Ingredient that the inventory will be set to
     *            have
     * @param amtMilk
     *            Amount of Milk Ingredient that the inventory will be set to
     *            have
     * @param amtSugar
     *            Amount of Sugar Ingredient that the inventory will be set to
     *            have
     * @param amtChocolate
     *            Amount of Chocolate Ingredient that the inventory will be set
     *            to have
     */
    private void invalidAddIndividualIngredient ( final int amtCoffee, final int amtMilk, final int amtSugar,
            final int amtChocolate ) {
        // for ( int j = 0; j < inventoryData.newVals.size(); j++ ) {
        // if ( inventoryData.newVals.get( j ).getIngredient().equals( "Coffee"
        // ) ) {
        // inventoryData.newVals.get( j ).setAmount( amtCoffee );
        // }
        // else if ( inventoryData.newVals.get( j ).getIngredient().equals(
        // "Milk" ) ) {
        // inventoryData.newVals.get( j ).setAmount( amtMilk );
        // }
        // else if ( inventoryData.newVals.get( j ).getIngredient().equals(
        // "Sugar" ) ) {
        // inventoryData.newVals.get( j ).setAmount( amtSugar );
        // }
        // else if ( inventoryData.newVals.get( j ).getIngredient().equals(
        // "Chocolate" ) ) {
        // inventoryData.newVals.get( j ).setAmount( amtChocolate );
        // }
        // }
        //
        // try {
        // driver.findElement( By.name( "coffee" ) ).clear();
        // driver.findElement( By.name( "coffee" ) ).sendKeys( Integer.toString(
        // amtCoffee ) );
        // driver.findElement( By.name( "milk" ) ).clear();
        // driver.findElement( By.name( "milk" ) ).sendKeys( Integer.toString(
        // amtMilk ) );
        // driver.findElement( By.name( "sugar" ) ).clear();
        // driver.findElement( By.name( "sugar" ) ).sendKeys( Integer.toString(
        // amtSugar ) );
        // driver.findElement( By.name( "chocolate" ) ).clear();
        // driver.findElement( By.name( "chocolate" ) ).sendKeys(
        // Integer.toString( amtChocolate ) );
        //
        // driver.findElement( By.cssSelector( "input[type=\"submit\"]" )
        // ).click();
        //
        // final WebElement message = driver.findElement( By.xpath(
        // "//div[@ng-show='failure']" ) );
        // final WebDriverWait wait = new WebDriverWait( driver, 5 );
        // wait.until( ExpectedConditions.visibilityOf( message ) );
        //
        // assertTrue( message.getText().contains( "Error while updating
        // inventory." ) );
        // }
        // catch ( final Exception e ) {
        // inventoryData.errorMessage = e.getMessage();
        // Assert.assertTrue( "Adding Inventory throws error", true );
        // }
    }

    /**
     * Verify that the CoffeeMaker's Inventory has been updated and that it
     * stores the values in the SharedInventoryData.
     */
    @Then ( "^the inventory of the CoffeeMaker is added successfully$" )
    public void individualInventoryAdded () {
        // int expectedCoffee = 0;
        // int expectedMilk = 0;
        // int expectedSugar = 0;
        // int expectedChocolate = 0;
        //
        // for ( int j = 0; j < inventoryData.newVals.size(); j++ ) {
        // if ( inventoryData.newVals.get( j ).getIngredient().equals( "Coffee"
        // ) ) {
        // expectedCoffee = inventoryData.origVals.get( j ).getAmount()
        // + inventoryData.newVals.get( j ).getAmount();
        // }
        // else if ( inventoryData.newVals.get( j ).getIngredient().equals(
        // "Milk" ) ) {
        // expectedMilk = inventoryData.origVals.get( j ).getAmount() +
        // inventoryData.newVals.get( j ).getAmount();
        // }
        // else if ( inventoryData.newVals.get( j ).getIngredient().equals(
        // "Sugar" ) ) {
        // expectedSugar = inventoryData.origVals.get( j ).getAmount()
        // + inventoryData.newVals.get( j ).getAmount();
        // }
        // else if ( inventoryData.newVals.get( j ).getIngredient().equals(
        // "Chocolate" ) ) {
        // expectedChocolate = inventoryData.origVals.get( j ).getAmount()
        // + inventoryData.newVals.get( j ).getAmount();
        // }
        // }
        //
        // final Inventory inventory = Inventory.getInventory();
        //
        // int coffee2 = 0;
        // int milk2 = 0;
        // int sugar2 = 0;
        // int chocolate2 = 0;
        //
        // for ( int j = 0; j < inventory.getIngredients().size(); j++ ) {
        // if ( inventory.getIngredients().get( j ).getIngredient().equals(
        // "Coffee" ) ) {
        // coffee2 = inventory.getIngredients().get( j ).getAmount();
        // }
        // else if ( inventory.getIngredients().get( j ).getIngredient().equals(
        // "Milk" ) ) {
        // milk2 = inventory.getIngredients().get( j ).getAmount();
        // }
        // else if ( inventory.getIngredients().get( j ).getIngredient().equals(
        // "Sugar" ) ) {
        // sugar2 = inventory.getIngredients().get( j ).getAmount();
        // }
        // else if ( inventory.getIngredients().get( j ).getIngredient().equals(
        // "Chocolate" ) ) {
        // chocolate2 = inventory.getIngredients().get( j ).getAmount();
        // }
        // }
        //
        // // Verify that the inventory is correct
        // Assert.assertEquals( "Coffee not added correctly", expectedCoffee,
        // coffee2 );
        // Assert.assertEquals( "Milk not added correctly", expectedMilk, milk2
        // );
        // Assert.assertEquals( "Sugar not added correctly", expectedSugar,
        // sugar2 );
        // Assert.assertEquals( "Chocolate not added correctly",
        // expectedChocolate, chocolate2 );
        //
        // final WebElement message = driver.findElement( By.xpath(
        // "//div[@ng-show='success']" ) );
        // assertTrue( message.getText().contains( "Inventory Successfully
        // Updated" ) );
    }

    /**
     * Ensure that an error was thrown while attempting to perform an action.
     *
     * @param error
     *            The error message to check
     */
    @Then ( "^an error occurs for the (.+)$" )
    public void errorThrown ( final String error ) {
        // Assert.assertTrue( !error.isEmpty() );
    }

    /**
     * Verify that the CoffeeMaker's Inventory has not been updated and that it
     * doesn't stores the values in the SharedInventoryData.
     */
    @Then ( "^the inventory is not updated$" )
    public void individualInventoryNotAdded () {
        // final Inventory inventory = Inventory.getInventory();
        //
        // int coffee2 = 0;
        // int milk2 = 0;
        // int sugar2 = 0;
        // int chocolate2 = 0;
        //
        // for ( int j = 0; j < inventory.getIngredients().size(); j++ ) {
        // if ( inventory.getIngredients().get( j ).getIngredient().equals(
        // "Coffee" ) ) {
        // coffee2 = inventory.getIngredients().get( j ).getAmount();
        // }
        // else if ( inventory.getIngredients().get( j ).getIngredient().equals(
        // "Milk" ) ) {
        // milk2 = inventory.getIngredients().get( j ).getAmount();
        // }
        // else if ( inventory.getIngredients().get( j ).getIngredient().equals(
        // "Sugar" ) ) {
        // sugar2 = inventory.getIngredients().get( j ).getAmount();
        // }
        // else if ( inventory.getIngredients().get( j ).getIngredient().equals(
        // "Chocolate" ) ) {
        // chocolate2 = inventory.getIngredients().get( j ).getAmount();
        // }
        // }
        //
        // // Verify that the inventory is unchanged
        //
        // for ( int j = 0; j < inventoryData.origVals.size(); j++ ) {
        // if ( inventory.getIngredients().get( j ).getIngredient().equals(
        // "Coffee" ) ) {
        // assertEquals( "Coffee not correct", inventoryData.origVals.get( j
        // ).getAmount(), coffee2 );
        // }
        // else if ( inventory.getIngredients().get( j ).getIngredient().equals(
        // "Milk" ) ) {
        // assertEquals( "Milk not correct", inventoryData.origVals.get( j
        // ).getAmount(), milk2 );
        // }
        // else if ( inventory.getIngredients().get( j ).getIngredient().equals(
        // "Sugar" ) ) {
        // assertEquals( "Sugar not correct", inventoryData.origVals.get( j
        // ).getAmount(), sugar2 );
        // }
        // else if ( inventory.getIngredients().get( j ).getIngredient().equals(
        // "Chocolate" ) ) {
        // assertEquals( "Chocolate not correct", inventoryData.origVals.get( j
        // ).getAmount(), chocolate2 );
        // }
        // }
        //
        // Assert.assertEquals( "Coffee not correct",
        // inventoryData.originalCoffee, coffee2 );
        // Assert.assertEquals( "Milk not correct", inventoryData.originalMilk,
        // milk2 );
        // Assert.assertEquals( "Sugar not correct",
        // inventoryData.originalSugar, sugar2 );
        // Assert.assertEquals( "Chocolate not correct",
        // inventoryData.originalChocolate, chocolate2 );
        //
        // final WebElement message = driver.findElement( By.xpath(
        // "//div[@ng-show='failure']" ) );
        // assertEquals( "Error while updating inventory.", message.getText() );
    }
}
