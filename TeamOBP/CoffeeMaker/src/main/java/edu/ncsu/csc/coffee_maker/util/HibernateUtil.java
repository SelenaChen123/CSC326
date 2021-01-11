package edu.ncsu.csc.coffee_maker.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * A utility class for setting up the Hibernate SessionFactory.
 *
 * @author Elizabeth Gilbert
 * @author Alex Snezhko
 * @author Dominic Brown
 * @author Selena Chen
 */
public class HibernateUtil {

    /** SessionFactory for CoffeeMaker. */
    private static SessionFactory sessionFactory = buildSessionFactory();

    /**
     * Creates a Hibernate SessionFactory.
     *
     * @return New session factory
     */
    private static SessionFactory buildSessionFactory () {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            return new Configuration().configure().buildSessionFactory();
        }
        catch ( final Throwable ex ) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println( "Initial SessionFactory creation failed." + ex );
            throw new ExceptionInInitializerError( ex );
        }
    }

    /**
     * Returns the Hibernate SessionFactory.
     *
     * @return Session factory
     */
    public static SessionFactory getSessionFactory () {
        return sessionFactory;
    }

    /**
     * Shuts down the connection to the database.
     */
    public static void shutdown () {
        // Close caches and connection pools
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }
    }
}
