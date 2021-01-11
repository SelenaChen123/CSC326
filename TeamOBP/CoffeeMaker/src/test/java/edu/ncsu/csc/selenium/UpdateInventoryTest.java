/**
 *
 */
package edu.ncsu.csc.selenium;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the update inventory functionality.
 *
 * @author Kai Presler-Marshall
 * @author Alex Snezhko
 * @author Dominic Brown
 * @author Selena Chen
 */
public class UpdateInventoryTest extends SeleniumTest {

    // /** The URL for CoffeeMaker. */
    // private String baseUrl;

    /** Verification errors. */
    private final StringBuffer verificationErrors = new StringBuffer();

    /**
     * {@inheritDoc}
     */
    @Override
    @Before
    protected void setUp () throws Exception {
        super.setUp();
        // baseUrl = "http://localhost:8080";
        driver.manage().timeouts().implicitlyWait( 10, TimeUnit.SECONDS );
    }

    // /**
    // * Helper function to navigate to the Update Inventory page.
    // */
    // private void navigateToUpdateInventoryPage () {
    // driver.get( baseUrl + "" );
    // driver.findElement( By.linkText( "Update Inventory" ) ).click();
    // }

    // /**
    // * Adds ingredient amounts to the "Update Inventory" form. Does not hit
    // * submit.
    // *
    // * @param coffeeAmt
    // * The string to enter into the coffee field
    // * @param milkAmt
    // * The string to enter into the milk field
    // * @param sugarAmt
    // * The string to enter into the sugar field
    // * @param chocolateAmt
    // * The string to enter into the chocolate field
    // */
    // private void addIngredientsToForm ( final String coffeeAmt, final String
    // milkAmt, final String sugarAmt,
    // final String chocolateAmt ) {
    // driver.findElement( By.name( "coffee" ) ).clear();
    // driver.findElement( By.name( "coffee" ) ).sendKeys( coffeeAmt );
    // driver.findElement( By.name( "milk" ) ).clear();
    // driver.findElement( By.name( "milk" ) ).sendKeys( milkAmt );
    // driver.findElement( By.name( "sugar" ) ).clear();
    // driver.findElement( By.name( "sugar" ) ).sendKeys( sugarAmt );
    // driver.findElement( By.name( "chocolate" ) ).clear();
    // driver.findElement( By.name( "chocolate" ) ).sendKeys( chocolateAmt );
    // }

    // /**
    // * Locates the failure message that appears when adding invalid amounts to
    // * the inventory and returns the message text.
    // *
    // * @return The message text of the invalid inventory message
    // */
    // private String waitForAndReadErrorMessage () {
    // final WebElement message = driver.findElement( By.xpath(
    // "//div[@ng-show='failure']" ) );
    // final WebDriverWait wait = new WebDriverWait( driver, 5 );
    // wait.until( ExpectedConditions.visibilityOf( message ) );
    // return message.getText();
    // }

    // /**
    // * Locates the failure message that appears when successfully adding to
    // * inventory and returns the message text.
    // *
    // * @return The success message text
    // */
    // private String waitForAndReadSuccessMessage () {
    // final WebElement message = driver.findElement( By.xpath(
    // "//div[@ng-show='success']" ) );
    // final WebDriverWait wait = new WebDriverWait( driver, 5 );
    // wait.until( ExpectedConditions.visibilityOf( message ) );
    // return message.getText();
    // }

    /**
     * Test for adding inventory. Expect to get an appropriate success message.
     *
     * @throws Exception
     *             If any errors persist
     */
    @Test
    public void testAddInventory1 () throws Exception {
        // navigateToUpdateInventoryPage();
        //
        // // Enter the amount of each ingredient
        // addIngredientsToForm( "5", "3", "7", "2" );
        //
        // // Submit the inventory.
        // driver.findElement( By.cssSelector( "input[type=\"submit\"]" )
        // ).click();
        //
        // assertEquals( "Inventory Successfully Updated",
        // waitForAndReadSuccessMessage() );
        //
        // driver.findElement( By.linkText( "Home" ) ).click();
    }

