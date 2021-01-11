package edu.ncsu.csc.itrust2.models.persistent;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import edu.ncsu.csc.itrust2.models.enums.State;

/**
 * A superclass that was refactored out of Hospital. This can be used to create
 * new types of locations, like Pharmacies.
 *
 * @author Gavin Shirey
 *
 * @param <T>
 *            A reference type for the extending object to parameterize itself.
 */
@MappedSuperclass
public abstract class Location <T extends DomainObject<T>> extends DomainObject<T> {

    /**
     * Name of the Location
     */
    @NotEmpty
    @Length ( max = 100 )
    @Id
    protected String name;

    /**
     * Address of the Location
     */
    @NotEmpty
    @Length ( max = 100 )
    protected String address;

    /**
     * State of the Location
     */
    @Enumerated ( EnumType.STRING )
    private State    state;

    /**
     * ZIP code of the Location
     */
    @NotEmpty
    @Length ( min = 5, max = 10 )
    private String   zip;

    public Location () {
        super();
    }

    /**
     * Retrieves the name of this Location
     *
     * @return The Name of the Location
     */
    public String getName () {
        return name;
    }

    /**
     * Sets the name of this Location
     *
     * @param name
     *            New Name for the Location
     */
    public void setName ( final String name ) {
        this.name = name;
    }

    /**
     * Gets the Address of this Location
     *
     * @return Address of the Location
     */
    public String getAddress () {
        return address;
    }

    /**
     * Sets the Address of this Location
     *
     * @param address
     *            New Address of the Location
     */
    public void setAddress ( final String address ) {
        this.address = address;
    }

    /**
     * Gets the State of this Location
     *
     * @return The State of the Location
     */
    public State getState () {
        return state;
    }

    /**
     * Sets the State of this Location
     *
     * @param state
     *            New State of the Location
     */
    public void setState ( final State state ) {
        this.state = state;
    }

    /**
     * Gets the ZIP code of this Location
     *
     * @return The ZIP of the Location
     */
    public String getZip () {
        return zip;
    }

    /**
     * Sets the ZIP of this Location
     *
     * @param zip
     *            New ZIP code for the Location
     */
    public void setZip ( final String zip ) {
        this.zip = zip;
    }

}
