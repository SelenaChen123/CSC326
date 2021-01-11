package edu.ncsu.csc.itrust2.cucumber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.itrust2.models.enums.PrescriptionType;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.enums.State;
import edu.ncsu.csc.itrust2.models.persistent.Diagnosis;
import edu.ncsu.csc.itrust2.models.persistent.Drug;
import edu.ncsu.csc.itrust2.models.persistent.OfficeVisit;
import edu.ncsu.csc.itrust2.models.persistent.Patient;
import edu.ncsu.csc.itrust2.models.persistent.Pharmacist;
import edu.ncsu.csc.itrust2.models.persistent.Pharmacy;
import edu.ncsu.csc.itrust2.models.persistent.Prescription;
import edu.ncsu.csc.itrust2.models.persistent.User;
import edu.ncsu.csc.itrust2.utils.EmailUtil;

public class PrescriptionsStepDefs extends CucumberTest {

    private static final String BASE_URL   = "http://localhost:8080/iTrust2/";
    private static final String VISIT_URL  = BASE_URL + "hcp/documentOfficeVisit.html";
    private static final String VIEW_URL   = BASE_URL + "patient/officeVisit/viewPrescriptions.html";
    private static final String DRUG_URL   = BASE_URL + "admin/drugs.html";
    private static final String FILL_URL   = BASE_URL + "pharmacist/fillPrescriptions.html";
    private static final String RECORD_URL = BASE_URL + "pharmacist/recordPrescriptions.html";

    private final String        baseUrl    = "http://localhost:8080/iTrust2";

    private boolean             prereqRun  = false;

    // private String getUserName ( final String first, final String last ) {
    // return first.substring( 0, 1 ).toLowerCase() + last.toLowerCase();
    // }

    private void enterValue ( final String name, final String value ) {
        final WebElement field = driver.findElement( By.name( name ) );
        field.clear();
        field.sendKeys( String.valueOf( value ) );
    }

    /**
     * Fills in the date and time fields with the specified date and time.
     *
     * @param date
     *            The date to enter.
     * @param time
     *            The time to enter.
     */
    private void fillInDateTime ( final String dateField, final String date, final String timeField,
            final String time ) {
        fillInDate( dateField, date );
        fillInTime( timeField, time );
    }

    /**
     * Fills in the date field with the specified date.
     *
     * @param date
     *            The date to enter.
     */
    private void fillInDate ( final String dateField, final String date ) {
        driver.findElement( By.name( dateField ) ).clear();
        final WebElement dateElement = driver.findElement( By.name( dateField ) );
        dateElement.sendKeys( date.replace( "/", "" ) );
    }

    /**
     * Fills in the time field with the specified time.
     *
     * @param time
     *            The time to enter.
     */
    private void fillInTime ( final String timeField, String time ) {
        // Zero-pad the time for entry
        if ( time.length() == 7 ) {
            time = "0" + time;
        }

        driver.findElement( By.name( timeField ) ).clear();
        final WebElement timeElement = driver.findElement( By.name( timeField ) );
        timeElement.sendKeys( time.replace( ":", "" ).replace( " ", "" ) );
    }

    private void selectItem ( final String name, final String value ) {
        final By selector = By.cssSelector( "input[name='" + name + "'][value='" + value + "']" );
        waitForAngular();
        final WebElement element = driver.findElement( selector );
        element.click();
    }

    private void selectName ( final String name ) {
        final WebElement element = driver.findElement( By.cssSelector( "input[name='" + name + "']" ) );
        element.click();
    }

    private void selectType ( final String type ) {
        final WebElement element = driver.findElement( By.cssSelector( "input[value='" + type + "']" ) );
        element.click();
    }

    private void selectPharmacy ( final String pharmacyName ) {
        final WebElement pharmacy = driver.findElement( By.name( "pharmacy" ) );
        final Select dropdown = new Select( pharmacy );
        dropdown.selectByVisibleText( pharmacyName );
    }

