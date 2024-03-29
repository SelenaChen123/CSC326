package edu.ncsu.csc.itrust2.forms.hcp_patient;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import edu.ncsu.csc.itrust2.models.enums.PrescriptionType;
import edu.ncsu.csc.itrust2.models.persistent.Patient;
import edu.ncsu.csc.itrust2.models.persistent.Pharmacy;

/**
 * Form for user to fill out to add a Patient to the system.
 *
 * @author Kai Presler-Marshall
 *
 */
public class PatientForm {

    /**
     * Populate the patient form from a patient object
     *
     * @param patient
     *            the patient object to set the form with
     */
    public PatientForm ( final Patient patient ) {
        if ( null == patient ) {
            return; /* Nothing to do here */
        }
        if ( null != patient.getMother() ) {
            setMother( patient.getMother().getUsername() );
        }
        if ( null != patient.getFather() ) {
            setFather( patient.getFather().getUsername() );
        }
        setFirstName( patient.getFirstName() );
        setLastName( patient.getLastName() );
        setPreferredName( patient.getPreferredName() );
        setEmail( patient.getEmail() );
        setAddress1( patient.getAddress1() );
        setAddress2( patient.getAddress2() );
        setCity( patient.getCity() );
        if ( null != patient.getState() ) {
            setState( patient.getState().toString() );
        }
        setZip( patient.getZip() );
        setPhone( patient.getPhone() );

        if ( null != patient.getDateOfBirth() ) {
            setDateOfBirth( patient.getDateOfBirth().toString() );
        }
        if ( null != patient.getDateOfDeath() ) {
            setDateOfDeath( patient.getDateOfDeath().toString() );
        }

        setCauseOfDeath( patient.getCauseOfDeath() );

        if ( null != patient.getBloodType() ) {
            setBloodType( patient.getBloodType().toString() );
        }

        if ( null != patient.getEthnicity() ) {
            setEthnicity( patient.getEthnicity().toString() );
        }

        if ( null != patient.getGender() ) {
            setGender( patient.getGender().toString() );
        }

        setId( patient.getId() );

        setSelf( patient.getSelf().getUsername() );

        final Set<String> reps = new HashSet<String>();
        for ( final Patient pat : patient.getRepresentatives() ) {
            reps.add( pat.getSelf().getUsername() );
        }
        setRepresentatives( reps );

        final Set<String> repd = new HashSet<String>();
        for ( final Patient pat : patient.getRepresented() ) {
            repd.add( pat.getSelf().getUsername() );
        }
        setRepresenting( repd );

        if ( patient.getDefaultPharmacy() != null ) {
            setDefaultPharmacy( patient.getDefaultPharmacy() );
        }
        else {
            setDefaultPharmacy( null );
        }

        if ( patient.getDefaultPrescriptionType() == null ) {
            setDefaultPrescriptionType( null );
        }
        else if ( patient.getDefaultPrescriptionType() == PrescriptionType.GENERIC ) {
            setDefaultPrescriptionType( "Generic" );
        }
        else {
            setDefaultPrescriptionType( "Brand Name" );
        }

    }

    /** The username of the patient **/
    @Length ( max = 20 )
    private String      self;

    /** The mother of the patient **/
    @Length ( max = 20 )
    private String      mother;

    /** The father of the patient **/
    @Length ( max = 20 )
    private String      father;

    /** Representatives of the patient **/
    private Set<String> representatives;

    /** Represented by the patient **/
    private Set<String> representing;

    /** The first name of the patient **/
    @NotEmpty
    @Length ( max = 20 )
    private String      firstName;

    /** The preferred name of the patient **/
    @Length ( max = 20 )
    private String      preferredName;

    /** The last name of the patient **/
    @NotEmpty
    @Length ( max = 30 )
    private String      lastName;

    /** The email of the patient **/
    @NotEmpty
    @Length ( max = 30 )
    private String      email;

