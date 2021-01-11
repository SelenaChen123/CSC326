package edu.ncsu.csc.selenium;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import edu.ncsu.csc.coffee_maker.models.persistent.Ingredient;
import edu.ncsu.csc.coffee_maker.models.persistent.IngredientType;
import edu.ncsu.csc.coffee_maker.models.persistent.Inventory;
import edu.ncsu.csc.coffee_maker.models.persistent.Recipe;

/**
 * Tests Make Coffee functionality.
 *
 * @author Elizabeth Gilbert
 * @author Kai Presler-Marshall
 * @author Alex Snezhko
 * @author Dominic Brown
 * @author Selena Chen
 */
public class MakeCoffeeTest extends SeleniumTest {

    /** The URL for CoffeeMaker. */
    private String             baseUrl;

    /** Verification errors. */
    private final StringBuffer verificationErrors = new StringBuffer();

    /** Number of wait seconds. */
    private static final int   SECONDS_TO_WAIT    = 5;

    /**
     * {@inheritDoc}
     */
    @Override
    @Before
    protected void setUp () throws Exception {
        super.cleanDB();

        /* Create lots of inventory to use */
        final ArrayList<Ingredient> ings = new ArrayList<Ingredient>();
        ings.add( new Ingredient( new IngredientType( "Coffee" ), 500 ) );
        ings.add( new Ingredient( new IngredientType( "Milk" ), 500 ) );
        ings.add( new Ingredient( new IngredientType( "Sugar" ), 500 ) );
        ings.add( new Ingredient( new IngredientType( "Chocolate" ), 500 ) );
        final Inventory ivt = Inventory.getInventory();
        ivt.addIngredients( ings );
        ivt.save();

        super.setUp();
        baseUrl = "http://localhost:8080/";
        driver.manage().timeouts().implicitlyWait( 20, TimeUnit.SECONDS );
    }

    /**
     * Helper to create a recipe.
     *
     * @return Nhe name of the recipe
     * @throws Exception
     *             If there was an issue in submitting the recipe
     */
    private void createRecipe ( final String name, final int price, final int amtCoffee, final int amtMilk,
            final int amtSugar, final int amtChocolate ) throws Exception {
        final Recipe e = Recipe.getByName( name );
        if ( null != e ) {
            e.delete();
        }

        final Recipe recipe = new Recipe();
        recipe.setName( name );
        recipe.setPrice( price );
        recipe.addIngredient( new Ingredient( new IngredientType( "Coffee" ), amtCoffee ) );
        recipe.addIngredient( new Ingredient( new IngredientType( "Milk" ), amtMilk ) );
        recipe.addIngredient( new Ingredient( new IngredientType( "Sugar" ), amtSugar ) );
        recipe.addIngredient( new Ingredient( new IngredientType( "Chocolate" ), amtChocolate ) );

        recipe.save();

        Thread.sleep( 2000 );
    }

