package edu.ncsu.csc.coffee_maker.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.coffee_maker.models.persistent.Ingredient;
import edu.ncsu.csc.coffee_maker.models.persistent.Inventory;

/**
 * This is the controller in the CoffeeMaker application that handles REST API
 * endpoints related to ingredients.
 *
 * Spring will automatically convert all of the ResponseEntity and List results
 * to JSON.
 *
 * @author Alex Snezhko
 * @author Dominic Brown
 * @author Selena Chen
 */
@RestController
public class APIIngredientController extends APIController {

    /**
     * REST API method to get all ingredients by completing a GET request
     *
     * @return List of all ingredients
     */
    @GetMapping ( BASE_PATH + "ingredients" )
    public List<Ingredient> getAll () {
        return Inventory.getInventory().getIngredients();
    }

    /**
     * REST API method to create an ingredient by completing a POST request
     *
     * @param ing
     *            Ingredient to be created
     * @return Response to the request
     */
    @SuppressWarnings ( "rawtypes" )
    @PostMapping ( BASE_PATH + "ingredients" )
    public ResponseEntity createIngredient ( @RequestBody final Ingredient ing ) {

        if ( Ingredient.getByType( ing.getIngredient().getName() ) == null && ing.getAmount() > 0 ) {
            ing.save();
            final Inventory i = Inventory.getInventory();
            i.getIngredients().add( ing );
            i.save();
            return new ResponseEntity( HttpStatus.OK );
        }
        return new ResponseEntity( HttpStatus.CONFLICT );
    }

    /**
     * REST API method to update an ingredient by completing a PUT request
     *
     * @param ing
     *            Ingredient to be updated
     * @return Response to the request
     */
    @SuppressWarnings ( "rawtypes" )
    @PutMapping ( BASE_PATH + "ingredients" )
    public ResponseEntity updateIngredient ( @RequestBody final Ingredient ing ) {
        if ( ing.getAmount() < 0 ) {
            return new ResponseEntity( HttpStatus.BAD_REQUEST );
        }
        ing.save();
        return new ResponseEntity( HttpStatus.OK );
    }
}