    private void runPrereqs () {
        final Patient patient = new Patient();
        final User patientUser = new User( "patient", "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.",
                Role.ROLE_PATIENT, 1 );
        patientUser.save();
        patient.setSelf( patientUser );
        patient.setDefaultPrescriptionType( PrescriptionType.GENERIC );
        patient.setDefaultPharmacy( Pharmacy.getByName( "Big pharma" ) );
        patient.setFirstName( "Patient" );
        patient.setLastName( "Patientson" );
        patient.setEmail( "patientcsc326@gmail.com" );
        patient.save();

        final Patient jbean = new Patient();
        final User jbeanUser = new User( "jbeanz", "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.",
                Role.ROLE_PATIENT, 1 );
        jbeanUser.save();
        jbean.setSelf( jbeanUser );
        jbean.setDefaultPrescriptionType( PrescriptionType.BRAND_NAME );
        jbean.setFirstName( "Jim" );
        jbean.setLastName( "Bean" );
        jbean.setEmail( "jbeancsc326@gmail.com" );
        jbean.save();

        final User pharmacistUser = new User( "pharmacist",
                "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.", Role.ROLE_PHARMACIST, 1 );
        pharmacistUser.save();
        final Pharmacist pharmacist = new Pharmacist();
        pharmacist.setSelf( pharmacistUser );
        pharmacist.setFirstName( "Pharma" );
        pharmacist.setLastName( "The Fake Pharmacist" );
        pharmacist.setEmail( "csc326.201.1@gmail.com" );
        pharmacist.setAddress1( "1234 Road St." );
        pharmacist.setCity( "town" );
        pharmacist.setState( State.AK );
        pharmacist.setZip( "12345" );
        pharmacist.setPhone( "111-222-3333" );
        pharmacist.setPharmacyId( "Big pharma" );
        pharmacist.save();
        final User pharmacistUser2 = new User( "testpharmacist",
                "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.", Role.ROLE_PHARMACIST, 1 );
        pharmacistUser2.save();
        final Pharmacist pharmacist2 = new Pharmacist();
        pharmacist2.setSelf( pharmacistUser2 );
        pharmacist2.setFirstName( "Pharma Bro" );
        pharmacist2.setLastName( "The Real Pharmacist" );
        pharmacist2.setEmail( "csc326.201.2@gmail.com" );
        pharmacist2.setAddress1( "1235 Road St." );
        pharmacist2.setCity( "town2" );
        pharmacist2.setState( State.AL );
        pharmacist2.setZip( "12346" );
        pharmacist2.setPhone( "111-222-3334" );
        pharmacist2.setPharmacyId( "Big pharma" );
        pharmacist2.save();

        final Patient joseph = new Patient();
        joseph.setFirstName( "Joseph" );
        final User josephUser = new User( "JosephStalling",
                "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.", Role.ROLE_PATIENT, 1 );
        josephUser.save();
        joseph.setSelf( josephUser );
        joseph.setLastName( "Stalling" );
        joseph.setDateOfBirth( LocalDate.now().minusYears( 40 ) ); // 40 years
                                                                   // old
        joseph.save();

        final Pharmacy pharm = new Pharmacy( "Big pharma", "124 Main St", "12345", State.NC );
        pharm.save();
        final Pharmacy pharm1 = new Pharmacy( "VCS Pharmacy", "1 One St", "12345", State.AK );
        pharm1.save();
        final Pharmacy pharm2 = new Pharmacy( "CSV Pharmacy", "2 Two St", "54321", State.AL );
        pharm2.save();
    }

    @Given ( "^I have logged in with username: (.+)$" )
    public void login ( final String username ) {
        if ( !prereqRun ) {
            runPrereqs();
            prereqRun = true;
        }
        attemptLogout();

        driver.get( baseUrl );

        enterValue( "username", username );
        enterValue( "password", "123456" );
        driver.findElement( By.className( "btn" ) ).click();
    }

