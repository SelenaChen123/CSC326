package edu.ncsu.csc.coffee_maker.models.persistent;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.validation.annotation.Validated;

/**
 * Ingredient types for the coffee maker. IngredientType is tied to the database
 * using Hibernate libraries.
 *
 * @author Alex Snezhko
 * @author Dominic Brown
 * @author Selena Chen
 */
@Entity
@Table ( name = "ingredient_types" )
@Validated
public class IngredientType extends DomainObject<IngredientType> {

    /** ID for ingredient type entry. */
    @Id
    @GeneratedValue ( generator = "increment" )
    @GenericGenerator ( name = "increment", strategy = "increment" )
    private Long         id;

    /** Name of the ingredient. */
    private final String name;

    /**
     * Empty constructor for Hibernate.
     */
    public IngredientType () {
        name = "";
    }

    /**
     * Constructs an IngredientType object.
     *
     * @param name
     *            Name of the ingredient
     */
    public IngredientType ( final String name ) {
        this.name = name;
    }

    /**
     * Returns the ingredient name.
     *
     * @return The name
     */
    public String getName () {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode () {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( id == null ) ? 0 : id.hashCode() );
        result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
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
        final IngredientType other = (IngredientType) obj;
        if ( name == null ) {
            if ( other.name != null ) {
                return false;
            }
        }
        else if ( !name.equals( other.name ) ) {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Serializable getId () {
        return id;
    }
}
