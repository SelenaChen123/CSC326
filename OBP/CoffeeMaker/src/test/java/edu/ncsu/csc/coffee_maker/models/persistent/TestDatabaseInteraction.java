package edu.ncsu.csc.coffee_maker.models.persistent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

/**
 * Tests interacting with the database.
 *
 * @author Alex Snezhko
 * @author Dominic Brown
 * @author Selena Chen
 */
public class TestDatabaseInteraction {

    /**
     * Tests creating recipes.
     *
     * @throws InterruptedException
     *             If any errors persist
     */
    @Test
    public void testRecipes () throws InterruptedException {
        DomainObject.deleteAll( Recipe.class );

        final Recipe r = new Recipe();

        r.setName( "Coffee Drink" );
        r.setPrice( 50 );

        r.addIngredient( new Ingredient( new IngredientType( "Coffee" ), 200 ) );
        r.addIngredient( new Ingredient( new IngredientType( "Milk" ), 1 ) );
        r.addIngredient( new Ingredient( new IngredientType( "Chocolate" ), 1 ) );

        r.save();

        final List<Recipe> dbRecipes = Recipe.getAll();

        assertEquals( 1, dbRecipes.size() );

        final Recipe dbRecipe = dbRecipes.get( 0 );

        assertEquals( r.getName(), dbRecipe.getName() );
        assertEquals( r.getPrice(), dbRecipe.getPrice() );
        for ( int i = 0; i < r.getIngredients().size(); i++ ) {
            if ( r.getIngredients().get( i ).getIngredient().getName().equals( "Coffee" ) ) {
                boolean exists = false;
                for ( final Ingredient ing : dbRecipe.getIngredients() ) {
                    if ( ing.getIngredient().getName()
                            .equals( r.getIngredients().get( i ).getIngredient().getName() ) ) {
                        exists = true;
                        break;
                    }
                }
                assertTrue( exists );
            }
            else if ( r.getIngredients().get( i ).getIngredient().getName().equals( "Milk" ) ) {
                boolean exists = false;
                for ( final Ingredient ing : dbRecipe.getIngredients() ) {
                    if ( ing.getIngredient().getName()
                            .equals( r.getIngredients().get( i ).getIngredient().getName() ) ) {
                        exists = true;
                        break;
                    }
                }
                assertTrue( exists );
            }
            else if ( r.getIngredients().get( i ).getIngredient().getName().equals( "Chocolate" ) ) {
                boolean exists = false;
                for ( final Ingredient ing : dbRecipe.getIngredients() ) {
                    if ( ing.getIngredient().getName()
                            .equals( r.getIngredients().get( i ).getIngredient().getName() ) ) {
                        exists = true;
                        break;
                    }
                }
                assertTrue( exists );
            }
        }

        dbRecipe.setPrice( 15 );
        dbRecipe.addIngredient( new Ingredient( new IngredientType( "Sugar" ), 12 ) );
        dbRecipe.save();

        assertEquals( 1, Recipe.getAll().size() );

        assertEquals( 15, (int) Recipe.getAll().get( 0 ).getPrice() );

        for ( int i = 0; i < Recipe.getAll().size(); i++ ) {
            if ( Recipe.getAll().get( 0 ).getIngredients().get( i ).getIngredient().getName().equals( "Sugar" ) ) {
                assertEquals( 12, Recipe.getAll().get( 0 ).getIngredients().get( i ).getAmount() );
            }
        }

        dbRecipe.delete();

        assertEquals( 0, Recipe.getAll().size() );

        DomainObject.deleteAll( Recipe.class );

        assertEquals( 0, Recipe.getAll().size() );
    }

    /**
     * Tests deleting recipes.
     *
     * @throws InterruptedException
     *             If any errors persist
     */
    @Test
    public void testDeleteRecipes () throws InterruptedException {
        DomainObject.deleteAll( Recipe.class );

        final Recipe r = new Recipe();

        r.setName( "Coffee Drink" );
        r.setPrice( 50 );

        r.addIngredient( new Ingredient( new IngredientType( "Coffee" ), 200 ) );
        r.addIngredient( new Ingredient( new IngredientType( "Milk" ), 1 ) );
        r.addIngredient( new Ingredient( new IngredientType( "Chocolate" ), 1 ) );

        r.save();

        assertEquals( 1, Recipe.getAll().size() );

        Recipe.getAll().get( 0 ).delete();

        assertEquals( 0, Recipe.getAll().size() );
    }
}