    /**
     * Document an office visit
     *
     * @param patient
     *            patient to document office visit for
     * @param firstName
     *            first name of patient
     * @param lastName
     *            last name of patient
     * @param dob
     *            date of birth of patient
     */
    @When ( "^I start documenting an office visit for user (.+) with name: (.+) (.+) and date of birth: (.+)$" )
    public void startOfficeVisit ( final String patient, final String firstName, final String lastName,
            final String dob ) {

        if ( Patient.getByName( patient ) == null ) {
            final Patient p = new Patient();
            p.setFirstName( firstName );
            final User u = new User( patient, "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.",
                    Role.ROLE_PATIENT, 1 );
            u.save();
            p.setSelf( u );
            p.setLastName( lastName );
            p.setDateOfBirth( LocalDate.now().minusYears( 40 ) ); // 40 years
                                                                  // old
            p.setEmail( EmailUtil.getSystemEmail() );
            p.save();
            try {
                Thread.sleep( 1500 );
            }
            catch ( final InterruptedException e ) {
                e.printStackTrace();
            }
        }
        driver.get( VISIT_URL );
        // final String patient = getUserName( firstName, lastName );

        try {
            Prescription.getForPatient( patient ).forEach( e -> e.delete() );
        }
        catch ( final Exception e ) {
            /* Ignored */
        }
        try {
            Diagnosis.getForPatient( User.getByName( patient ) ).forEach( e -> e.delete() );
        }
        catch ( final Exception e ) {
            /* Ignored */
        }
        try {
            OfficeVisit.getForPatient( patient ).forEach( e -> e.delete() );
        }
        catch ( final Exception e ) {
            /* Ignored */
        }

        waitForAngular();
        selectItem( "name", patient );
    }

    /**
     * Fill in an office visit
     *
     * @param date
     *            date of office visit
     * @param hospital
     *            hospital of office visit
     * @param notes
     *            notes for office visit
     * @param weight
     *            weight of patient
     * @param height
     *            height of patient
     * @param bloodPressure
     *            blood pressure of patient
     * @param hss
     *            patient's HSS
     * @param pss
     *            patient's PSS
     * @param hdl
     *            patient's HDL
     * @param ldl
     *            patient's LDL
     * @param triglycerides
     *            patient's triglyceride levels
     */
    @When ( "^fill in the office visit with date: (.+), hospital: (.+), notes: (.*), weight: (.+), height: (.+), blood pressure: (.+), household smoking status: (.+), patient smoking status: (.+), hdl: (.+), ldl: (.+), and triglycerides: (.+)$" )
    public void fillOfficeVisitForm ( final String date, final String hospital, final String notes, final String weight,
            final String height, final String bloodPressure, final String hss, final String pss, final String hdl,
            final String ldl, final String triglycerides ) {

        waitForAngular();

        fillInDateTime( "date", date, "time", "10:10 AM" );

        ( (JavascriptExecutor) driver ).executeScript( "document.getElementsByName('hospital')[0].click();" );
        waitForAngular();
        driver.findElement( By.name( "GENERAL_CHECKUP" ) ).click();
        waitForAngular();
        enterValue( "notes", notes );
        enterValue( "weight", weight );
        enterValue( "height", height );
        enterValue( "systolic", bloodPressure.split( "/" )[0] );
        enterValue( "diastolic", bloodPressure.split( "/" )[1] );
        selectItem( "houseSmokingStatus", hss );
        selectItem( "patientSmokingStatus", pss );
        enterValue( "hdl", hdl );
        enterValue( "ldl", ldl );
        enterValue( "tri", triglycerides );
    }

