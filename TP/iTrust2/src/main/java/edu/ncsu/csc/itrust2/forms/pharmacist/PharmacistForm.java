package edu.ncsu.csc.itrust2.forms.pharmacist;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import edu.ncsu.csc.itrust2.models.persistent.Pharmacist;
import edu.ncsu.csc.itrust2.models.persistent.User;

public class PharmacistForm {

    /**
     * Constructs a PharmacistForm for the User provided
     *
     * @param u
     *            User to make a Form for
     */
    public PharmacistForm ( final User u ) {
        this();
        if ( null != u ) {
            setSelf( u.getUsername() );
        }
    }

    /**
     * Username of the iTrust2 pharmacist to make a Pharmacist object for
     */
    private String  self;

    /**
     * Whether the Pharmacist is enabled or not
     */
    private boolean enabled;

    /**
     * First name of the Pharmacist
     */
    @NotEmpty
    @Length ( max = 20 )
    private String  firstName;

    /**
     * Last name of the Pharmacist
     */
    @NotEmpty
    @Length ( max = 30 )
    private String  lastName;

    /**
     * Address1 of the Pharmacist
     */
    @NotEmpty
    @Length ( max = 50 )
    private String  address1;

    /**
     * Address2 of the Pharmacist
     */
    @Length ( max = 50 )
    private String  address2;

    /**
     * City of the Pharmacist
     */
    @NotEmpty
    @Length ( max = 15 )
    private String  city;

    /**
     * State of the Pharmacist
     */
    @NotEmpty
    @Length ( min = 2, max = 2 )
    private String  state;

    /**
     * Zip of the Pharmacist
     */
    @NotEmpty
    @Length ( min = 5, max = 10 )
    private String  zip;

    /**
     * Phone of the Pharmacist
     */
    @NotEmpty
    @Pattern ( regexp = "(^[0-9]{3}-[0-9]{3}-[0-9]{4}$)", message = "Phone number must be formatted as xxx-xxx-xxxx" )
    private String  phone;

    /**
     * Email of the Pharmacist
     */
    @NotEmpty
    @Length ( max = 30 )
    private String  email;

    // /**
    // * Pharmacy this pharmacist works at
    // */
    // @NotEmpty
    // @Length ( max = 255 )
    // private String pharmacy;

    /**
     * ID of the Pharmacist
     */
    private String  id;

    /**
     * Creates a PharmacistForm object. For initializing a blank form
     */
    public PharmacistForm () {

    }

    /**
     * Creates a PharmacistForm for editing from a persistent Pharmacist
     *
     * @param p
     *            Pharmacist to create a form from
     */
    public PharmacistForm ( final Pharmacist p ) {
        if ( p == null ) {
            return;
        }
        if ( null != p.getSelf() ) {
            setSelf( p.getSelf().getUsername() );
        }
        if ( p.getEnabled() ) {
            setEnabled( p.getEnabled() );
        }
        setFirstName( p.getFirstName() );
        setLastName( p.getLastName() );
        setAddress1( p.getAddress1() );
        setAddress2( p.getAddress2() );
        setCity( p.getCity() );
        if ( null != p.getState() ) {
            setState( p.getState().toString() );
        }
        setZip( p.getZip() );
        setPhone( p.getPhone() );
        setEmail( p.getEmail() );
        // setPharmacy( p.getPharmacy().getName() );
        setId( p.getId().toString() );
    }

    /**
     * Get Username of the pharmacist
     *
     * @return The Pharmacist's username
     */
    public String getSelf () {
        return self;
    }

    /**
     * Set username of the Personenl
     *
     * @param self
     *            The pharmacist's username
     */
    public void setSelf ( final String self ) {
        this.self = self;
    }

    /**
     * Get whether the Pharmacist is enabled
     *
     * @return If the pharmacist is enabled or not
     */
    public boolean getEnabled () {
        return enabled;
    }

