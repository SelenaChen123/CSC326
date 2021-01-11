package edu.ncsu.csc.itrust2.cucumber;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Random;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.itrust2.models.persistent.User;

/**
 * Step definitions for adding a user feature
 */
public class AddUserStepDefs extends CucumberTest {

    /** The base url for the index of iTrust2 */
    private final String baseUrl      = "http://localhost:8080/iTrust2";
    /** A randomly defined username */
    private final String jenkinsUname = "jenkins" + ( new Random() ).nextInt();

    /**
     * Check for no user
     */
    @Given ( "The user does not already exist in the database" )
    public void noUser () {
        attemptLogout();

        final List<User> users = User.getUsers();
        for ( final User user : users ) {
            if ( user.getUsername().equals( jenkinsUname ) ) {
                try {
                    user.delete();
                }
                catch ( final Exception e ) {
                    Assert.fail();
                }
            }
        }
    }

    /**
     * Admin log in
     */
    @When ( "I log in as admin" )
    public void loginAdmin () {
        driver.get( baseUrl );
        final WebElement username = driver.findElement( By.name( "username" ) );
        username.clear();
        username.sendKeys( "admin" );
        final WebElement password = driver.findElement( By.name( "password" ) );
        password.clear();
        password.sendKeys( "123456" );
        final WebElement submit = driver.findElement( By.className( "btn" ) );
        submit.click();
    }

    /**
     * Navigate to add user page
     */
    @When ( "I navigate to the Add User page" )
    public void addUserPage () {
        ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('users').click();" );
    }

    /**
     * Fill in add user values
     */
    @When ( "^I fill in the values in the Add User form$" )
    public void fillFields () {
        final WebElement username = driver.findElement( By.id( "username" ) );
        username.clear();
        username.sendKeys( jenkinsUname );

        final WebElement password = driver.findElement( By.id( "password" ) );
        password.clear();
        password.sendKeys( "123456" );

        final WebElement password2 = driver.findElement( By.id( "password2" ) );
        password2.clear();
        password2.sendKeys( "123456" );

        final Select role = new Select( driver.findElement( By.id( "role" ) ) );
        role.selectByVisibleText( "Healthcare Provider" );

        final WebElement enabled = driver.findElement( By.name( "enabled" ) );
        enabled.click();

        driver.findElement( By.id( "submit" ) ).click();

    }

    /**
     * Fill in fields for creating a user with the given parameters
     *
     * @param name
     *            The username of the user
     * @param password
     *            The password of the user
     * @param confirmPassword
     *            The password to enter in the confirm password field
     * @param role
     *            The role of the user
     */
    @When ( "I enter (.+), (.+), (.+), and (.+) in the Add User form" )
    public void fillFieldsModular ( final String name, final String password, final String confirmPassword,
            final String role ) {
        final WebElement usernameField = driver.findElement( By.id( "username" ) );
        usernameField.clear();
        usernameField.sendKeys( name );

        final WebElement passwordField = driver.findElement( By.id( "password" ) );
        passwordField.clear();
        passwordField.sendKeys( password );

        final WebElement passwordField2 = driver.findElement( By.id( "password2" ) );
        passwordField2.clear();
        passwordField2.sendKeys( confirmPassword );

        final Select roleField = new Select( driver.findElement( By.id( "role" ) ) );
        roleField.selectByVisibleText( role );

        final WebElement enabled = driver.findElement( By.name( "enabled" ) );
        enabled.click();

        driver.findElement( By.id( "submit" ) ).click();

    }

    /**
     * Create user
     */
    @Then ( "The user is created successfully" )
    public void createdSuccessfully () {
        assertTrue( driver.getPageSource().contains( "User added successfully" ) );
    }

    /**
     * User is not created
     */
    @Then ( "The user is not created" )
    public void createdUnsuccessfully () {
        assertFalse( driver.findElement( By.id( "success" ) ).getText().contains( "User added successfully" ) );
    }

    /**
     * An error message is displayed
     *
     * @param problem
     *            The issue the error message is about
     */
    @Then ( "An error message is displayed about (.+)" )
    public void errorMessage ( final String problem ) {
        assertTrue( driver.findElement( By.id( "errP" ) ).getText().toLowerCase().contains( problem ) );
    }

    /**
     * User login
     */
    @Then ( "^The new user can login$" )
    public void tryLogin () {
        driver.findElement( By.id( "logout" ) ).click();

        waitForAngular();

        final WebElement username = driver.findElement( By.name( "username" ) );
        username.clear();
        username.sendKeys( jenkinsUname );
        final WebElement password = driver.findElement( By.name( "password" ) );
        password.clear();
        password.sendKeys( "123456" );
        final WebElement submit = driver.findElement( By.className( "btn" ) );
        submit.click();

        /**
         * Not an assert statement in the typical sense, but we know that we can
         * log in if we can find the "iTrust" button in the top-left after
         * attempting to do so.
         */
        try {
            waitForAngular();
            Thread.sleep( 500 );
            driver.findElement( By.linkText( "iTrust2" ) );
        }
        catch ( final Exception e ) {
            fail();
        }
    }

    /**
     * Login with fields from the parameters
     *
     * @param name
     *            Name to login with
     * @param password
     *            Password to login with
     */
    @Then ( "^The new user can login with (.+) and (.+)$" )
    public void tryLoginModular ( final String name, final String password ) {
        driver.findElement( By.id( "logout" ) ).click();

        final WebElement usernameField = driver.findElement( By.id( "username" ) );
        usernameField.clear();
        usernameField.sendKeys( name );

        final WebElement passwordField = driver.findElement( By.id( "password" ) );
        passwordField.clear();
        passwordField.sendKeys( password );

        final WebElement submit = driver.findElement( By.className( "btn" ) );
        submit.click();

        /*
         * Not an assert statement in the typical sense, but we know that we can
         * log in if we can find the "iTrust" button in the top-left after
         * attempting to do so.
         */
        try {
            waitForAngular();
            driver.findElement( By.linkText( "iTrust2" ) );
        }
        catch ( final Exception e ) {
            fail();
        }
    }
}