    /**
     * Adda a prescription to office visit
     *
     * @param drug
     *            prescription drug
     * @param type
     *            prescription type
     * @param pharmacy
     *            pharmacy to send prescription to
     * @param dosage
     *            dosage of prescription
     * @param startDate
     *            start date of prescription
     * @param endDate
     *            end date of prescription
     * @param renewals
     *            number of renewals on the prescription
     */
    @When ( "^add a prescription for (.+) of the (.+) type from pharmacy (.+) with a dosage of (.+) starting on (.+) and ending on (.+) with (.+) renewals and submit the office visit$" )
    public void addPrescription ( final String drug, final String type, final String pharmacy, final String dosage,
            final String startDate, final String endDate, final String renewals ) {
        waitForAngular();

        enterValue( "dosageEntry", dosage );
        fillInDate( "startEntry", startDate );
        fillInDate( "endEntry", endDate );
        enterValue( "renewalEntry", renewals );
        selectName( drug );
        selectType( type );
        selectPharmacy( pharmacy );

        driver.findElement( By.name( "fillPrescription" ) ).click();
        assertEquals( "", driver.findElement( By.name( "errorMsg" ) ).getText() );

        driver.findElement( By.name( "submit" ) ).click();
        waitForAngular();
        assertEquals( "", driver.findElement( By.name( "errorMsg" ) ).getText() );
    }

    // @When ( "^submit the office visit$" )
    // public void submitOfficeVisit () {
    // driver.findElement( By.name( "submit" ) ).click();
    // waitForAngular();
    // }

    /**
     * Ensure a success message is displayed
     */
    @Then ( "A message indicates the visit was submitted successfully" )
    public void officeVisitSuccessful () {
        waitForAngular();
        final WebElement msg = driver.findElement( By.name( "success" ) );
        assertEquals( "Office visit created successfully", msg.getText() );
    }

    /**
     * Navigate to prescription viewing
     */
    @When ( "^I choose to view my prescriptions$" )
    public void viewPrescriptions () {
        driver.get( VIEW_URL );
        waitForAngular();
    }

    /**
     * Ensure that a prescription is visible
     *
     * @param drug
     *            drug of prescription
     * @param type
     *            type of prescription
     * @param pharmacy
     *            pharmacy prescription sent to
     * @param dosage
     *            dosage on prescription
     * @param startDate
     *            prescription start date
     * @param endDate
     *            prescription end date
     * @param renewals
     *            number of renewals on prescription
     */
    @Then ( "^I see a prescription for (.+) of the (.+) type from pharmacy (.+) with a dosage of (.+) starting on (.+) and ending on (.+) with (.+) renewals$" )
    public void prescriptionVisible ( final String drug, final String type, final String pharmacy, final String dosage,
            final String startDate, final String endDate, final String renewals ) {
        waitForAngular();
        final List<WebElement> rows = driver.findElements( By.name( "prescriptionTableRow" ) );

        List<WebElement> data = null;
        for ( final WebElement r : rows ) {
            if ( r.getText().contains( drug ) ) {
                waitForAngular();
                data = r.findElements( By.tagName( "td" ) );
                break;
            }
        }

        assertEquals( drug, data.get( 0 ).getText() );
        assertEquals( dosage, data.get( 1 ).getText() );
        assertEquals( type, data.get( 2 ).getText() );
        assertEquals( pharmacy, data.get( 3 ).getText() );
        assertEquals( startDate, data.get( 4 ).getText() );
        assertEquals( endDate, data.get( 5 ).getText() );
        assertEquals( renewals, data.get( 6 ).getText() );
    }

    /**
     * Navigate to page for adding drugs
     */
    @When ( "^I choose to add a new drug$" )
    public void addDrug () {
        driver.get( DRUG_URL );
    }

    /**
     * Add a drug to the system
     *
     * @param ndc
     *            NDC of the drug
     * @param name
     *            name of the drug
     * @param description
     *            description of the drug
     * @throws InterruptedException
     *             thrown if issue creating drug
     */
    @When ( "^submit the values for NDC (.+), name (.+), and description (.*)$" )
    public void submitDrug ( final String ndc, final String name, final String description )
            throws InterruptedException {

        waitForAngular();
        assertEquals( "Admin Manage Drugs", driver.findElement( By.tagName( "h3" ) ).getText() );

        waitForAngular();
        enterValue( "drug", name );
        enterValue( "code", ndc );
        enterValue( "description", description );
        driver.findElement( By.name( "submit" ) ).click();
    }

