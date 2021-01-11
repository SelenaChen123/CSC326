package edu.ncsu.csc.cucumber.web;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.paulhammant.ngwebdriver.NgWebDriver;

import io.github.bonigarcia.wdm.ChromeDriverManager;

/**
 * Handles running the cucumber tests.
 *
 * @author Alex Snezhko
 * @author Dominic Brown
 * @author Selena Chen
 */
public abstract class CucumberTest {

    /** Sets up the Chrome drivers. */
    static {
        ChromeDriverManager.chromedriver().setup();
    }

    /** Name of the OS. */
    final static private String   OS = System.getProperty( "os.name" );

    /** Chrome driver. */
    static protected ChromeDriver driver;

    /**
     * Sets up the cucumber tests.
     */
    static public void setup () {
        final ChromeOptions options = new ChromeOptions();
        options.addArguments( "headless" );
        options.addArguments( "window-size=1200x600" );
        options.addArguments( "blink-settings=imagesEnabled=false" );

        if ( Linux() ) {
            options.setBinary( "/usr/bin/google-chrome" );
        }
        else if ( Windows() ) {
            options.setBinary( "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe" );
        }
        else if ( Mac() ) {
            options.setBinary( "/Applications/Google Chrome.app/Contents/MacOS/Google Chrome" );
        }

        driver = new ChromeDriver( options );
    }

    /**
     * Whether or not the OS is a Mac.
     *
     * @return True if the OS is a Mac, false otherwise
     */
    static private boolean Mac () {
        return OS.contains( "Mac OS X" );
    }

    /**
     * Whether or not the OS is a Linux.
     *
     * @return True if the OS is a Linux, false otherwise
     */
    static private boolean Linux () {
        return OS.contains( "Linux" );
    }

    /**
     * Whether or not the OS is a Windows.
     *
     * @return True if the OS is a Windows, false otherwise
     */
    static private boolean Windows () {
        return OS.contains( "Windows" );
    }

    /**
     * Runs after the cucumber tests.
     */
    static public void tearDown () {
        driver.close();
        driver.quit();

        if ( Windows() ) {
            windowsKill();
        }
        else if ( Linux() || Mac() ) {
            unixKill();
        }
    }

    /**
     * Waits for angular requests to finish.
     */
    protected void waitForAngular () {
        new NgWebDriver( driver ).waitForAngularRequestsToFinish();
    }

    /**
     * Kills Windows tasks.
     */
    static private void windowsKill () {
        try {
            Runtime.getRuntime().exec( "taskkill /f /im chrome.exe" );
            Runtime.getRuntime().exec( "taskkill /f /im chromedriver.exe" );
        }
        catch ( final Exception e ) {
        }
    }

    /**
     * Kills Unix tasks.
     */
    static private void unixKill () {
        try {
            Runtime.getRuntime().exec( "pkill -f chromium-browser" );
            Runtime.getRuntime().exec( "pkill -f chrome" );
            Runtime.getRuntime().exec( "pkill -f chromedriver" );
        }
        catch ( final Exception e ) {
        }
    }
}
