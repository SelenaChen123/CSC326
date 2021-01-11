package edu.ncsu.csc.coffee_maker.models.persistent;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.criterion.Criterion;

/**
 * Recipe for the coffee maker. Recipe is tied to the database using Hibernate
 * libraries.
 *
 * @author Sarah Heckman
 * @author Kai Presler-Marshall
 * @author Elizabeth Gilbert
 * @author Alex Snezhko
 * @author Dominic Brown
 * @author Selena Chen
 */
@Entity
@Table ( name = "recipes" )
public class Recipe extends DomainObject<Recipe> {

    /** Recipe ID. */
    @Id
    @GeneratedValue ( generator = "increment" )
    @GenericGenerator ( name = "increment", strategy = "increment" )
    private Long                   id;

    /** Recipe name. */
    @NotNull
    private String                 name;

    /** Recipe price. */
    @Min ( 0 )
    private Integer                price;

    /** Recipe ingredients. */
    @ManyToMany ( fetch = FetchType.EAGER, cascade = CascadeType.ALL )
    private final List<Ingredient> ingredients;

    /**
     * Creates a default recipe for the coffee maker.
     */
    public Recipe () {
        this.name = "";
        this.price = 0;
        ingredients = new ArrayList<Ingredient>();
    }

    /**
     * Adds an ingredient.
     *
     * @param ingredient
     *            Ingredient to be added
     */
    public void addIngredient ( final Ingredient ingredient ) {
        ingredients.add( ingredient );
    }

    /**
     * Removes an ingredient.
     *
     * @param ingredient
     *            Ingredient to be removed
     */
    public void removeIngredient ( final Ingredient ingredient ) {
        ingredients.remove( ingredient );
    }

    /**
     * Returns the list of ingredients.
     *
     * @return The recipe ingredients
     */
    public List<Ingredient> getIngredients () {
        return ingredients;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Set the ID of the Recipe (Used by Hibernate).
     *
     * @param id
     *            The ID to be set
     */
    @SuppressWarnings ( "unused" )
    private void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Returns name of the recipe.
     *
     * @return The name of the recipe.
     */
    public String getName () {
        return name;
    }

    /**
     * Sets the recipe name.
     *
     * @param name
     *            The name to set.
     */
    public void setName ( final String name ) {
        this.name = name;
    }

    /**
     * Returns the price of the recipe.
     *
     * @return Returns the price.
     */
    public Integer getPrice () {
        return price;
    }

    /**
     * Sets the recipe price.
     *
     * @param price
     *            The price to set.
     */
    public void setPrice ( final int price ) {
        this.price = price;
    }

    /**
     * Checks whether or not the recipe is a valid one.
     *
     * @param recipe
     *            Recipe to be checked
     * @return True if the recipe is valid, false otherwise
     */
    public static boolean isValidRecipe ( final Recipe recipe ) {
        if ( recipe == null || recipe.getIngredients() == null || recipe.getPrice() < 0 ) {
            return false;
        }
        for ( final Ingredient ing : recipe.getIngredients() ) {
            if ( ing.getAmount() < 0 ) {
                return false;
            }
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString () {
        return "Recipe [name=" + name + ", ingredients=" + ingredients + "]";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode () {
        final int prime = 31;
        int result = 1;
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
        final Recipe other = (Recipe) obj;
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
     * Get all the associated Recipes given criteria constraints.
     *
     * @param criteriaList
     *            List of what to search for
     * @return List of recipes that match the given criteria
     */
    @SuppressWarnings ( "unchecked" )
    public static List<Recipe> getWhere ( final List<Criterion> criteriaList ) {
        return (List<Recipe>) DomainObject.getWhere( Recipe.class, criteriaList );
    }

    /**
     * Get all recipes in the system.
     *
     * @return All recipes currently stored
     */
    @SuppressWarnings ( "unchecked" )
    public static List<Recipe> getAll () {
        return (List<Recipe>) DomainObject.getAll( Recipe.class );
    }

    /**
     * Gets a recipe from a given name.
     *
     * @param name
     *            Name of the recipe
     * @return Recipe with the given name
     */
    public static Recipe getByName ( final String name ) {
        return (Recipe) DomainObject.getBy( Recipe.class, "name", name );
    }
}
