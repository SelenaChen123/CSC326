package edu.ncsu.csc.coffee_maker;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Tests the front page of the CoffeeMaker application loads correctly and has
 * all expected links.
 *
 * Reference: https://spring.io/guides/gs/testing-web/
 *
 * @author Sarah Heckman
 * @author Alex Snezhko
 * @author Dominic Brown
 * @author Selena Chen
 */
@RunWith ( SpringRunner.class )
@SpringBootTest ( webEnvironment = WebEnvironment.RANDOM_PORT )
public class HttpRequestTest {

    /** Port to communicate with the server. */
    @LocalServerPort
    private int              port;

    /** Rest template. */
    @Autowired
    private TestRestTemplate restTemplate;

    /**
     * Tests that the main page contains the needed items.
     */
    @Test
    public void testCoffeeMakerIndex () {
        assertThat( this.restTemplate.getForObject( "http://localhost:" + port + "/", String.class ) )
                .contains( "Coffee Maker" );
    }
}
