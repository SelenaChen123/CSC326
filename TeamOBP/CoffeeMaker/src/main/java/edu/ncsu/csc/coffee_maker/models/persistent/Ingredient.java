package edu.ncsu.csc.coffee_maker.models.persistent;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.validation.annotation.Validated;

/**
 * Ingredients for the coffee maker. Ingredient is tied to the database using
 * Hibernate libraries.
 *
 * @author Alex Snezhko
 * @author Dominic Brown
 * @author Selena Chen
 */
@Entity
@Table ( name = "ingredients" )
@Validated
public class Ingredient extends DomainObject<Ingredient> {

    /** ID for ingredient entry. */
    @Id
    @GeneratedValue ( generator = "increment" )
    @GenericGenerator ( name = "increment", strategy = "increment" )
    private Long           id;

    /** Name of the ingredient. */
    @ManyToOne ( cascade = CascadeType.ALL, optional = false )
    private IngredientType ingredient;

    /** Amount of the ingredient. */
    private int            amount;

    /**
     * Empty constructor for Hibernate.
     */
    public Ingredient () {
        // Empty constructor
    }

    /**
     * Constructs an Ingredient object.
     *
     * @param type
     *            Type of ingredient
     * @param amount
     *            Amount of the ingredient
     */
    public Ingredient ( final IngredientType type, final int amount ) {
        super();
        setIngredient( type );
        setAmount( amount );
    }

    /**
     * Returns the ingredient name.
     *
     * @return The name
     */
    public IngredientType getIngredient () {
        return ingredient;
    }

    /**
     * Sets the ingredient name.
     *
     * @param name
     *            The name to set
     */
    public void setIngredient ( final IngredientType name ) {
        this.ingredient = name;
    }

    /**
     * Returns the ingredient amount.
     *
     * @return The amount
     */
    public int getAmount () {
        return amount;
    }

    /**
     * Sets the ingredient amount.
     *
     * @param amount
     *            The amount to set
     */
    public void setAmount ( final int amount ) {
        this.amount = amount;
    }

    /**
     * Sets the ingredient id.
     *
     * @param id
     *            The id to set
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Serializable getId () {
        return id;
    }

    /**
     * Performs a getAll on the Ingredient class. The resulting list can then be
     * streamed and filtered on any parameters desired.
     *
     * @return A List of all records for the Ingredient class.
     */
    @SuppressWarnings ( "unchecked" )
    public static List<Ingredient> getAll () {
        return (List<Ingredient>) getAll( Ingredient.class );
    }

    /**
     * Gets an Ingredient by type. Will return null if the field is not valid
     * for the Ingredient or no record exists with the value provided.
     *
     * @param type
     *            Type of the ingredient
     * @return Ingredient associated with the type
     */
    public static Ingredient getByType ( final String type ) {
        final List<Ingredient> all = getAll();
        for ( final Ingredient i : all ) {
            if ( i.getIngredient().getName().equals( type ) ) {
                return i;
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode () {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( ingredient == null ) ? 0 : ingredient.hashCode() );
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals ( final Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final Ingredient other = (Ingredient) obj;
        if ( ingredient == null ) {
            if ( other.ingredient != null ) {
                return false;
            }
        }
        else if ( !ingredient.equals( other.ingredient ) ) {
            return false;
        }
        return true;
    }
}
