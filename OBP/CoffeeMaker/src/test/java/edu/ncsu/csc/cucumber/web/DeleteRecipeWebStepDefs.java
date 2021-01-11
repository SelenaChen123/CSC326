package edu.ncsu.csc.cucumber.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.coffee_maker.models.persistent.DomainObject;
import edu.ncsu.csc.coffee_maker.models.persistent.Recipe;

/**
 * StepDefs (Cucumber) test class for the new delete recipe functionality.
 *
 * @author Alex Snezhko
 * @author Dominic Brown
 * @author Selena Chen
 */
public class DeleteRecipeWebStepDefs extends CucumberTest {

    /** Name of the recipe to be deleted. */
    private String recipeToDelete;

    /**
     * Navigates to the delete recipe link in the menu.
     */
    private void navigateToDeleteRecipe () {
        final String baseUrl = "http://localhost:8080";

        driver.get( baseUrl + "" );
        driver.findElement( By.linkText( "Delete Recipe" ) ).click();
        waitForAngular();
    }

    /**
     * Given there are numRecipes recipes in the CoffeeMaker.
     *
     * @param numRecipes
     *            Number of recipes in the CoffeeMaker
     */
    @Given ( "^there are (.+) recipes in the CoffeeMaker$" )
    public void initialRecipesRemove ( int numRecipes ) {
        DomainObject.deleteAll( Recipe.class );
        for ( int i = 0; i < numRecipes; i++ ) {
            final Recipe r = new Recipe();
            r.setName( "R" + i );
            r.save();
        }
    }

    /**
     * Given there is a recipe named recipeName in the CoffeeMaker.
     *
     * @param recipeName
     *            Name of the recipe
     */
    @Given ( "^there is a recipe named (.+) in the CoffeeMaker$" )
    public void initialRecipesAdd ( final String recipeName ) {
        final Recipe r = new Recipe();
        r.setName( recipeName );
        r.save();

        recipeToDelete = recipeName;
    }

    /**
     * Given I am on the Delete Recipe page.
     */
    @Given ( "^I am on the Delete Recipe page$" )
    public void initiallyOnDeleteRecipePage () {
        navigateToDeleteRecipe();
    }

    /**
     * When I navigate to the Delete Recipe page.
     */
    @When ( "^I navigate to the Delete Recipe page$" )
    public void goToDeleteRecipePage () {
        navigateToDeleteRecipe();
    }

    /**
     * When I select to delete this recipe.
     */
    @When ( "^I select to delete this recipe$" )
    public void deleteRecipe () {
        driver.findElement( By.xpath( "//input[@value='" + recipeToDelete + "']" ) ).click();
        driver.findElement( By.cssSelector( "input[type=\"submit\"]" ) ).click();
        waitForAngular();
    }

    /**
     * When I attempt to delete all recipes.
     */
    @When ( "^I attempt to delete all recipes$" )
    public void deleteAll () {
        driver.findElement( By.name( "all" ) ).click();
        driver.findElement( By.xpath( "//input[@type=\"submit\"]" ) ).click();
        waitForAngular();
    }

    /**
     * Then I see numRecipes recipes shown.
     *
     * @param numRecipes
     *            Number of recipes shown
     */
    @Then ( "^I see (\\d+) recipes shown$" )
    public void seeNumRecipes ( int numRecipes ) {
        final List<WebElement> list = driver.findElements( By.xpath( "//li[@ng-repeat='recipe in recipes']" ) );
        assertEquals( numRecipes, list.size() );

        driver.findElement( By.linkText( "Home" ) ).click();
    }

    /**
     * Then the recipe is deleted.
     */
    @Then ( "^the recipe is deleted$" )
    public void checkRecipeDeleted () {
        assertNull( Recipe.getByName( recipeToDelete ) );

        final WebElement message = driver.findElement( By.xpath( "//div[@ng-if='submissionSuccess']" ) );
        assertTrue( message.getText().contains( "Recipe deleted successfully" ) );

        driver.findElement( By.linkText( "Home" ) ).click();
    }

    /**
     * Then all recipes in the recipe book are deleted.
     */
    @Then ( "^all recipes in the recipe book are deleted$" )
    public void allDeleted () {
        final List<WebElement> list = driver.findElements( By.xpath( "//li[@ng-repeat='recipe in recipes']" ) );
        assertEquals( 0, list.size() );

        driver.findElement( By.linkText( "Home" ) ).click();
    }
}
