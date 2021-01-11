package edu.ncsu.csc.selenium;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;

/**
 * Tests Delete Recipe functionality.
 *
 * @author Kai Presler-Marshall
 * @author Elizabeth Gilbert
 * @author Alex Snezhko
 * @author Dominic Brown
 * @author Selena Chen
 */
public class DeleteRecipeTest extends SeleniumTest {

    /** The URL for CoffeeMaker. */
    private String             baseUrl;

    /** Verification errors. */
    private final StringBuffer verificationErrors = new StringBuffer();

    /** Name of the recipe. */
    private String             recipeName;

    /**
     * {@inheritDoc}
     */
    @Override
    @Before
    protected void setUp () throws Exception {
        super.setUp();

        recipeName = "CoffeeRecipe";
        baseUrl = "http://localhost:8080";
        driver.manage().timeouts().implicitlyWait( 10, TimeUnit.SECONDS );
    }

    /**
     * Test to delete an existing, valid recipe. Expect to get a valid success
     * message stating the recipe was deleted.
     *
     * @throws Exception
     *             If any errors persist
     */
    @Test
    public void testDeleteRecipe1 () throws Exception {
        add();
        waitForAngular();
        driver.get( baseUrl + "" );
        driver.findElement( By.linkText( "Delete Recipe" ) ).click();

        // Select the recipe to delete and delete it.
        driver.findElement( By.cssSelector( "input[type=\"radio\"]" ) ).click();
        driver.findElement( By.cssSelector( "input[type=\"submit\"]" ) ).click();
        assertTextPresent( "Recipe deleted successfully", driver );

        driver.findElement( By.linkText( "Home" ) ).click();
    }

    /**
     * Tests adding a recipe to delete.
     *
     * @throws Exception
     *             If any errors persist
     */
    public void add () throws Exception {
        driver.get( baseUrl + "" );
        driver.findElement( By.linkText( "Add a Recipe" ) ).click();

        // Enter the recipe information
        driver.findElement( By.name( "name" ) ).clear();
        driver.findElement( By.name( "name" ) ).sendKeys( recipeName );
        driver.findElement( By.name( "price" ) ).clear();
        driver.findElement( By.name( "price" ) ).sendKeys( "50" );
        driver.findElement( By.name( "coffee" ) ).clear();
        driver.findElement( By.name( "coffee" ) ).sendKeys( "2" );
        driver.findElement( By.name( "milk" ) ).clear();
        driver.findElement( By.name( "milk" ) ).sendKeys( "1" );
        driver.findElement( By.name( "sugar" ) ).clear();
        driver.findElement( By.name( "sugar" ) ).sendKeys( "1" );
        driver.findElement( By.name( "chocolate" ) ).clear();
        driver.findElement( By.name( "chocolate" ) ).sendKeys( "0" );
        // Submit the recipe.
        driver.findElement( By.cssSelector( "input[type=\"submit\"]" ) ).click();

        // Make sure the proper message was displayed.
        assertTextPresent( "Recipe Created", driver );
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
