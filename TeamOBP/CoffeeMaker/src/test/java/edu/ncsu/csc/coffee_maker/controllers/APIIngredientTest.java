package edu.ncsu.csc.coffee_maker.controllers;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.coffee_maker.models.persistent.DomainObject;
import edu.ncsu.csc.coffee_maker.models.persistent.Ingredient;
import edu.ncsu.csc.coffee_maker.models.persistent.IngredientType;

@RunWith ( SpringRunner.class )
@SpringBootTest
@AutoConfigureMockMvc

/**
 * Tests the ingredient controller and that ingredients are being made
 * correctly.
 *
 * Reference: https://spring.io/guides/gs/testing-web/
 *
 * @author Alex Snezhko
 * @author Dominic Brown
 * @author Selena Chen
 */
public class APIIngredientTest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API.
     */
    private MockMvc               mvc;

    /** Context of the web application. */
    @Autowired
    private WebApplicationContext context;

    /**
     * Sets up the tests.
     */
    @Before
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
    }

    /**
     * Tests ingredient functionality.
     *
     * @throws Exception
     *             If any errors persist.
     */
    @Test
    public void test () throws Exception {
        DomainObject.deleteAll( Ingredient.class );
        final Ingredient in = new Ingredient( new IngredientType( "Coffee" ), 1 );
        assertEquals( "Coffee", in.getIngredient().getName() );
        assertEquals( 1, in.getAmount() );

        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( in ) ) ).andExpect( status().isOk() );

        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( in ) ) ).andExpect( status().isConflict() );

        in.setAmount( 3 );

        mvc.perform( put( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( in ) ) ).andExpect( status().isOk() );
    }
}
