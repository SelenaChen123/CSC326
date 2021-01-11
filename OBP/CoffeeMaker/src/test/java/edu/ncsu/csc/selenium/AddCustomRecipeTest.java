package edu.ncsu.csc.selenium;

import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import edu.ncsu.csc.coffee_maker.controllers.APIIngredientController;
import edu.ncsu.csc.coffee_maker.models.persistent.Ingredient;
import edu.ncsu.csc.coffee_maker.models.persistent.IngredientType;

/**
 * Tests add custom recipe functionality.
 *
 * @author Alex Snezhko
 * @author Dominic Brown
 * @author Selena Chen
 */
public class AddCustomRecipeTest extends SeleniumTest {

    /** The URL for CoffeeMaker. */
    private String           baseUrl;

    /** Length of time out. */
    private static final int TIMEOUT_IN_SECONDS = 5;

    /**
     * {@inheritDoc}
     */
    @Override
    @Before
    protected void setUp () throws Exception {
        super.setUp();

        baseUrl = "http://localhost:8080";
        driver.manage().timeouts().implicitlyWait( 10, TimeUnit.SECONDS );

        final APIIngredientController api = new APIIngredientController();
        api.createIngredient( new Ingredient( new IngredientType( "Coughy" ), 30 ) );
    }

    /**
     * Navigates to the add custom recipe link in the menu.
     */
    private void navigateToAddCustomRecipe () {
        driver.get( baseUrl );
        driver.findElement( By.linkText( "Add a Custom Recipe" ) ).click();

        driver.findElement( By.name( "name" ) ).clear();
        driver.findElement( By.name( "name" ) ).sendKeys( "Coffee" );
        driver.findElement( By.name( "price" ) ).clear();
        driver.findElement( By.name( "price" ) ).sendKeys( "50" );
    }

    /**
     * Tests adding a custom recipe.
     *
     * @throws InterruptedException
     *             If any errors persist
     */
    @Test
    public void testAddCustomRecipe1 () throws InterruptedException {
        navigateToAddCustomRecipe();

        driver.findElements( By.cssSelector( "button[type=\"add-ingredient\"]" ) ).get( 0 ).click();
        final Select ingredient = new Select( driver.findElement( By.xpath( "//select" ) ) );
        ingredient.selectByVisibleText( "Coughy" );

        driver.findElement( By.xpath( "//input[@placeholder=\"Enter ingredient amount\"]" ) ).clear();
        driver.findElement( By.xpath( "//input[@placeholder=\"Enter ingredient amount\"]" ) ).sendKeys( "10" );

        driver.findElement( By.cssSelector( "input[type=\"submit\"]" ) ).click();

        final WebElement visibleSuccessMessage = driver
                .findElement( By.xpath( "//div[@ng-show='success' and @class!='ng-hide']" ) );
        final WebDriverWait wait = new WebDriverWait( driver, TIMEOUT_IN_SECONDS );
        wait.until( ExpectedConditions.visibilityOf( visibleSuccessMessage ) );

        assertEquals( "Recipe Created", visibleSuccessMessage.getText() );
    }

    @Test
    public void testInvalidAddCustomRecipe () {
        navigateToAddCustomRecipe();

        driver.findElements( By.cssSelector( "button[type=\"add-ingredient\"]" ) ).get( 0 ).click();
        final Select ingredient = new Select( driver.findElement( By.xpath( "//select" ) ) );
        ingredient.selectByVisibleText( "Coughy" );

        driver.findElement( By.xpath( "//input[@placeholder=\"Enter ingredient amount\"]" ) ).clear();
        driver.findElement( By.xpath( "//input[@placeholder=\"Enter ingredient amount\"]" ) ).sendKeys( "-1" );

        driver.findElement( By.cssSelector( "input[type=\"submit\"]" ) ).click();

        final WebElement visibleFailureMessage = driver
                .findElement( By.xpath( "//div[@ng-show='failure' and @class!='ng-hide']" ) );
        final WebDriverWait wait = new WebDriverWait( driver, TIMEOUT_IN_SECONDS );
        wait.until( ExpectedConditions.visibilityOf( visibleFailureMessage ) );

        assertEquals( "Error while adding recipe.", visibleFailureMessage.getText() );
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
