package edu.ncsu.csc.itrust2.models.persistent;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.criterion.Criterion;

import edu.ncsu.csc.itrust2.forms.admin.PharmacyForm;
import edu.ncsu.csc.itrust2.models.enums.State;

/**
 * This class represents a pharmacy, which is a location.
 *
 * @author Gavin Shirey
 *
 */
@Entity
@Table ( name = "Pharmacies" )
public class Pharmacy extends Location<Pharmacy> implements Serializable {

    /**
     * Used for serializing the object.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Empty Constructor for Hibernate.
     */
    public Pharmacy () {

    }

    /**
     * This constructor creates a Pharmacy with the given values.
     *
     * @param name
     *            Name of the pharmacy.
     * @param address
     *            Address of the pharmacy.
     * @param zip
     *            Zip of the pharmacy.
     * @param state
     *            State of the pharmacy.
     */
    public Pharmacy ( final String name, final String address, final String zip, final State state ) {
        setName( name );
        setAddress( address );
        setZip( zip );
        setState( state );
    }

    /**
     * Creates a pharmacy from a form object.
     *
     * @param pf
     *            The form that contains the data to use.
     */
    public Pharmacy ( final PharmacyForm pf ) {
        setName( pf.getName() );
        setAddress( pf.getAddress() );
        setZip( pf.getZip() );
        setState( State.parse( pf.getState() ) );
    }

    /**
     * Gets the name of Pharmacy as an ID.
     *
     * @return The name (ID) of the Pharmacy.
     */
    @Override
    public String getId () {
        return getName();
    }

    /**
     * Gets the content of the Pharmacy as a string.
     *
     * @return A representation of the Pharmacy.
     */
    @Override
    public String toString () {
        final String s = this.name + "  " + this.address;
        return s;
    }

    /**
     * Gets every Pharmacy from the database that matches the criteria.
     *
     * @param where
     *            Criteria to search by.
     * @return The matching Pharmacies in the database.
     */
    @SuppressWarnings ( "unchecked" )
    private static List<Pharmacy> getWhere ( final List<Criterion> where ) {
        return (List<Pharmacy>) getWhere( Pharmacy.class, where );
    }

    /**
     * Retrieves a Pharmacy from the database by the given name.
     *
     * @param name
     *            Name of the Pharmacy to retrieve.
     * @return The Pharmacy if it was found, null otherwise.
     */
    public static Pharmacy getByName ( final String name ) {
        try {
            return getWhere( eqList( "name", name ) ).get( 0 );
        }
        catch ( final Exception e ) {
            return null;
        }
    }

    /**
     * Gets all of the Pharmacies from the database.
     *
     * @return Returns all of the Pharmacies.
     */
    @SuppressWarnings ( "unchecked" )
    public static List<Pharmacy> getPharmacies () {
        return (List<Pharmacy>) getAll( Pharmacy.class );
    }

}