    /**
     * Ensure drug added successfully
     *
     * @param drug
     *            name of drug
     * @throws InterruptedException
     *             thrown if issue checking drug
     */
    @Then ( "^the drug (.+) is successfully added to the system$" )
    public void drugSuccessful ( final String drug ) throws InterruptedException {
        waitForAngular();
        assertEquals( "", driver.findElement( By.id( "errP" ) ).getText() );

        for ( final WebElement r : driver.findElements( By.name( "drugTableRow" ) ) ) {
            if ( r.getText().contains( drug ) ) {
                r.findElement( By.name( "deleteDrug" ) ).click();
            }
        }
        waitForAngular();

        try {
            assertFalse( driver.findElement( By.tagName( "body" ) ).getText().contains( drug ) );

        }
        catch ( final Exception e ) {
            fail();
        }
    }

    /**
     * Precondition: the user is a pharmacist
     *
     * @param username
     *            user to check
     */
    @Given ( "^user (.+) is a pharmacist$" )
    public void createPharmacist ( final String username ) {
        Pharmacist pharma = Pharmacist.getByName( username );
        if ( pharma == null ) {
            User u = User.getByName( username );
            if ( u == null ) {
                u = new User( username, "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.",
                        Role.ROLE_PHARMACIST, 1 );
                u.save();
                try {
                    Thread.sleep( 1500 );
                }
                catch ( final InterruptedException e ) {
                    e.printStackTrace();
                }
            }
            pharma = new Pharmacist();
            pharma.setSelf( u );
            pharma.setFirstName( "Pharma" );
            pharma.setLastName( "The Real Pharmacist" );
            pharma.setEmail( "csc326.201.1@gmail.com" );
            pharma.setAddress1( "1234 Road St." );
            pharma.setCity( "town" );
            pharma.setState( State.AK );
            pharma.setZip( "12345" );
            pharma.setPhone( "111-222-3333" );
            pharma.setPharmacyId( "Big pharma" );
            pharma.save();

            try {
                Thread.sleep( 1500 );
            }
            catch ( final InterruptedException e ) {
                e.printStackTrace();
            }
        }

        if ( Pharmacy.getByName( "Big pharma" ) == null ) {
            final Pharmacy pharm = new Pharmacy( "Big pharma", "124 Main St", "12345", State.NC );
            pharm.save();
            try {
                Thread.sleep( 1500 );
            }
            catch ( final InterruptedException e ) {
                e.printStackTrace();
            }
        }

        if ( !"Big pharma".equals( pharma.getPharmacyId() ) ) {
            pharma.setPharmacyId( "Big pharma" );
            pharma.save();

            try {
                Thread.sleep( 1500 );
            }
            catch ( final InterruptedException e ) {
                e.printStackTrace();
            }
        }

        if ( Pharmacist.getByName( username ) == null || Pharmacy.getByName( "Big pharma" ) == null ) {
            fail( "No create in time : v (" );
        }
    }

    /**
     * Precondition: assume a prescription has been prescribed for patient
     *
     * @param prescription
     *            name of drug
     * @param patient
     *            patient prescription is for
     */
    @Given ( "^a (.+) prescription has been requested for (.+)$" )
    public void assignPrescription ( final String prescription, final String patient ) {
        Prescription presc = Prescription.getForPatient( patient ).stream().filter(
                p -> p.getDrug().getName().equals( prescription ) && "Big pharma".equals( p.getPharmacyName() ) )
                .findFirst().orElse( null );
        if ( presc == null ) {
            presc = new Prescription();

            final Drug d = new Drug();
            d.setCode( "1234-1234-12" );
            d.setDescription( "A drug." );
            d.setName( prescription );
            d.save();
            presc.setDrug( d );
            presc.setFilled( false );
            presc.setDosage( 100 );
            presc.setEndDate( LocalDate.now() );
            presc.setStartDate( LocalDate.now().minusDays( 1 ) );
            presc.setRenewals( 10 );
            presc.setType( PrescriptionType.GENERIC );
            presc.setPatient( User.getByName( patient ) );
            presc.setPharmacyName( "Big pharma" );
            presc.save();
            try {
                Thread.sleep( 1500 );
            }
            catch ( final InterruptedException e ) {
                e.printStackTrace();
            }
        }
        else if ( presc.isFilled() ) {
            presc.setFilled( false );
            presc.save();
            try {
                Thread.sleep( 1500 );
            }
            catch ( final InterruptedException e ) {
                e.printStackTrace();
            }
        }

        if ( Prescription.getForPatient( patient ).stream().filter(
                p -> p.getDrug().getName().equals( prescription ) && "Big pharma".equals( p.getPharmacyName() ) )
                .findFirst().orElse( null ) == null ) {
            fail( ":(" );
        }
    }