    /** The address line 1 of the patient **/
    @NotEmpty
    @Length ( max = 50 )
    private String      address1;

    /** The address line 2 of the patient **/
    @Length ( max = 50 )
    private String      address2;

    /** The city of residence of the patient **/
    @NotEmpty
    @Length ( max = 15 )
    private String      city;

    /** The state of residence of the patient **/
    @NotEmpty
    @Length ( min = 2, max = 2 )
    private String      state;

    /** The zipcode of the patient **/
    @NotEmpty
    @Length ( min = 5, max = 10 )
    private String      zip;

    /** The phone number of the patient **/
    @NotEmpty
    @Pattern ( regexp = "(^[0-9]{3}-[0-9]{3}-[0-9]{4}$)", message = "Phone number must be formatted as xxx-xxx-xxxx" )
    private String      phone;

    /** The date of birth of the patient **/
    @NotEmpty
    @Length ( min = 10, max = 10 )
    private String      dateOfBirth;

    /** The date of death of the patient **/
    private String      dateOfDeath;

    /** The cause of death of the patient **/
    @Length ( max = 50 )
    private String      causeOfDeath;

    /** The blood type of the patient **/
    @NotEmpty
    private String      bloodType;

    /** The ethnicity of the patient **/
    @NotEmpty
    private String      ethnicity;

    /** The gender of the patient **/
    @NotEmpty
    private String      gender;

    /** The id of the patient **/
    private Long        id;

    /**
     * Whether the patient is diabetic/pre-diabetic or not
     */
    @NotNull
    private boolean     isDiabetic;

    /**
     * Blood sugar limit for fasting
     */
    @Min ( 80 )
    @Max ( 130 )
    private int         fastingLimit;

    /**
     * Blood sugar limit for meals
     */
    @Min ( 120 )
    @Max ( 180 )
    private int         mealLimit;

    /** Default pharmacy for prescriptions. */
    private Pharmacy    defaultPharmacy;

    /** Default type to fill prescriptions. */
    private String      defaultPrescriptionType;

    /**
     * Returns whether the patient is diabetic/pre-diabetic or not
     *
     * @return true if the patient is diabetic/pre-diabetic
     */
    public boolean isDiabetic () {
        return isDiabetic;
    }

    /**
     * Sets the patients diabetic/pre-diabetic status
     *
     * @param isDiabetic
     *            true if the patient is diabetic/pre-diabetic
     */
    public void setDiabetic ( final boolean isDiabetic ) {
        this.isDiabetic = isDiabetic;
    }

    /**
     * Returns the fasting blood sugar limit imposed by the HCP
     *
     * @return the fasting blood sugar limit
     */
    public int getFastingLimit () {
        return fastingLimit;
    }

    /**
     * Sets the fasting blood sugar limit
     *
     * @param fastingLimit
     *            the fasting blood sugar limit
     */
    public void setFastingLimit ( final int fastingLimit ) {
        this.fastingLimit = fastingLimit;
    }

    /**
     * Returns the meal blood sugar limit imposed by the HCP
     *
     * @return the meal blood sugar limit
     */
    public int getMealLimit () {
        return mealLimit;
    }

    /**
     * Sets the meal blood sugar limit
     *
     * @param mealLimit
     *            the meal blood sugar limit
     */
    public void setMealLimit ( final int mealLimit ) {
        this.mealLimit = mealLimit;
    }

    /**
     * Constructor generates empty sets.
     */
    public PatientForm () {
        representatives = new HashSet<String>();
        representing = new HashSet<String>();
    }

    /**
     * Get the mother of the patient
     *
     * @return the mother of the patient
     */
    public String getMother () {
        return mother;
    }

    /**
     * Set the mother of the patient
     *
     * @param mother
     *            the mother of the patient
     */
    public void setMother ( final String mother ) {
        this.mother = mother;
    }

