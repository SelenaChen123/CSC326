package edu.ncsu.csc.coffee_maker.controllers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

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
import edu.ncsu.csc.coffee_maker.models.persistent.Inventory;
import edu.ncsu.csc.coffee_maker.models.persistent.Recipe;

/**
 * Tests the coffee controller and that coffee is being made correctly.
 *
 * Reference: https://spring.io/guides/gs/testing-web/
 *
 * @author Alex Snezhko
 * @author Dominic Brown
 * @author Selena Chen
 */
@RunWith ( SpringRunner.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APICoffeeTest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API.
     */
    private MockMvc               mvc;

    /**
     * Context of the web application.
     */
    @Autowired
    private WebApplicationContext context;

    /**
     * Sets up the tests.
     */
    @Before
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
        Recipe.deleteAll( Recipe.class );
        Ingredient.deleteAll( Ingredient.class );
        IngredientType.deleteAll( IngredientType.class );
    }

    /**
     * Ensures recipes are created and updated properly.
     *
     * @throws Exception
     *             If any errors persist
     */
    @Test
    public void ensureRecipe () throws Exception {
        final String recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        final Recipe r = new Recipe();
        r.addIngredient( new Ingredient( new IngredientType( "Coffee" ), 3 ) );
        r.addIngredient( new Ingredient( new IngredientType( "Milk" ), 4 ) );
        r.addIngredient( new Ingredient( new IngredientType( "Sugar" ), 8 ) );
        r.addIngredient( new Ingredient( new IngredientType( "Chocolate" ), 5 ) );
        r.setPrice( 10 );
        r.setName( "Mocha" );

        if ( !recipe.contains( "Mocha" ) ) {
            mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                    .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );
        }

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isConflict() );

        String recipe2 = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        assertTrue( recipe2.contains( "Mocha" ) );

        recipe2 = mvc.perform( get( "/api/v1/recipes/Mocha" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        assertTrue( recipe2.contains( "Mocha" ) );

        mvc.perform( get( "/api/v1/inventory" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();

        final ArrayList<Ingredient> ings = new ArrayList<Ingredient>();
        ings.add( new Ingredient( new IngredientType( "Coffee" ), 50 ) );
        ings.add( new Ingredient( new IngredientType( "Milk" ), 50 ) );
        ings.add( new Ingredient( new IngredientType( "Sugar" ), 50 ) );
        ings.add( new Ingredient( new IngredientType( "Chocolate" ), 50 ) );
        final Inventory i = new Inventory( ings );

        mvc.perform( put( "/api/v1/inventory" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i ) ) ).andExpect( status().isOk() );

        mvc.perform( post( "/api/v1/makecoffee/Mocha" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( 100 ) ) ).andExpect( status().isOk() );
    }

    /**
     * Tests deleting recipes.
     *
     * @throws Exception
     *             If any errors persist
     */
    @Test
    public void deleteRecipe () throws Exception {
        String recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        if ( !recipe.contains( "Latte" ) ) {
            final Recipe r = new Recipe();

            r.addIngredient( new Ingredient( new IngredientType( "Coffee" ), 3 ) );
            r.addIngredient( new Ingredient( new IngredientType( "Milk" ), 4 ) );
            r.addIngredient( new Ingredient( new IngredientType( "Sugar" ), 8 ) );
            r.addIngredient( new Ingredient( new IngredientType( "Chocolate" ), 5 ) );
            r.setPrice( 10 );
            r.setName( "Latte" );

            mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                    .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );
        }

        recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertTrue( recipe.contains( "Latte" ) );

        mvc.perform( delete( "/api/v1/recipes/Latte" ) ).andExpect( status().isOk() );
        Thread.sleep( 2000 );

        final String recipe3 = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertFalse( recipe3.contains( "Latte" ) );

        mvc.perform( delete( "/api/v1/recipes/Latte" ) ).andExpect( status().isNotFound() );
    }

    /**
     * Tests creating recipes.
     *
     * @throws Exception
     *             If any errors persist
     */
    @Test
    public void createRecipe () throws Exception {
        DomainObject.deleteAll( Recipe.class );
        final Recipe r1 = new Recipe();
        r1.setName( "r1" );
        final String r1String = mvc
                .perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( r1 ) ) )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        assertTrue( r1String.contains( "r1" ) );

        final Recipe r2 = new Recipe();
        r2.setName( "r2" );
        final String r2String = mvc
                .perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( r2 ) ) )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        assertTrue( r2String.contains( "r2" ) );

        final Recipe r3 = new Recipe();
        r3.setName( "r3" );
        final String r3String = mvc
                .perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( r3 ) ) )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        assertTrue( r3String.contains( "r3" ) );

        final Recipe r4 = new Recipe();
        r4.setName( "r4" );
        final String r4String = mvc
                .perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( r4 ) ) )
                .andExpect( status().isInsufficientStorage() ).andReturn().getResponse().getContentAsString();
        assertTrue( r4String.contains( "r4" ) );
    }
}