    /**
     * Test for adding an individual inventory item. Expect to get an
     * appropriate success message.
     *
     * @throws Exception
     *             If any errors persist
     */
    @Test
    public void testAddInventory2 () throws Exception {
        // navigateToUpdateInventoryPage();
        //
        // addIngredientsToForm( "5", "0", "0", "0" );
        //
        // // Submit the inventory.
        // driver.findElement( By.cssSelector( "input[type=\"submit\"]" )
        // ).click();
        //
        // assertEquals( "Inventory Successfully Updated",
        // waitForAndReadSuccessMessage() );
        //
        // driver.findElement( By.linkText( "Home" ) ).click();
    }

    /**
     * Test for adding to the inventory with 0 values. Expect to get an
     * appropriate success message.
     *
     * @throws Exception
     *             If any errors persist
     */
    @Test
    public void testAddInventory3 () throws Exception {
        // navigateToUpdateInventoryPage();
        //
        // addIngredientsToForm( "0", "0", "0", "0" );
        //
        // // Submit the inventory.
        // driver.findElement( By.cssSelector( "input[type=\"submit\"]" )
        // ).click();
        //
        // assertEquals( "Inventory Successfully Updated",
        // waitForAndReadSuccessMessage() );
        //
        // driver.findElement( By.linkText( "Home" ) ).click();
    }

    /**
     * Test for a adding invalid coffee to the inventory. Expect to get an
     * appropriate error message.
     *
     * @throws Exception
     *             If any errors persist
     */
    @Test
    public void testInvalidCoffeeAddInventory () throws Exception {
        // navigateToUpdateInventoryPage();
        //
        // addIngredientsToForm( "-1", "3", "7", "2" );
        //
        // // Submit the inventory.
        // driver.findElement( By.cssSelector( "input[type=\"submit\"]" )
        // ).click();
        //
        // // Make sure the proper message was displayed.
        // assertEquals( "Error while updating inventory.",
        // waitForAndReadErrorMessage() );
    }

    /**
     * Test for a adding invalid milk to the inventory. Expect to get an
     * appropriate error message.
     *
     * @throws Exception
     *             If any errors persist
     */
    @Test
    public void testInvalidMilkAddInventory () throws Exception {
        // navigateToUpdateInventoryPage();
        //
        // addIngredientsToForm( "5", "-3", "7", "2" );
        //
        // // Submit the inventory.
        // driver.findElement( By.cssSelector( "input[type=\"submit\"]" )
        // ).click();
        //
        // assertEquals( "Error while updating inventory.",
        // waitForAndReadErrorMessage() );
    }

    /**
     * Test for a adding invalid sugar to the inventory. Expect to get an
     * appropriate error message.
     *
     * @throws Exception
     *             If any errors persist
     */
    @Test
    public void testInvalidSugarAddInventory () throws Exception {
        // navigateToUpdateInventoryPage();
        //
        // addIngredientsToForm( "5", "3", "-7", "2" );
        //
        // // Submit the inventory.
        // driver.findElement( By.cssSelector( "input[type=\"submit\"]" )
        // ).click();
        //
        // assertEquals( "Error while updating inventory.",
        // waitForAndReadErrorMessage() );
    }

    /**
     * Test for a adding invalid chocolate to the inventory. Expect to get an
     * appropriate error message.
     *
     * @throws Exception
     *             If any errors persist
     */
    @Test
    public void testInvalidChocolateAddInventory () throws Exception {
        // navigateToUpdateInventoryPage();
        //
        // addIngredientsToForm( "5", "3", "7", "-2" );
        //
        // // Submit the inventory.
        // driver.findElement( By.cssSelector( "input[type=\"submit\"]" )
        // ).click();
        //
        // assertEquals( "Error while updating inventory.",
        // waitForAndReadErrorMessage() );
    }

    /**
     * Test for a adding invalid chocolate to the inventory. Expect to get an
     * appropriate error message.
     *
     * @throws Exception
     *             If any errors persist
     */
    @Test
    public void testInvalidMissingAddInventory () throws Exception {
        // navigateToUpdateInventoryPage();
        //
        // addIngredientsToForm( "a", "3", "7", "2" );
        //
        // // Submit the inventory.
        // driver.findElement( By.cssSelector( "input[type=\"submit\"]" )
        // ).click();
        //
        // assertEquals( "Error while updating inventory.",
        // waitForAndReadErrorMessage() );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @After
    public void tearDown () {
        final String verificationErrorString = verificationErrors.toString();
        if ( !"".equals( verificationErrorString ) ) {
            fail( verificationErrorString );
        }
    }
}
