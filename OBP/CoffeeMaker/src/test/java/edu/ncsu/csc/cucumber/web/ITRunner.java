package edu.ncsu.csc.cucumber.web;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

/**
 * Handles the before and after of the cucumber tests.
 *
 * @author Alex Snezhko
 * @author Dominic Brown
 * @author Selena Chen
 */
@RunWith ( Cucumber.class )
@CucumberOptions ( features = "src/test/resources/web" )
public class ITRunner {

    /**
     * Sets up the tests.
     */
    @BeforeClass
    public static void setUp () {
        CucumberTest.setup();
    }

    /**
     * Runs after the tests.
     */
    @AfterClass
    public static void tearDown () {
        // Empty for now
    }
}
