package edu.ncsu.csc.coffee_maker.controllers;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Tests the InventoryController and that inventory is displayed and added
 * correctly.
 *
 * Reference: https://spring.io/guides/gs/testing-web/
 *
 * @author Kai Presler-Marshall
 * @author Elizabeth Gilbert
 * @author Sarah Heckman
 * @author Alex Snezhko
 * @author Dominic Brown
 * @author Selena Chen
 */
@RunWith ( SpringRunner.class )
@WebMvcTest ( MappingController.class )
public class InventoryControllerTest {

    @Autowired
    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API.
     */
    private MockMvc mockMvc;

    /**
     * Retrieves the Inventory page and ensures that the page, as expected, lets
     * us view the current inventory of the CoffeeMaker.
     *
     * @throws Exception
     *             If any errors persist
     */
    @Test
    public void testInventoryGet () throws Exception {
        assertTrue( this.mockMvc.perform( get( "/inventory" ) ).andDo( print() ).andExpect( status().isOk() )
                .andExpect( content().string( containsString( "View Inventory" ) ) ).andReturn().getResponse()
                .getContentAsString().contains( "View Inventory" ) );
    }
}
