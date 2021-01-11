package edu.ncsu.csc.selenium;

import java.util.List;

import org.junit.BeforeClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import com.paulhammant.ngwebdriver.NgWebDriver;

import edu.ncsu.csc.coffee_maker.models.persistent.DomainObject;
import junit.framework.TestCase;

/**
 * Base Selenium test. Contains helper methods for checking text.
 *
 * @author Kai Presler-Marshall
 * @author Elizabeth Gilbert
 * @author Alex Snezhko
 * @author Dominic Brown
 * @author Selena Chen
 */
abstract class SeleniumTest extends TestCase {

    /** Type of OS. */
    final static private String OS = System.getProperty( "os.name" );

    /** Web driver. */
    static protected WebDriver  driver;

    /**
     * {@inheritDoc}
     */
    @BeforeClass
    protected static void cleanDB () {
        DomainObject.deleteAll( DomainObject.class );
    }

    @Override
    protected void setUp () throws Exception {
        driver = BrowserHandler.getInstance().getDriver();
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
     * Closes everything.
     */
    public void close () {
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

    /**
     * Asserts that the text is on the page.
     *
     * @param text
     *            Text to check
     * @param driver
     *            Web driver
     */
    public void assertTextPresent ( final String text, final WebDriver driver ) {
        final List<WebElement> list = driver.findElements( By.xpath( "//*[contains(text(),'" + text + "')]" ) );
        assertTrue( "Text not found!", list.size() > 0 );
    }

    /**
     * Asserts that the text is not on the page. Does not pause for text to
     * appear.
     *
     * @param text
     *            Text to check
     * @param driver
     *            Web driver
     */
    public void assertTextNotPresent ( final String text, final WebDriver driver ) {
        assertFalse( "Text should not be found!",
                driver.findElement( By.cssSelector( "BODY" ) ).getText().contains( text ) );
    }

    /**
     * Wait method that will let angular finish loading before continuing.
     */
    protected void waitForAngular () {
        new NgWebDriver( (ChromeDriver) driver ).waitForAngularRequestsToFinish();
    }
}
