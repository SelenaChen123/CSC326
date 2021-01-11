package edu.ncsu.csc.coffee_maker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application main.
 *
 * @author Sarah Heckman
 * @author Kai Presler-Marshall
 * @author Elizabeth Gilbert
 * @author Alex Snezhko
 * @author Dominic Brown
 * @author Selena Chen
 */
@SpringBootApplication
public class Application {

    /**
     * Starts the program.
     *
     * @param args
     *            Command line args
     */
    public static void main ( final String[] args ) {
        SpringApplication.run( Application.class, args );
    }
}
