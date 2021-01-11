package edu.ncsu.csc.itrust2.models.persistent;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.criterion.Criterion;
import org.hibernate.validator.constraints.Length;

import edu.ncsu.csc.itrust2.forms.pharmacist.PharmacistForm;
import edu.ncsu.csc.itrust2.models.enums.State;

@Entity
@Table ( name = "Pharmacists" )
public class Pharmacist extends DomainObject<Pharmacist> {

    /**
     * This stores a reference to the User object that this pharmacist is.
     * Mandatory.
     */
    @JoinColumn ( name = "self_id", columnDefinition = "varchar(100)" )
    @OneToOne
    private User    self;

    /**
     * Whether or not the pharmacist is enabled
     */
    private boolean enabled;

    /**
     * The first name of the pharmacist
     */
    @Length ( max = 20 )
    private String  firstName;

    /**
     * The last name of the pharmacist
     */
    @Length ( max = 30 )
    private String  lastName;

    /**
     * The address line 1 of the pharmacist
     */
    @Length ( max = 50 )
    private String  address1;

    /**
     * The address line 2 of the pharmacist
     */
    @Length ( max = 50 )
    private String  address2;

    /**
     * The city of residence of the pharmacist
     */
    @Length ( max = 15 )
    private String  city;

    /**
     * The state of residence of the pharmacist
     */
    @Enumerated ( EnumType.STRING )
    private State   state;

    /**
     * The zipcode of the pharmacist
     */
    @Length ( min = 5, max = 10 )
    private String  zip;

    /**
     * The phone number of the pharmacist
     */
    @Length ( min = 12, max = 12 )
    private String  phone;

    /**
     * The email of the pharmacist
     */
    @Length ( max = 30 )
    private String  email;

    /**
     * The id of the pharmacist
     */
    @Id
    @GeneratedValue ( strategy = GenerationType.AUTO )
    private Long    id;

    /**
     * The pharmacy this pharmacist is assigned to
     */
    // @ManyToOne
    // private Pharmacy pharmacy;
    private String  pharmacyId;

    /**
     * Create a new pharmacist based off of the PharmacistForm
     *
     * @param form
     *            the filled-in pharmacist form with pharmacist information
     */
    public Pharmacist ( final PharmacistForm form ) {
        setSelf( User.getByName( form.getSelf() ) );
        setEnabled( form.getEnabled() );

        setFirstName( form.getFirstName() );
        setLastName( form.getLastName() );
        setAddress1( form.getAddress1() );
        setAddress2( form.getAddress2() );
        setCity( form.getCity() );
        setState( State.valueOf( form.getState() ) );
        setZip( form.getZip() );
        setPhone( form.getPhone() );
        setEmail( form.getEmail() );
        // setPharmacy( Pharmacy.getByName( form.getPharmacy() ) );
        try {
            setId( Long.valueOf( form.getId() ) );
        }
        catch ( NullPointerException | NumberFormatException npe ) {
            /* Will not have ID set if fresh form */
        }
    }

    /**
     * Empty constructor necessary for Hibernate.
     */
    public Pharmacist () {

    }

    /**
     * Get the id of this pharmacist
     *
     * @return the id of this pharmacist
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Set the id of this pharmacist
     *
     * @param id
     *            the id to set this pharmacist to
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Get the user representation of this pharmacist
     *
     * @return the user representation of this pharmacist
     */
    public User getSelf () {
        return self;
    }

    /**
     * Set the user representation of this pharmacist
     *
     * @param self
     *            the user representation to set this pharmacist to
     */
    public void setSelf ( final User self ) {
        this.self = self;
    }

    /**
     * Get whether or not this pharmacist is enabled
     *
     * @return whether or not this pharmacist is enabled
     */
    public boolean getEnabled () {
        return enabled;
    }

    /**
     * Set whether or not this pharmacist is enabled
     *
     * @param enabled
     *            whether or not this pharmacist is enabled
     */
    public void setEnabled ( final boolean enabled ) {
        this.enabled = enabled;
    }

    /**
     * ======= >>>>>>> master Get the first name of this pharmacist
     *
     * @return the first name of this pharmacist
     */
    public String getFirstName () {
        return firstName;
    }

    /**
     * Set the first name of this pharmacist
     *
     * @param firstName
     *            the first name to set this pharmacist to
     */
    public void setFirstName ( final String firstName ) {
        this.firstName = firstName;
    }

    /**
     * Get the last name of this pharmacist
     *
     * @return the last name of this pharmacist
     */
    public String getLastName () {
        return lastName;
    }

    /**
     * Set the last name of this pharmacist
     *
     * @param lastName
     *            the last name to set this pharmacist to
     */
    public void setLastName ( final String lastName ) {
        this.lastName = lastName;
    }

    /**
     * Get the address line 1 of this pharmacist
     *
     * @return the address line 1 of this pharmacist
     */
    public String getAddress1 () {
        return address1;
    }

