package edu.ncsu.csc.selenium;

import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;

/**
 * Tests add ingredient functionality.
 *
 * @author Alex Snezhko
 * @author Dominic Brown
 * @author Selena Chen
 */
public class AddIngredientsTest extends SeleniumTest {

    /** The URL for CoffeeMaker. */
    private static String baseUrl = "http://localhost:8080";

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
     * Helper method to add coffee as an ingredient.
     *
     * @param ingredientName
     *            Name of the ingredient
     * @param ingredientAmount
     *            Amount of the ingredient
     */
    private static void addCoffeeIngredient ( final String ingredientName, final String ingredientAmount ) {
        driver.get( baseUrl );
        driver.findElement( By.linkText( "Add Ingredient" ) ).click();

        driver.findElement( By.xpath( "//input[@ng-model=\"ingredient.ingredient\"]" ) ).clear();
        driver.findElement( By.xpath( "//input[@ng-model=\"ingredient.ingredient\"]" ) ).sendKeys( ingredientName );
        driver.findElement( By.xpath( "//input[@ng-model=\"ingredient.amount\"]" ) ).clear();
        driver.findElement( By.xpath( "//input[@ng-model=\"ingredient.amount\"]" ) ).sendKeys( ingredientAmount );

        driver.findElement( By.cssSelector( "input[type=\"submit\"]" ) ).click();
    }

    /**
     * Tests adding an ingredient.
     */
    @Test
    public void testAddIngredient1 () {
        addCoffeeIngredient( "Coffee", "50" );
        assertTextPresent( "Ingredient added successfully.", driver );
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
