package edu.ncsu.csc.selenium;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.ChromeDriverManager;

/**
 * Handles the browser logic.
 *
 * @author Alex Snezhko
 * @author Dominic Brown
 * @author Selena Chen
 */
public class BrowserHandler {

    /** Chrome driver. */
    private final ChromeDriver    driver;

    /** BrowserHandler object. */
    static private BrowserHandler instance = new BrowserHandler();

    /** Type of OS. */
    static private String         OS;

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
     * Returns the Chrome driver.
     *
     * @return Chrome driver
     */
    public ChromeDriver getDriver () {
        return driver;
    }

    /**
     * Returns the BrowserHandler instance.
     *
     * @return BrowserHandler instance
     */
    static public BrowserHandler getInstance () {
        return instance;
    }

    /**
     * Private class that handles the browser logic.
     */
    private BrowserHandler () {

        OS = System.getProperty( "os.name" );

        ChromeDriverManager.chromedriver().setup();
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
}
