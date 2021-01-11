package edu.ncsu.csc.coffee_maker.models.persistent;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

/**
 * Tests the Inventory class.
 *
 * @author Alex Snezhko
 * @author Dominic Brown
 * @author Selena Chen
 */
public class InventoryTest {

    /**
     * Tests consuming inventory when making coffee.
     */
    @Test
    public void testConsumeInventory () {
        assertEquals( 1, Inventory.parseIngredientAmount( "1" ) );

        try {
            Inventory.parseIngredientAmount( "a" );
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "Units of ingredient must be a positive integer", e.getMessage() );
        }

        final ArrayList<Ingredient> ings = new ArrayList<Ingredient>();
        ings.add( new Ingredient( new IngredientType( "Coffee" ), 500 ) );
        ings.add( new Ingredient( new IngredientType( "Milk" ), 500 ) );
        ings.add( new Ingredient( new IngredientType( "Sugar" ), 500 ) );
        ings.add( new Ingredient( new IngredientType( "Chocolate" ), 500 ) );

        final Inventory i = new Inventory( ings );
        i.save();

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        recipe.addIngredient( new Ingredient( new IngredientType( "Coffee" ), 1 ) );
        recipe.addIngredient( new Ingredient( new IngredientType( "Milk" ), 20 ) );
        recipe.addIngredient( new Ingredient( new IngredientType( "Sugar" ), 5 ) );
        recipe.addIngredient( new Ingredient( new IngredientType( "Chocolate" ), 10 ) );

        recipe.setPrice( 5 );

        i.useIngredients( recipe );

        /*
         * Make sure that all of the inventory fields are now properly updated.
         */
        for ( int j = 0; j < i.getIngredients().size(); j++ ) {
            if ( i.getIngredients().get( j ).getIngredient().getName().equals( "Coffee" ) ) {
                assertEquals( 499, i.getIngredients().get( j ).getAmount() );
            }
            else if ( i.getIngredients().get( j ).getIngredient().getName().equals( "Milk" ) ) {
                assertEquals( 480, i.getIngredients().get( j ).getAmount() );
            }
            else if ( i.getIngredients().get( j ).getIngredient().getName().equals( "Sugar" ) ) {
                assertEquals( 495, i.getIngredients().get( j ).getAmount() );
            }
            else if ( i.getIngredients().get( j ).getIngredient().getName().equals( "Chocolate" ) ) {
                assertEquals( 490, i.getIngredients().get( j ).getAmount() );
            }
        }
    }
}
