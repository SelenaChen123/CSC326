package edu.ncsu.csc.coffee_maker.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

import edu.ncsu.csc.coffee_maker.models.persistent.Ingredient;
import edu.ncsu.csc.coffee_maker.models.persistent.IngredientType;
import edu.ncsu.csc.coffee_maker.models.persistent.Recipe;

/**
 * Perform a quick check of one of the API methods to ensure that the API
 * controller is up and receiving requests as it should be.
 *
 * @author Kai Presler-Marshall
 * @author Alex Snezhko
 * @author Dominic Brown
 * @author Selena Chen
 */
@RunWith ( SpringRunner.class )
@SpringBootTest ( properties = "logging.level.org.springframework.web=DEBUG" )
@AutoConfigureMockMvc
public class APITest {

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
     * Tests that we are able to make a call to the REST API. If such a call
     * cannot be made, throws an exception instead.
     *
     * @throws Exception
     *             If any errors persist
     */
    @Test
    public void testApi () throws Exception {
        /*
         * Verify that we are able to make a request to the Inventory API
         * endpoint and that we get a 400 (OK) status in return.
         */
        assertNotNull( mvc.perform( get( "/api/v1/inventory" ) ).andExpect( status().isOk() ).andReturn().getResponse()
                .getContentAsString() );
    }

    /**
     * Tests that we are able to make a call to the REST API to create a recipe.
     *
     * @throws Exception
     *             If any errors persist
     */
    @Test
    public void testRecipeApi () throws Exception {
        Recipe.deleteAll( Recipe.class );

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );

        recipe.addIngredient( new Ingredient( new IngredientType( "Coffee" ), 1 ) );
        recipe.addIngredient( new Ingredient( new IngredientType( "Milk" ), 20 ) );
        recipe.addIngredient( new Ingredient( new IngredientType( "Sugar" ), 5 ) );
        recipe.addIngredient( new Ingredient( new IngredientType( "Chocolate" ), 10 ) );

        recipe.setPrice( 5 );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe ) ) );

        assertEquals( 1, Recipe.getAll().size() );
    }
}