    /**
     * Get the father of the patient
     *
     * @return the father of the patient
     */
    public String getFather () {
        return father;
    }

    /**
     * Set the mother of the patient
     *
     * @param father
     *            the father of the patient
     */
    public void setFather ( final String father ) {
        this.father = father;
    }

    /**
     * Get the representatives for this patient
     *
     * @return the representatives for this patient
     */
    public Set<String> getRepresentatives () {
        return representatives;
    }

    /**
     * Get the patients represented by this patient
     *
     * @return the patients represented by this patient
     */
    public Set<String> getRepresented () {
        return representing;
    }

    /**
     * Sets the list of representatives for this patient.
     *
     * @param reps
     *            the list of representatives
     */
    private void setRepresentatives ( final Set<String> reps ) {
        this.representatives = reps;
    }

    /**
     * Sets the list of patients represented by this patient.
     *
     * @param representing
     *            the list of represented
     */
    private void setRepresenting ( final Set<String> representing ) {
        this.representing = representing;
    }

    /**
     * Get the first name of the patient
     *
     * @return the first name of the patient
     */
    public String getFirstName () {
        return firstName;
    }

    /**
     * Set the first name of the patient
     *
     * @param firstName
     *            the first name of the patient
     */
    public void setFirstName ( final String firstName ) {
        this.firstName = firstName;
    }

    /**
     * Get the preferred name of the patient
     *
     * @return the preferred name of the patient
     */
    public String getPreferredName () {
        return preferredName;
    }

    /**
     * Set the preferred name of the patient
     *
     * @param preferredName
     *            the preferred name of the patient
     */
    public void setPreferredName ( final String preferredName ) {
        this.preferredName = preferredName;
    }

    /**
     * Get the last name of the patient
     *
     * @return the last name of the patient
     */
    public String getLastName () {
        return lastName;
    }

    /**
     * Set the last name of the patient
     *
     * @param lastName
     *            the last name of the patient
     */
    public void setLastName ( final String lastName ) {
        this.lastName = lastName;
    }

    /**
     * Get the email of the patient
     *
     * @return the email of the patient
     */
    public String getEmail () {
        return email;
    }

    /**
     * Set the email of the patient
     *
     * @param email
     *            the email of the patient
     */
    public void setEmail ( final String email ) {
        this.email = email;
    }

    /**
     * Get the address line 1 of the patient
     *
     * @return the address line 1 of the patient
     */
    public String getAddress1 () {
        return address1;
    }

    /**
     * Set the address line 1 of the patient
     *
     * @param address1
     *            the address line 1 of the patient
     */
    public void setAddress1 ( final String address1 ) {
        this.address1 = address1;
    }

    /**
     * Get the address line 2 of the patient
     *
     * @return the address line 2 of the patient
     */
    public String getAddress2 () {
        return address2;
    }

    /**
     * Set the address line 2 of the patient
     *
     * @param address2
     *            the address line 2 of the patient
     */
    public void setAddress2 ( final String address2 ) {
        this.address2 = address2;
    }

    /**
     * Get the city of residence of the patient
     *
     * @return the city of residence of the patient
     */
    public String getCity () {
        return city;
    }

    /**
     * Set the city of residence of the patient
     *
     * @param city
     *            the city of residence of the patient
     */
    public void setCity ( final String city ) {
        this.city = city;
    }

    /**
     * Get the state of residence of the patient
     *
     * @return the state of residence of the patient
     */
    public String getState () {
        return state;
    }

    /**
     * Set the state of residence of the patient
     *
     * @param state
     *            the state of residence of the patient
     */
    public void setState ( final String state ) {
        this.state = state;
    }

    /**
     * Get the zipcode of the patient
     *
     * @return the zipcode of the patient
     */
    public String getZip () {
        return zip;
    }

    /**
     * Set the zipcode of the patient
     *
     * @param zip
     *            the zipcode of the patient
     */
    public void setZip ( final String zip ) {
        this.zip = zip;
    }

