package edu.ncsu.csc.itrust2.cucumber;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.itrust2.models.enums.State;
import edu.ncsu.csc.itrust2.models.persistent.Pharmacist;
import edu.ncsu.csc.itrust2.models.persistent.Pharmacy;
import edu.ncsu.csc.itrust2.models.persistent.User;

/**
 * Step defs file for frontend implementation of adding pharmacies and assigning
 * a pharmacist to a pharmacy
 *
 * @author Andrew Sauerbrei
 *
 */
public class AddPharmacyStepDefs extends CucumberTest {

    /** Base url for logging in */
    private final String baseUrl        = "http://localhost:8080/iTrust2";
    /** Name of the pharmacy we are using */
    private final String pharmacyName   = "Pharmacy";
    /** Name of the pharmacist we are using */
    private final String pharmacistName = "Pharmacist";

    /**
     * Pharmacy doesn't exist
     */
    @Given ( "The desired pharmacy doesn't exist" )
    public void noPharmacy () {
        attemptLogout();

        final List<Pharmacy> pharmacies = Pharmacy.getPharmacies();
        for ( final Pharmacy pharmacy : pharmacies ) {
            try {
                pharmacy.delete();
            }
            catch ( final Exception e ) {
                // Assume this pharmacy is in use & can't be deleted.
            }
        }

    }

    /**
     * Pharmacy does exist
     */
    @Given ( "The desired pharmacy exists" )
    public void pharmacy () {
        attemptLogout();

        boolean pharmacyFound = false;

        final List<Pharmacy> pharmacies = Pharmacy.getPharmacies();
        for ( final Pharmacy pharmacy : pharmacies ) {
            if ( ! ( pharmacy.getName() == pharmacyName ) ) {
                try {
                    pharmacy.delete();
                }
                catch ( final Exception e ) {
                    // Assume this pharmacy is in use & can't be deleted.
                }
            }
            else {
                pharmacyFound = true;
            }

        }

        if ( !pharmacyFound ) {
            final State state = State.NC;
            final Pharmacy p = new Pharmacy( pharmacyName, "123 Street St.", "27502", state );
            p.save();
        }
    }

    /**
     * Pharmacy does exist
     */
    @Given ( "there are pharmacists and pharmacies in the system" )
    public void pharmacyPharmacies () {
        attemptLogout();

        boolean pharmacyFound = false;
        boolean pharmacistFound = false;

        final List<Pharmacy> pharmacies = Pharmacy.getPharmacies();
        for ( final Pharmacy pharmacy : pharmacies ) {
            if ( ! ( pharmacy.getName() == pharmacyName ) ) {
                try {
                    pharmacy.delete();
                }
                catch ( final Exception e ) {
                    // Assume this pharmacy is in use & can't be deleted.
                }
            }
            else {
                pharmacyFound = true;
            }

        }

        if ( !pharmacyFound ) {
            final State state = State.NC;
            final Pharmacy p = new Pharmacy( pharmacyName, "123 Street St.", "27502", state );
            p.save();
        }

        final List<Pharmacist> pharmacists = Pharmacist.getPharmacists();
        for ( final Pharmacist pharmacist : pharmacists ) {
            if ( ! ( pharmacist.getFirstName() == pharmacyName ) ) {
                try {
                    pharmacist.delete();
                }
                catch ( final Exception e ) {
                    // Assume this pharmacist is in use & can't be deleted.
                }
            }
            else {
                pharmacistFound = true;
            }
        }

        if ( !pharmacistFound ) {
            final Pharmacist p = new Pharmacist();
            final User u = new User();
            u.setUsername( pharmacistName );
            u.setPassword( "123456" );
            u.save();
            // final State state = State.NC;
            p.setFirstName( pharmacistName );
            p.setAddress1( "123 Street St." );
            p.setCity( "Raleigh" );
            p.setZip( "27502" );
            // p.setState( state );
            p.setEnabled( true );

            p.save();
            p.setSelf( u );
        }
    }

    /**
     * Admin login
     */
    @When ( "I log in as the admin" )
    public void loginAdminH () {
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
     * Add pharmacist page
     */
    @When ( "I navigate to the Manage Locations page" )
    public void addPharmacyPage () {
        ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('deletehospital').click();" );
    }

    /**
     * Assign pharmacist page
     */
    @When ( "I navigate to the Assign Pharmacist page" )
    public void assignPharmacistPage () {
        ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('assignPharmacists').click();" );
    }

    /**
     * Deleting the specified pharmacy
     */
    @When ( "I find the pharmacy and I press the delete button" )
    public void deletePharmacy () {
        final WebElement pharmacies = driver.findElement( By.name( "Pharmacies" ) );
        pharmacies.click();

        final WebElement delete = driver.findElement( By.id( pharmacyName ) );
        delete.click();
        waitForAngular();
    }

    /**
     * Fill hosptial forms
     */
    @When ( "I fill in the values in the Add Pharmacy form" )
    public void fillPharmacyFields () {
        final WebElement pharmacies = driver.findElement( By.name( "Pharmacies" ) );
        pharmacies.click();

        final WebElement name = driver.findElement( By.id( "name" ) );
        name.clear();
        name.sendKeys( pharmacyName );

        final WebElement address = driver.findElement( By.id( "address" ) );
        address.clear();
        address.sendKeys( "123 Street St." );

        final WebElement state = driver.findElement( By.id( "state" ) );
        final Select dropdown = new Select( state );
        dropdown.selectByVisibleText( "North Carolina" );

        final WebElement zip = driver.findElement( By.id( "zip" ) );
        zip.clear();
        zip.sendKeys( "27502" );

        driver.findElement( By.id( "submit" ) ).click();
    }

    /**
     * Selecting pharmacy and pharmacist
     */
    @When ( "I select the pharmacist and pharmacy" )
    public void assignPharmacist () {
        waitForAngular();
        final WebElement pharmacy = driver.findElement( By.id( pharmacyName ) );
        pharmacy.click();

        final WebElement pharmacist = driver.findElement( By.id( pharmacistName ) );
        pharmacist.click();

        final WebElement submit = driver.findElement( By.name( "assignPharmacist" ) );
        submit.click();
        waitForAngular();
    }

    /**
     * Create pharmacy successfully
     */
    @Then ( "The Pharmacy is created successfully" )
    public void createdSuccessfully () {
        waitForAngular();
        assertTrue( driver.getPageSource().contains( "Pharmacy added successfully." ) );
    }

    /**
     * Checking for delete message
     */
    @Then ( "the pharmacy is deleted from the system" )
    public void deletedSuccessfully () {
        waitForAngular();
        assertTrue( driver.getPageSource().contains( "Pharmacy deleted successfully." ) );
    }

    /**
     * Checking for assign message
     */
    @Then ( "the pharmacist is assigned" )
    public void assignedSuccessfully () {
        waitForAngular();
        assertTrue( driver.getPageSource().contains( "Pharmacist assigned successfully." ) );
    }
}
