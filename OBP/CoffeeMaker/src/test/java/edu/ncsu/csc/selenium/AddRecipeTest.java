package edu.ncsu.csc.selenium;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;

/**
 * Tests Add Recipe functionality.
 *
 * @author Kai Presler-Marshall
 * @author Alex Snezhko
 * @author Dominic Brown
 * @author Selena Chen
 */
public class AddRecipeTest extends SeleniumTest {

    /** The URL for CoffeeMaker. */
    private String             baseUrl;

    /** Verification errors. */
    private final StringBuffer verificationErrors = new StringBuffer();

    /**
     * {@inheritDoc}
     */
    @Override
    @Before
    protected void setUp () throws Exception {
        super.setUp();
        baseUrl = "http://localhost:8080";
        driver.manage().timeouts().implicitlyWait( 10, TimeUnit.SECONDS );
    }

    /**
     * Helper method to add a recipe.
     */
    private void addRecipeHelper () {
        driver.get( baseUrl );
        driver.findElement( By.linkText( "Add a Recipe" ) ).click();

        // Enter the recipe information
        driver.findElement( By.name( "name" ) ).clear();
        driver.findElement( By.name( "name" ) ).sendKeys( "Coffee" );
        driver.findElement( By.name( "price" ) ).clear();
        driver.findElement( By.name( "price" ) ).sendKeys( "50" );
        driver.findElement( By.name( "coffee" ) ).clear();
        driver.findElement( By.name( "coffee" ) ).sendKeys( "3" );
        driver.findElement( By.name( "milk" ) ).clear();
        driver.findElement( By.name( "milk" ) ).sendKeys( "1" );
        driver.findElement( By.name( "sugar" ) ).clear();
        driver.findElement( By.name( "sugar" ) ).sendKeys( "1" );
        driver.findElement( By.name( "chocolate" ) ).clear();
        driver.findElement( By.name( "chocolate" ) ).sendKeys( "1" );

        // Submit the recipe.
        driver.findElement( By.cssSelector( "input[type=\"submit\"]" ) ).click();
    }

    /**
     * Tests successfully adding a recipe. Expect to get an appropriate success
     * message.
     *
     * @throws Exception
     *             If any errors persist
     */
    @Test
    public void testAddRecipe1 () throws Exception {
        addRecipeHelper();

        // Make sure the proper message was displayed.
        assertTextPresent( "Recipe Created", driver );

        System.out.println( "Recipe created" );
    }

    /**
     * Tests adding a duplicate recipe. Expect to get an appropriate error
     * message.
     *
     * @throws Exception
     *             If any errors persist
     */
    @Test
    public void testAddRecipe2 () throws Exception {
        addRecipeHelper();

        assertTextPresent( "Error while adding recipe", driver );
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

    /**
     * {@inheritDoc}
     */
    @AfterClass
    @Override
    public void close () {
        super.close();
    }
}
