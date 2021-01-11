package edu.ncsu.csc.coffee_maker.models.persistent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.coffee_maker.util.HibernateUtil;

/**
 * Tests the Recipe object.
 *
 * @author sesmith5
 * @author Alex Snezhko
 * @author Dominic Brown
 * @author Selena Chen
 */
public class RecipeTest {

    /** Database session for testing. */
    private Session session;

    /**
     * Creates the session and transaction before testing.
     */
    @Before
    public void before () {
        session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Recipe.deleteAll( Recipe.class );
    }

    /**
     * Tests creating and then retrieving a recipe.
     */
    @Test
    public void testAddRecipe () {
        final Recipe r1 = new Recipe();
        r1.setName( "Black Coffee" );
        r1.setPrice( 1 );
        r1.addIngredient( new Ingredient( new IngredientType( "Coffee" ), 1 ) );
        r1.save();

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 1 );
        r2.addIngredient( new Ingredient( new IngredientType( "Coffee" ), 1 ) );
        r2.addIngredient( new Ingredient( new IngredientType( "Milk" ), 1 ) );
        r2.addIngredient( new Ingredient( new IngredientType( "Sugar" ), 1 ) );
        r2.addIngredient( new Ingredient( new IngredientType( "Chocolate" ), 1 ) );
        r2.save();

        final List<Recipe> recipes = Recipe.getAll();
        assertEquals( 2, recipes.size() );
    }

    /**
     * Tests removing ingredients from recipes.
     */
    @Test
    public void testRemoveIngredient () {
        DomainObject.deleteAll( Recipe.class );

        assertEquals( 0, Recipe.getAll().size() );

        final Recipe r1 = new Recipe();
        r1.setName( "Green Coffee" );
        r1.setPrice( 1 );
        r1.addIngredient( new Ingredient( new IngredientType( "Coffee" ), 1 ) );
        r1.addIngredient( new Ingredient( new IngredientType( "Grass" ), 1 ) );
        r1.save();

        final List<Criterion> criteria = new LinkedList<>();
        criteria.add( Restrictions.eq( "name", r1.getName() ) );
        assertEquals( 2, Recipe.getWhere( criteria ).get( 0 ).getIngredients().size() );

        r1.removeIngredient( new Ingredient( new IngredientType( "Grass" ), 2 ) );
        r1.save();

        assertEquals( 1, Recipe.getByName( "Green Coffee" ).getIngredients().size() );
    }

    /**
     * Tests isValidRecipe().
     */
    @Test
    public void testIsValidRecipe () {
        final Recipe r1 = new Recipe();
        r1.setName( "Bad Drink" );
        r1.setPrice( -1 );

        assertFalse( Recipe.isValidRecipe( r1 ) );

        r1.setPrice( 1 );
        assertTrue( Recipe.isValidRecipe( r1 ) );

        r1.addIngredient( new Ingredient( new IngredientType( "Milk" ), 3 ) );
        assertTrue( Recipe.isValidRecipe( r1 ) );

        r1.addIngredient( new Ingredient( new IngredientType( "Antimatter Milk" ), -1 ) );
        assertFalse( Recipe.isValidRecipe( r1 ) );
    }
}
