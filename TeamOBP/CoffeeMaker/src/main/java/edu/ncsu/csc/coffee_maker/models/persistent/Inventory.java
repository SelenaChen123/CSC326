package edu.ncsu.csc.coffee_maker.models.persistent;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.validation.annotation.Validated;

/**
 * Inventory for the coffee maker. Inventory is tied to the database using
 * Hibernate libraries.
 *
 * @author Kai Presler-Marshall
 * @author Michelle Lemons
 * @author Elizabeth Gilbert
 * @author Sarah Heckman
 * @author Alex Snezhko
 * @author Dominic Brown
 * @author Selena Chen
 */
@Entity
@Table ( name = "inventory" )
@Validated
public class Inventory extends DomainObject<Inventory> {

    /** ID for inventory entry. */
    @Id
    @GeneratedValue ( strategy = GenerationType.IDENTITY )
    private Long             id;

    /** List of inventory ingredients. */
    @OneToMany ( fetch = FetchType.EAGER, cascade = CascadeType.ALL )
    private List<Ingredient> ingredients;

    /**
     * Empty constructor for Hibernate.
     */
    public Inventory () {
        // Intentionally empty so that Hibernate can instantiate
        // Inventory object.
    }

    /**
     * Constructs a new Inventory with given ingredients.
     *
     * @param ings
     *            Ingredients to put to Inventory
     */
    public Inventory ( final List<Ingredient> ings ) {
        setIngredients( ings );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Set the ID of the Inventory (Used by Hibernate).
     *
     * @param id
     *            The ID
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Get ingredients.
     *
     * @return Inventory ingredients
     */
    public List<Ingredient> getIngredients () {
        return ingredients;
    }

    /**
     * Set ingredients.
     *
     * @param ingredients
     *            Ingredients to set
     */
    public void setIngredients ( final List<Ingredient> ingredients ) {
        this.ingredients = ingredients;
    }

    /**
     * Add the number of ingredient units in the inventory to the current
     * amount.
     *
     * @param ingredient
     *            Amount of ingredient
     * @return Checked amount of ingredient
     * @throws IllegalArgumentException
     *             If the parameter isn't a positive integer
     */
    public static int parseIngredientAmount ( final String ingredient ) throws IllegalArgumentException {
        int amtIngredient = 0;
        try {
            amtIngredient = Integer.parseInt( ingredient );
        }
        catch ( final NumberFormatException e ) {
            throw new IllegalArgumentException( "Units of ingredient must be a positive integer" );
        }
        if ( amtIngredient < 0 ) {
            throw new IllegalArgumentException( "Units of ingredient must be a positive integer" );
        }
        return amtIngredient;
    }

    /**
     * Helper method to retrieve inventory ingredient.
     *
     * @param other
     *            Ingredient to be retrieved
     * @return Retrieved Ingredient
     */
    private Ingredient getIngredientInThis ( final Ingredient other ) {
        for ( final Ingredient i : this.ingredients ) {
            if ( i.getIngredient().equals( other.getIngredient() ) ) {
                return i;
            }
        }
        return null;
    }

    /**
     * Returns true if there are enough ingredients to make the beverage.
     *
     * @param r
     *            Recipe to check if there are enough ingredients
     * @return True if enough ingredients to make the beverage
     */
    public boolean enoughIngredients ( final Recipe r ) {
        for ( final Ingredient ing : r.getIngredients() ) {
            if ( getIngredientInThis( ing ).getAmount() < ing.getAmount() ) {
                return false;
            }
        }
        return true;
    }

    /**
     * Removes the ingredients used to make the specified recipe. Assumes that
     * the user has checked that there are enough ingredients to make.
     *
     * @param r
     *            Recipe to make
     * @return True if recipe is made.
     */
    public boolean useIngredients ( final Recipe r ) {
        if ( enoughIngredients( r ) ) {
            for ( final Ingredient ing : r.getIngredients() ) {
                final Ingredient ingInThis = getIngredientInThis( ing );
                setIngredient( ingInThis, ingInThis.getAmount() - ing.getAmount() );
            }
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Sets ingredient
     *
     * @param ing
     *            Ingredient to set
     * @param newAmount
     *            New ingredient amount
     */
    private void setIngredient ( final Ingredient ing, final int newAmount ) {
        ing.setAmount( newAmount );
    }

    /**
     * Adds ingredients to the inventory.
     *
     * @param ingredients
     *            List of ingredients to add
     * @return True if successful, false if not
     */
    public boolean addIngredients ( final List<Ingredient> ingredients ) {
        for ( final Ingredient ing : ingredients ) {
            if ( ing.getAmount() < 0 ) {
                throw new IllegalArgumentException( "Amount cannot be negative" );
            }
            final Ingredient ingToSet = getIngredientInThis( ing );
            if ( ingToSet == null ) {
                this.getIngredients().add( ing );
            }
            else {
                setIngredient( ingToSet, ingToSet.getAmount() + ing.getAmount() );
            }
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString () {
        final StringBuffer buf = new StringBuffer();
        for ( final Ingredient ing : ingredients ) {
            buf.append( ing.toString() + ": " + ing.getAmount() + "\n" );
        }
        return buf.toString();
    }

    /**
     * Gets the system's inventory.
     *
     * @return Inventory for the CoffeeMaker
     */
    @SuppressWarnings ( "unchecked" )
    public static Inventory getInventory () {
        final List<Inventory> inventoryList = (List<Inventory>) DomainObject.getAll( Inventory.class );
        if ( inventoryList != null && inventoryList.size() == 1 ) {
            return inventoryList.get( 0 );
        }
        else {
            // initialize the inventory with 0 of everything
            final Inventory i = new Inventory( new ArrayList<Ingredient>() );
            i.save();
            return i;
        }
    }
}