    /**
     * Set whether the pharmacist is enabled.
     *
     * @param enabled
     *            New enabled value
     */
    public void setEnabled ( final boolean enabled ) {
        this.enabled = enabled;
    }

    /**
     * Get the first name of the pharmacist
     *
     * @return Pharmacist's first name
     */
    public String getFirstName () {
        return firstName;
    }

    /**
     * Set the First Name of the Pharmacist
     *
     * @param firstName
     *            New FirstName to set
     */
    public void setFirstName ( final String firstName ) {
        this.firstName = firstName;
    }

    /**
     * Get the last name of the Pharmacist
     *
     * @return The last name of the Pharmacist
     */
    public String getLastName () {
        return lastName;
    }

    /**
     * Set the last name of the Pharmacist
     *
     * @param lastName
     *            New last name to set
     */
    public void setLastName ( final String lastName ) {
        this.lastName = lastName;
    }

    /**
     * Get Address1 of the Pharmacist
     *
     * @return Address1 of Pharmacist
     */
    public String getAddress1 () {
        return address1;
    }

    /**
     * Set Address1 of Pharmacist
     *
     * @param address1
     *            New Address1 to set
     */
    public void setAddress1 ( final String address1 ) {
        this.address1 = address1;
    }

    /**
     * Get Address2 of Pharmacist
     *
     * @return Address2 of pharmacist
     */
    public String getAddress2 () {
        return address2;
    }

    /**
     * Set Address2 of Pharmacist
     *
     * @param address2
     *            New Address2 to set
     */
    public void setAddress2 ( final String address2 ) {
        this.address2 = address2;
    }

    /**
     * Get the City of the Pharmacist
     *
     * @return Pharmacist's city of their address
     */
    public String getCity () {
        return city;
    }

    /**
     * Set the city of the Pharmacist
     *
     * @param city
     *            New city to set
     */
    public void setCity ( final String city ) {
        this.city = city;
    }

    /**
     * Get the Pharmacist's state
     *
     * @return State of the Pharmacist
     */
    public String getState () {
        return state;
    }

    /**
     * Set the State of the Pharmacist
     *
     * @param state
     *            New State to set
     */
    public void setState ( final String state ) {
        this.state = state;
    }

    /**
     * Get the ZIP of the Pharmacist
     *
     * @return The Pharmacist's ZIP
     */
    public String getZip () {
        return zip;
    }

    /**
     * Set the ZIP of the Pharmacist
     *
     * @param zip
     *            New ZIP to set
     */
    public void setZip ( final String zip ) {
        this.zip = zip;
    }

    /**
     * Get the Phone number of the pharmacist
     *
     * @return The Pharmacist's phone number
     */
    public String getPhone () {
        return phone;
    }

    /**
     * Set the Phone Number of the Pharmacist
     *
     * @param phone
     *            New phone number to set
     */
    public void setPhone ( final String phone ) {
        this.phone = phone;
    }

    /**
     * Get the Email of the Pharmacist
     *
     * @return Pharmacist's email
     */
    public String getEmail () {
        return email;
    }

    /**
     * Set the Email of the Pharmacist
     *
     * @param email
     *            The Pharmacist's new email
     */
    public void setEmail ( final String email ) {
        this.email = email;
    }

    // /**
    // * Returns the pharmacy of the Pharmacist
    // *
    // * @return the pharmacy of the Pharmacist
    // */
    // public String getPharmacy () {
    // return pharmacy;
    // }
    //
    // /**
    // * Sets the pharmacy of the Pharmacist
    // *
    // * @param pharmacy
    // * pharmacy to set
    // */
    // public void setPharmacy ( String pharmacy ) {
    // this.pharmacy = pharmacy;
    // }

    /**
     * Get the database ID of the Pharmacist object
     *
     * @return The Pharmacist's DB ID
     */
    public String getId () {
        return id;
    }

    /**
     * Set the database ID of the Pharmacist object
     *
     * @param id
     *            The new DB ID of the Pharmacist
     */
    public void setId ( final String id ) {
        this.id = id;
    }
}
