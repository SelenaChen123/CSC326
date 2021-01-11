package edu.ncsu.csc.itrust2.forms.admin;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import edu.ncsu.csc.itrust2.models.persistent.Pharmacy;

/**
 * A form that will be used to create a new pharmacy.
 *
 * @author Gavin Shirey
 *
 */
public class PharmacyForm {

    /**
     * Name of the Pharmacy
     */
    @NotEmpty
    @Length ( max = 255 )
    private String name;

    /**
     * Address of the Pharmacy
     */
    @NotEmpty
    @Length ( max = 255 )
    private String address;

    /**
     * ZIP Code of the Pharmacy
     */
    @NotEmpty
    @Length ( min = 5, max = 10 )
    private String zip;

    /**
     * State of the Pharmacy
     */
    @NotEmpty
    @Length ( max = 255 )
    private String state;

    /**
     * Empty constructor for the controllers to fill out.
     */
    public PharmacyForm () {

    }

    /**
     * Used to recreate a form that can be filled from a given pharmacy.
     *
     * @param p
     *            The pharmacy to use.
     */
    public PharmacyForm ( final Pharmacy p ) {
        setName( p.getName() );
        setAddress( p.getAddress() );
        setZip( p.getZip() );
        setState( p.getState().getName() );
    }

    /**
     * Gets the name of the pharmacy on the form.
     *
     * @return The name.
     */
    public String getName () {
        return name;
    }

    /**
     * Sets the name on the form.
     *
     * @param name
     *            Name of the form.
     */
    public void setName ( final String name ) {
        this.name = name;
    }

    /**
     * Gets the address of the pharmacy on the form.
     *
     * @return The address.
     */
    public String getAddress () {
        return address;
    }

    /**
     * Sets the address on the form.
     *
     * @param address
     *            Address of the form.
     */
    public void setAddress ( final String address ) {
        this.address = address;
    }

    /**
     * Gets the zip of the pharmacy on the form.
     *
     * @return The zip.
     */
    public String getZip () {
        return zip;
    }

    /**
     * Sets the zip code on the form.
     *
     * @param zip
     *            Zip code of the form.
     */
    public void setZip ( final String zip ) {
        this.zip = zip;
    }

    /**
     * Gets the state of the pharmacy on the form.
     *
     * @return The state.
     */
    public String getState () {
        return state;
    }

    /**
     * Sets the state on the form.
     *
     * @param state
     *            State name of the form.
     */
    public void setState ( final String state ) {
        this.state = state;
    }

}