    /**
     * Navigate to prescription filling page
     */
    @When ( "^I choose to fill prescriptions sent to my pharmacy$" )
    public void navigateToFillPrescription () {
        driver.get( FILL_URL );
    }

    /**
     * Fill a prescription
     *
     * @param prescription
     *            name of prescription to fill
     * @param patient
     *            patient the prescription is for
     */
    @When ( "^I choose to fill the (.+) prescription for patient: (.+)$" )
    public void selectPrescriptionToFill ( final String prescription, final String patient ) {
        waitForAngular();
        try {
            Thread.sleep( 500 );
        }
        catch ( final InterruptedException e ) {
            e.printStackTrace();
        }
        assertTrue( driver.findElement( By.id( "header0" ) ).getText().contains( "Fill Prescription" ) );
        driver.findElement( By.name( "select-" + prescription ) ).click();
        waitForAngular();
    }

    /**
     * Assume that the drug type specified does/does not exist
     *
     * @param which
     *            requested or alternate
     * @param exists
     *            is or is not
     */
    @When ( "^I determine that the (.+) type (.+) available$" )
    public void fillWithAvailableType ( final String which, final String exists ) {
        driver.findElement( By.id( which + "Available" + ( "is".equals( exists ) ? "Yes" : "No" ) ) ).click();
        waitForAngular();
    }

    /**
     * Ensure prescription is successfully filled
     *
     * @throws InterruptedException
     */
    @Then ( "^the prescription is successfully filled$" )
    public void ensureFilled () throws InterruptedException {
        waitForAngular();
        driver.findElement( By.name( "submit" ) ).click();
        waitForAngular();
        Thread.sleep( 1000 );
        assertTrue( driver.findElement( By.name( "success" ) ).isDisplayed() );
    }

    /**
     * Ensure prescription is not filled
     */
    @Then ( "^the prescription is not filled$" )
    public void ensureNotFilled () {
        driver.findElement( By.name( "submit" ) ).click();
        waitForAngular();
        assertTrue( driver.findElement( By.name( "failure" ) ).isDisplayed() );
    }

    @Then ( "I see the user has a default type of (.+) and a default pharmacy of (.+)" )
    public void viewPatientPrescriptionDefaults ( final String type, final String pharmacy ) {
        waitForAngular();

        final Select prescriptionSelect = new Select( driver.findElement( By.name( "prescriptionTypeEntry" ) ) );

        waitForAngular();

        if ( !"null".equals( pharmacy ) ) {
            // final Select pharmacySelect = new Select( driver.findElement(
            // By.name( "pharmacyEntry" ) ) );
            waitForAngular();
            // final WebElement pharmacyEntry =
            // pharmacySelect.getFirstSelectedOption();
            waitForAngular();
            // assertEquals( pharmacy, pharmacyEntry.getText() );
            assertTrue( driver.getPageSource().contains( pharmacy ) );
        }

        waitForAngular();

        final WebElement prescriptionTypeEntry = prescriptionSelect.getFirstSelectedOption();

        waitForAngular();

        assertEquals( type, prescriptionTypeEntry.getText() );

    }