    /**
     * Get the phone number of the patient
     *
     * @return the phone number of the patient
     */
    public String getPhone () {
        return phone;
    }

    /**
     * Set the phone number of the patient
     *
     * @param phone
     *            the phone number of the patient
     */
    public void setPhone ( final String phone ) {
        this.phone = phone;
    }

    /**
     * Get the date of birth of the patient
     *
     * @return the date of birth of the patient
     */
    public String getDateOfBirth () {
        return dateOfBirth;
    }

    /**
     * Set the date of birth of the patient
     *
     * @param dateOfBirth
     *            the date of birth of the patient
     */
    public void setDateOfBirth ( final String dateOfBirth ) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Get the date of death of the patient
     *
     * @return the date of death of the patient
     */
    public String getDateOfDeath () {
        return dateOfDeath;
    }

    /**
     * Set the date of death of the patient
     *
     * @param dateOfDeath
     *            the date of death of the patient
     */
    public void setDateOfDeath ( final String dateOfDeath ) {
        this.dateOfDeath = dateOfDeath;
    }

    /**
     * Get the cause of death of the patient
     *
     * @return the cause of death of the patient
     */
    public String getCauseOfDeath () {
        return causeOfDeath;
    }

    /**
     * Set the cause of death of the patient
     *
     * @param causeOfDeath
     *            the cause of death of the patient
     */
    public void setCauseOfDeath ( final String causeOfDeath ) {
        this.causeOfDeath = causeOfDeath;
    }

    /**
     * Get the blood type of the patient
     *
     * @return the blood type of the patient
     */
    public String getBloodType () {
        return bloodType;
    }

    /**
     * Set the blood type of the patient
     *
     * @param bloodType
     *            the blood type of the patient
     */
    public void setBloodType ( final String bloodType ) {
        this.bloodType = bloodType;
    }

    /**
     * Get the ethnicity of the patient
     *
     * @return the ethnicity of the patient
     */
    public String getEthnicity () {
        return ethnicity;
    }

    /**
     * Set the ethnicity of the patient
     *
     * @param ethnicity
     *            the ethnicity of the patient
     */
    public void setEthnicity ( final String ethnicity ) {
        this.ethnicity = ethnicity;
    }

    /**
     * Get the gender of the patient
     *
     * @return the gender of the patient
     */
    public String getGender () {
        return gender;
    }

    /**
     * Set the gender of the patient
     *
     * @param gender
     *            the gender of the patient
     */
    public void setGender ( final String gender ) {
        this.gender = gender;
    }

    /**
     * Get the id of the patient
     *
     * @return the id of the patient
     */
    public Long getId () {
        return id;
    }

    /**
     * Set the id of the patient
     *
     * @param id
     *            the id of the patient
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Get the username of the patient
     *
     * @return the username of the patient
     */
    public String getSelf () {
        return self;
    }

    /**
     * Set the username of the patient
     *
     * @param self
     *            the username of the patient
     */
    public void setSelf ( final String self ) {
        this.self = self;
    }

    /**
     * Sets the default pharmacy of the user.
     *
     * @param pharmacy
     *            The new default Pharmacy.
     */
    public void setDefaultPharmacy ( final Pharmacy pharmacy ) {
        this.defaultPharmacy = pharmacy;
    }

    /**
     * Gets the default pharmacy.
     *
     * @return Returns the default pharmacy.
     */
    public Pharmacy getDefaultPharmacy () {
        return this.defaultPharmacy;
    }

    /**
     * Changes the prescription type.
     *
     * @param type
     *            The new prescription default.
     */
    public void setDefaultPrescriptionType ( final String type ) {
        this.defaultPrescriptionType = type;
    }

    /**
     * Gets the default prescription type.
     *
     * @return Returns the default prescription type.
     */
    public String getDefaultPrescriptionType () {
        return this.defaultPrescriptionType;
    }

}
