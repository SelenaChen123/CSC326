package edu.ncsu.csc.coffee_maker.controllers;

import com.google.gson.Gson;

/**
 * Class for handy utils shared across all of the API tests.
 *
 * @author Kai Presler-Marshall
 * @author Alex Snezhko
 * @author Dominic Brown
 * @author Selena Chen
 */
public class TestUtils {

    /** Google GSON parser. */
    private static Gson gson = new Gson();

    /**
     * Uses Google's GSON parser to serialize a Java object to JSON. Useful for
     * creating JSON representations of our objects when calling API methods.
     *
     * @param obj
     *            Object to serialize to JSON
     * @return JSON string associated with object
     */
    public static String asJsonString ( final Object obj ) {
        return gson.toJson( obj );
    }
}