    /**
     * Looks through the list of available recipes and selects the specified
     * recipe.
     *
     * @param name
     *            Name of the recipe
     * @return True if found and selected, false if not
     * @throws InterruptedException
     *             If any errors persist
     */
    private boolean selectRecipe ( final String name ) throws InterruptedException {
        final List<WebElement> list = driver.findElements( By.name( "name" ) );
        Thread.sleep( 3000 );

        // Select the recipe
        for ( final WebElement we : list ) {
            if ( name.equals( we.getAttribute( "value" ) ) ) {
                we.click();
                // Wait for thread to perform operation
                while ( !we.isSelected() ) {
                    Thread.sleep( 1000 );
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Create valid coffee.
     *
     * @throws Exception
     *             If any errors persist
     */
    private void makeCoffee ( final String recipeName, final int price, final int amtCoffee, final int amtMilk,
            final int amtSugar, final int amtChocolate, final int paid, final String expectedMessage,
            final boolean success ) throws Exception {
        createRecipe( recipeName, price, amtCoffee, amtMilk, amtSugar, amtChocolate );

        driver.get( baseUrl + "" );
        driver.findElement( By.linkText( "Make Coffee" ) ).click();

        selectRecipe( recipeName );

        try {
            driver.findElement( By.name( "amtPaid" ) ).clear();
            driver.findElement( By.name( "amtPaid" ) ).sendKeys( paid + "" );
        }
        catch ( final Exception e ) {
            System.out.println( driver.getCurrentUrl() );
            System.out.println( driver.getPageSource() );
            Assert.fail();
        }

        // Submit
        System.out.println( recipeName + " " + price + " " + amtCoffee + " " + amtMilk + " " + " " + amtSugar + " "
                + amtChocolate + " " + paid + " " + expectedMessage );
        driver.findElement( By.cssSelector( "input[type=\"submit\"]" ) ).click();
        Thread.sleep( 1000 );

        final WebElement message = driver.findElement( By.className( success ? "success" : "failure" ) );

        assertNotNull( message );

        assertTrue( message.getText().contains( expectedMessage ) );
    }

    /**
     * Test for making coffee (valid) Expect to get an appropriate success
     * message.
     *
     * @throws Exception
     *             If any errors persist
     */
    @Test
    public void testValidMakeCoffee () throws Exception {
        makeCoffee( "Coffee", 60, 0, 3, 7, 2, 60, "Coffee was made", true );
        makeCoffee( "Coffee", 60, 5, 0, 7, 2, 60, "Coffee was made", true );
        makeCoffee( "Coffee", 60, 5, 3, 0, 2, 60, "Coffee was made", true );
        makeCoffee( "Coffee", 60, 5, 3, 0, 2, 60, "Coffee was made", true );
        makeCoffee( "Coffee", 60, 5, 3, 7, 0, 60, "Coffee was made", true );
        makeCoffee( "Coffee", 60, 5, 3, 7, 2, 100, "Coffee was made", true );
        makeCoffee( "Coffee", 60, 5, 3, 7, 2, 61, "Coffee was made", true );
    }

    /**
     * Test for making coffee (invalid) Expect to get an appropriate failure
     * message.
     *
     * @throws Exception
     *             If any errors persist
     */
    @Test
    public void testInvalidMakeCoffee () throws Exception {
        makeCoffee( "Coffee", 60, 0, 3, 7, 2, 59, "Error while making recipe", false );
        makeCoffee( "Coffee", 60, 5, 0, 7, 2, -1, "Error while making recipe", false );
    }

    /**
     * Tests making coffee and returning change.
     */
    @Test
    public void testMakeCoffeeChange () {
        Recipe.deleteAll( Recipe.class );

        Recipe toMake = Recipe.getByName( "Delicious Not-Coffee" );

        if ( null != toMake ) {
            toMake.delete();
        }

        toMake = new Recipe();
        toMake.setName( "Delicious Not-Coffee" );
        toMake.addIngredient( new Ingredient( new IngredientType( "Milk" ), 20 ) );
        toMake.addIngredient( new Ingredient( new IngredientType( "Sugar" ), 5 ) );
        toMake.addIngredient( new Ingredient( new IngredientType( "Chocolate" ), 10 ) );

        toMake.setPrice( 5 );

        toMake.save();
        driver.get( baseUrl + "makecoffee.html" );

        final WebElement radioButton = driver.findElement( By.cssSelector( "input[name='name']" ) );

        radioButton.click();

        final WebElement payment = driver.findElement( By.cssSelector( "input[name='amtPaid']" ) );

        payment.sendKeys( "50" );

        driver.findElement( By.cssSelector( "input[type=\"submit\"]" ) ).click();

        final WebElement message = driver.findElement( By.className( "success" ) );
        final WebDriverWait wait = new WebDriverWait( driver, SECONDS_TO_WAIT );
        wait.until( ExpectedConditions.visibilityOf( message ) );

        assertNotNull( message );

        assertEquals( "Coffee was made. Your change is 45.", message.getText() );
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
        /* Remove our inventory so we don't mess up any future test */
        Inventory.deleteAll( Inventory.class );
    }
}
