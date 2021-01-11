package edu.ncsu.csc.coffee_maker.models.persistent;

import static org.junit.Assert.assertEquals;

import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.coffee_maker.util.HibernateUtil;

/**
 * Tests the Ingredient object.
 *
 * @author sesmith5
 * @author Alex Snezhko
 * @author Dominic Brown
 * @author Selena Chen
 */
public class IngredientTest {

    /** Database session for testing. */
    private Session session;

    /**
     * Creates the session and transaction before testing.
     */
    @Before
    public void before () {
        session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Recipe.deleteAll( Ingredient.class );
    }

    /**
     * Tests constructor.
     */
    @Test
    public void testIngredient () {
        final Ingredient i1 = new Ingredient( new IngredientType( "Coffee" ), 1 );
        assertEquals( "Coffee", i1.getIngredient().getName() );
        assertEquals( 1, i1.getAmount() );
    }

    /**
     * Tests setters.
     */
    @Test
    public void testSetIngredient () {
        final Ingredient i1 = new Ingredient( new IngredientType( "Coffee" ), 1 );
        assertEquals( "Coffee", i1.getIngredient().getName() );
        assertEquals( 1, i1.getAmount() );

        i1.setIngredient( new IngredientType( "Milk" ) );
        assertEquals( "Milk", i1.getIngredient().getName() );
        assertEquals( 1, i1.getAmount() );

        i1.setAmount( 2 );
        assertEquals( "Milk", i1.getIngredient().getName() );
        assertEquals( 2, i1.getAmount() );
    }

    /**
     * Tests getAll().
     */
    @Test
    public void testGetAll () {
        assertEquals( 0, Ingredient.getAll().size() );
        final Ingredient i1 = new Ingredient( new IngredientType( "Coffee" ), 1 );
        final Ingredient i2 = new Ingredient( new IngredientType( "Milk" ), 2 );
        final Ingredient i3 = new Ingredient( new IngredientType( "Sugar" ), 3 );
        final Ingredient i4 = new Ingredient( new IngredientType( "Chocolate" ), 4 );
        i1.save();
        i2.save();
        i3.save();
        i4.save();
        assertEquals( 4, Ingredient.getAll().size() );
    }
}