    @When ( "I submit changes to the default type to (.+) and the default Pharmacy to (.+)" )
    public void changePatientDefaults ( final String type, final String pharmacy ) {
        waitForAngular();

        final List<WebElement> prescriptionTypeOptions = driver.findElements( By.name( "prescriptionTypeEntry" ) );
        final List<WebElement> pharmacyOptions = driver.findElements( By.name( "pharmacyEntry" ) );

        for ( final WebElement typeOption : prescriptionTypeOptions ) {
            if ( type.equals( typeOption.getText() ) ) {
                typeOption.click();
                break;
            }
        }

        for ( final WebElement pharmacyOption : pharmacyOptions ) {
            if ( type.equals( pharmacyOption.getText() ) ) {
                pharmacyOption.click();
                break;
            }
        }

        final WebElement submitDefaults = driver.findElement( By.name( "submitDefaults" ) );

        submitDefaults.click();
    }

    @Then ( "the defaults are successfully changed" )
    public void defaultsChangeSuccessful () {
        waitForAngular();

        assertTrue( driver.getPageSource().contains( "Patient's default prescription updated successfully" ) );
        assertTrue( driver.getPageSource().contains( "Patient's default pharmacy updated successfully" ) );
    }

    /**
     * Precondition: assume a prescription has been prescribed for patient
     *
     * @param prescription
     *            name of drug
     * @param patient
     *            patient prescription is for
     */
    @Given ( "^a (.+) prescription has been filled for (.+)$" )
    public void assignFilledPrescription ( final String prescription, final String patient ) {
        Prescription presc = Prescription
                .getForPatient( patient ).stream().filter( p -> p.getDrug().getName().equals( prescription )
                        && "Big pharma".equals( p.getPharmacyName() ) && p.isFilled() && !p.isPickedUp() )
                .findFirst().orElse( null );
        if ( presc == null ) {
            presc = new Prescription();

            final Drug d = new Drug();
            d.setCode( "1234-1234-12" );
            d.setDescription( "A drug." );
            d.setName( prescription );
            d.save();
            presc.setDrug( d );
            presc.setFilled( false );
            presc.setDosage( 100 );
            presc.setEndDate( LocalDate.now() );
            presc.setStartDate( LocalDate.now().minusDays( 1 ) );
            presc.setRenewals( 10 );
            presc.setType( PrescriptionType.GENERIC );
            presc.setPatient( User.getByName( patient ) );
            presc.setPharmacyName( "Big pharma" );
            presc.setFilled( true );
            presc.setPickedUp( false );
            presc.save();
            try {
                Thread.sleep( 1500 );
            }
            catch ( final InterruptedException e ) {
                e.printStackTrace();
            }
        }

        if ( Prescription
                .getForPatient( patient ).stream().filter( p -> p.getDrug().getName().equals( prescription )
                        && "Big pharma".equals( p.getPharmacyName() ) && p.isFilled() && !p.isPickedUp() )
                .findFirst().orElse( null ) == null ) {
            fail( ":(" );
        }
    }

    /**
     * Navigate to prescription recording page
     */
    @When ( "^I choose to record filled prescriptions sent to my pharmacy$" )
    public void navigateToRecordPrescription () {
        waitForAngular();
        driver.get( RECORD_URL );
    }

    /**
     * Fill a prescription
     *
     * @param prescription
     *            name of prescription to fill
     * @param patient
     *            patient the prescription is for
     */
    @When ( "^I choose to record the filled (.+) prescription for patient: (.+)$" )
    public void selectPrescriptionToRecord ( final String prescription, final String patient ) {
        waitForAngular();
        try {
            Thread.sleep( 500 );
        }
        catch ( final InterruptedException e ) {
            e.printStackTrace();
        }
        assertTrue( driver.findElement( By.id( "header0" ) ).getText().contains( "Record Prescription" ) );
        driver.findElement( By.name( "select" ) ).click();
        waitForAngular();
    }

    /**
     * Ensure filled prescription is successfully recorded
     */
    @Then ( "^the filled prescription is successfully recorded$" )
    public void ensureRecorded () {
        driver.findElement( By.name( "submit" ) ).click();
        waitForAngular();
        assertTrue( driver.findElement( By.name( "success" ) ).isDisplayed() );
    }
}
