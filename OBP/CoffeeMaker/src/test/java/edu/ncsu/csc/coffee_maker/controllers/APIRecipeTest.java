package edu.ncsu.csc.coffee_maker.controllers;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
import edu.ncsu.csc.coffee_maker.models.persistent.Recipe;

@RunWith ( SpringRunner.class )
@SpringBootTest
@AutoConfigureMockMvc

/**
 * Tests the recipe controller and that recipes are being made correctly.
 *
 * Reference: https://spring.io/guides/gs/testing-web/
 *
 * @author Selena Chen
 */
public class APIRecipeTest {

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
     * Tests getting a recipe.
     *
     * @throws Exception
     *             If any errors persist.
     */
    @Test
    public void testGet () throws Exception {
        DomainObject.deleteAll( Recipe.class );
        final Recipe r1 = new Recipe();
        r1.setName( "Black Coffee" );
        r1.setPrice( 1 );
        r1.addIngredient( new Ingredient( new IngredientType( "Coffee" ), 1 ) );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r1 ) ) ).andExpect( status().isOk() );

        final String recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertTrue( recipe.contains( "Black Coffee" ) );
    }

    /**
     * Tests creating a recipe.
     *
     * @throws Exception
     *             If any errors persist.
     */
    @Test
    public void testCreate () throws Exception {
        DomainObject.deleteAll( Recipe.class );
        final Recipe r1 = new Recipe();
        r1.setName( "Black Coffee" );
        r1.setPrice( 1 );
        r1.addIngredient( new Ingredient( new IngredientType( "Coffee" ), 1 ) );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r1 ) ) ).andExpect( status().isOk() );

        r1.setPrice( 10 );

        mvc.perform( put( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r1 ) ) ).andExpect( status().isOk() );
    }

    /**
     * Tests editing a recipe.
     *
     * @throws Exception
     *             If any errors persist.
     */
    @Test
    public void testEdit () throws Exception {
        DomainObject.deleteAll( Recipe.class );
        final Recipe r1 = new Recipe();
        r1.setName( "Black Coffee" );
        r1.setPrice( 1 );
        r1.addIngredient( new Ingredient( new IngredientType( "Coffee" ), 1 ) );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r1 ) ) ).andExpect( status().isOk() );

        r1.setPrice( 10 );

        mvc.perform( put( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r1 ) ) ).andExpect( status().isOk() );
    }

    /**
     * Tests deleting a recipe.
     *
     * @throws Exception
     *             If any errors persist.
     */
    @Test
    public void testDelete () throws Exception {
        DomainObject.deleteAll( Recipe.class );
        final Recipe r1 = new Recipe();
        r1.setName( "Black Coffee" );
        r1.setPrice( 1 );
        r1.addIngredient( new Ingredient( new IngredientType( "Coffee" ), 1 ) );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r1 ) ) ).andExpect( status().isOk() );

        mvc.perform( delete( "/api/v1/recipes/Black Coffee" ) ).andExpect( status().isOk() );

        mvc.perform( delete( "/api/v1/recipes/Black Coffee" ) ).andExpect( status().isConflict() );
    }
}