    /**
     * Set the address line 1 of this pharmacist
     *
     * @param address1
     *            the address line 1 to set this pharmacist to
     */
    public void setAddress1 ( final String address1 ) {
        this.address1 = address1;
    }

    /**
     * Get the address line 2 of this pharmacist
     *
     * @return the address line 2 of this pharmacist
     */
    public String getAddress2 () {
        return address2;
    }

    /**
     * Set the address line 2 of this pharmacist
     *
     * @param address2
     *            the address line 2 to set this pharmacist to
     */
    public void setAddress2 ( final String address2 ) {
        this.address2 = address2;
    }

    /**
     * Get the city of residence of this pharmacist
     *
     * @return the city of residence of this pharmacist
     */
    public String getCity () {
        return city;
    }

    /**
     * Set the city of residence of this pharmacist
     *
     * @param city
     *            the city of residence to set this pharmacist to
     */
    public void setCity ( final String city ) {
        this.city = city;
    }

    /**
     * Get the state of residence of this pharmacist
     *
     * @return the state of residence of this pharmacist
     */
    public State getState () {
        return state;
    }

    /**
     * Set the state of residence of this pharmacist
     *
     * @param state
     *            the state of residence to set this pharmacist to
     */
    public void setState ( final State state ) {
        this.state = state;
    }

    /**
     * Get the zipcode of this pharmacist
     *
     * @return the zipcode of this pharmacist
     */
    public String getZip () {
        return zip;
    }

    /**
     * Set the zipcode of this pharmacist
     *
     * @param zip
     *            the zipcode to set this pharmacist to
     */
    public void setZip ( final String zip ) {
        this.zip = zip;
    }

    /**
     * Get the phone number of this pharmacist
     *
     * @return the phone number of this pharmacist
     */
    public String getPhone () {
        return phone;
    }

    /**
     * Set the phone number of this pharmacist
     *
     * @param phone
     *            the phone number to set this pharmacist to
     */
    public void setPhone ( final String phone ) {
        this.phone = phone;
    }

    /**
     * Get the email of this pharmacist
     *
     * @return the email of this pharmacist
     */
    public String getEmail () {
        return email;
    }

    /**
     * Set the email of this pharmacist
     *
     * @param email
     *            the email to set this pharmacist to
     */
    public void setEmail ( final String email ) {
        this.email = email;
    }

    /**
     * Returns the pharmacy this pharmacist is at
     *
     * @return the pharmacy this pharmacist is at
     */
    public String getPharmacyId () {
        return pharmacyId;
    }

    /**
     * Sets the pharmacist's pharmacy
     *
     * @param pharmacyId
     *            the pharmacy to set
     */
    public void setPharmacyId ( String pharmacyId ) {
        this.pharmacyId = pharmacyId;
    }

    /**
     * To string method
     *
     * @return string rep. of Pharmacist.
     */
    @Override
    public String toString () {
        final String s = this.firstName + " " + this.lastName + " " + this.email;
        return s;
    }

    /**
     * Get the pharmacist by username
     *
     * @param username
     *            the username of the pharmacist to get
     * @return the pharmacist result with the queried username
     */
    public static Pharmacist getByName ( final String username ) {
        return getByName( User.getByName( username ) );
    }

    /**
     * Get the pharmacist by username, done by passing the User representation
     * of the pharmacist
     *
     * @param self
     *            the self the user representation of the pharmacist with their
     *            username
     * @return the pharmacist result with the queried username
     */
    public static Pharmacist getByName ( final User self ) {
        try {
            return getWhere( eqList( "self", self ) ).get( 0 );
        }
        catch ( final Exception e ) {
            return null;
        }
    }

    /**
     * Get all pharmacists in the DB
     *
     * @SuppressWarnings for Unchecked cast from List<capture#1-of ? extends
     *                   DomainObject> to List<Pharmacist> Because get all just
     *                   returns a list of DomainObjects, the cast is okay.
     *
     * @return all pharmacists in the DB
     */
    @SuppressWarnings ( "unchecked" )
    public static List<Pharmacist> getPharmacists () {
        return (List<Pharmacist>) getAll( Pharmacist.class );
    }

    /**
     * Get all Pharmacists in the database where the passed query is true
     *
     * @SuppressWarnings for Unchecked cast from List<capture#1-of ? extends
     *                   DomainObject> to List<Pharmacist> Because get all just
     *                   returns a list of DomainObjects, the cast is okay.
     *
     * @param where
     *            List of Criterion to and together and search for records by
     * @return all Pharmacists in the database where the passed query is true
     */
    @SuppressWarnings ( "unchecked" )
    public static List<Pharmacist> getWhere ( final List<Criterion> where ) {
        return (List<Pharmacist>) getWhere( Pharmacist.class, where );
    }
}
