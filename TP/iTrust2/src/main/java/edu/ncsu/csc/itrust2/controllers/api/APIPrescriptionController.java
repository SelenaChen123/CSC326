package edu.ncsu.csc.itrust2.controllers.api;

import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.itrust2.forms.hcp.PrescriptionForm;
import edu.ncsu.csc.itrust2.models.enums.PrescriptionType;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.models.persistent.Pharmacy;
import edu.ncsu.csc.itrust2.models.persistent.Prescription;
import edu.ncsu.csc.itrust2.models.persistent.User;
import edu.ncsu.csc.itrust2.utils.EmailUtil;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

/**
 * Provides REST endpoints that deal with prescriptions. Exposes functionality
 * to add, edit, fetch, and delete prescriptions.
 *
 * @author Connor
 * @author Kai Presler-Marshall
 */
@RestController
@SuppressWarnings ( { "rawtypes", "unchecked" } )
public class APIPrescriptionController extends APIController {

    /**
     * Adds a new prescription to the system. Requires HCP permissions.
     *
     * @param form
     *            details of the new prescription
     * @return the created prescription
     */
    @PreAuthorize ( "hasAnyRole('ROLE_HCP', 'ROLE_OD', 'ROLE_OPH', 'ROLE_VIROLOGIST', 'ROLE_PHARMACIST')" )
    @PostMapping ( BASE_PATH + "/prescriptions" )
    public ResponseEntity addPrescription ( @RequestBody final PrescriptionForm form ) {
        try {
            final Prescription p = new Prescription( form );
            p.save();
            LoggerUtil.log( TransactionType.PRESCRIPTION_CREATE, LoggerUtil.currentUser(), p.getPatient().getUsername(),
                    "Created prescription with id " + p.getId() );
            return new ResponseEntity( p, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            LoggerUtil.log( TransactionType.PRESCRIPTION_CREATE, LoggerUtil.currentUser(),
                    "Failed to create prescription" );
            return new ResponseEntity( errorResponse( "Could not save the prescription: " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Edits an existing prescription in the system. Matches prescriptions by
     * ids. Requires HCP permissions.
     *
     * @param form
     *            the form containing the details of the new prescription
     * @return the edited prescription
     */
    @PreAuthorize ( "hasAnyRole('ROLE_HCP', 'ROLE_OD', 'ROLE_OPH', 'ROLE_VIROLOGIST', 'ROLE_PHARMACIST')" )
    @PutMapping ( BASE_PATH + "/prescriptions" )
    public ResponseEntity editPrescription ( @RequestBody final PrescriptionForm form ) {
        try {
            final Prescription p = new Prescription( form );
            final Prescription saved = Prescription.getById( p.getId() );
            if ( saved == null ) {
                LoggerUtil.log( TransactionType.PRESCRIPTION_EDIT, LoggerUtil.currentUser(),
                        "No prescription found with id " + p.getId() );
                return new ResponseEntity( errorResponse( "No prescription found with id " + p.getId() ),
                        HttpStatus.NOT_FOUND );
            }
            p.save(); /* Overwrite existing */
            LoggerUtil.log( TransactionType.PRESCRIPTION_EDIT, LoggerUtil.currentUser(), p.getPatient().getUsername(),
                    "Edited prescription with id " + p.getId() );
            return new ResponseEntity( p, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            LoggerUtil.log( TransactionType.PRESCRIPTION_EDIT, LoggerUtil.currentUser(),
                    "Failed to edit prescription" );
            return new ResponseEntity( errorResponse( "Failed to update prescription: " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Sets the prescription marked by the POST request to the specified id as
     * being filled with the given drug type.
     *
     * @param id
     *            the id of the prescription to fill
     * @param type
     *            the drug type (generic or brand-name) that the prescription
     *            has been filled with
     * @return the filled prescription
     * @throws MessagingException
     *             thrown if error sending email
     */
    @PreAuthorize ( "hasAnyRole('ROLE_PHARMACIST')" )
    @PostMapping ( BASE_PATH + "/fillPrescription/{id}" )
    public ResponseEntity fillPrescription ( @PathVariable final Long id, @RequestBody final String type )
            throws MessagingException {
        final Prescription p = Prescription.getById( id );
        if ( p == null ) {
            return new ResponseEntity( errorResponse( "No prescription found with id " + id ), HttpStatus.NOT_FOUND );
        }

        PrescriptionType enumType = null;
        if ( "unavailable".equals( type ) ) {
            final String msg = "Your prescribed medication: " + p.getDrug().getName()
                    + " is unable to be filled at this moment due to lack of supply. Please wait patiently until the drug truck comes again.";
            // EmailUtil.sendEmail( "csc326f20.202.3@gmail.com", "iTrust2: Your
            // prescription cannot be filled", msg );
            EmailUtil.sendEmail( EmailUtil.getEmailByUsername( p.getPatient().getUsername() ),
                    "iTrust2: Your prescription cannot be filled", msg );
            LoggerUtil.log( TransactionType.CREATE_FILL_PRESCRIPTION_EMAIL, LoggerUtil.currentUser(),
                    p.getPatient().getUsername(), "Filled prescription with id " + p.getId() );
            return new ResponseEntity( HttpStatus.BAD_REQUEST );
        }
        else if ( "Generic".equals( type ) ) {
            enumType = PrescriptionType.GENERIC;
        }
        else if ( "Brand Name".equals( type ) ) {
            enumType = PrescriptionType.BRAND_NAME;
        }
        else {
            return new ResponseEntity( HttpStatus.BAD_REQUEST );
        }

        try {
            // Set the prescription as filled, and modify its PrescriptionType
            // field to match what it was filled with
            p.setFilled( true );
            p.setType( enumType );
            final String msg = "Your prescribed medication: " + p.getDrug().getName()
                    + " has been filled. You can now pick it up at " + p.getPharmacyName() + " whenever you are ready.";
            // EmailUtil.sendEmail( "csc326f20.202.3@gmail.com", "iTrust2: Your
            // prescription has been filled", msg );
            EmailUtil.sendEmail( EmailUtil.getEmailByUsername( p.getPatient().getUsername() ),
                    "iTrust2: Your prescription has been filled", msg );
            p.save();
            LoggerUtil.log( TransactionType.PRESCRIPTION_FILLED, LoggerUtil.currentUser(), p.getPatient().getUsername(),
                    "Filled prescription with id " + p.getId() );
            LoggerUtil.log( TransactionType.CREATE_FILL_PRESCRIPTION_EMAIL, LoggerUtil.currentUser(),
                    p.getPatient().getUsername(), "Filled prescription with id " + p.getId() );
            return new ResponseEntity( p, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            LoggerUtil.log( TransactionType.PRESCRIPTION_FILLED, LoggerUtil.currentUser(),
                    "Failed to fill prescription" );
            return new ResponseEntity( errorResponse( "Could not fill the prescription: " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Deletes the prescription with the given id.
     *
     * @param id
     *            the id
     * @return the id of the deleted prescription
     */
    @PreAuthorize ( "hasAnyRole('ROLE_HCP', 'ROLE_OD', 'ROLE_OPH', 'ROLE_VIROLOGIST', 'ROLE_PHARMACIST')" )
    @DeleteMapping ( BASE_PATH + "/prescriptions/{id}" )
    public ResponseEntity deletePrescription ( @PathVariable final Long id ) {
        final Prescription p = Prescription.getById( id );
        if ( p == null ) {
            return new ResponseEntity( errorResponse( "No prescription found with id " + id ), HttpStatus.NOT_FOUND );
        }
        try {
            p.delete();
            LoggerUtil.log( TransactionType.PRESCRIPTION_DELETE, LoggerUtil.currentUser(), p.getPatient().getUsername(),
                    "Deleted prescription with id " + p.getId() );
            return new ResponseEntity( p.getId(), HttpStatus.OK );
        }
        catch ( final Exception e ) {
            LoggerUtil.log( TransactionType.PRESCRIPTION_DELETE, LoggerUtil.currentUser(), p.getPatient().getUsername(),
                    "Failed to delete prescription" );
            return new ResponseEntity( errorResponse( "Failed to delete prescription: " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Returns a collection of all the prescriptions in the system.
     *
     * @return all saved prescriptions
     */
    @PreAuthorize ( "hasAnyRole('ROLE_HCP', 'ROLE_OD', 'ROLE_OPH', 'ROLE_VIROLOGIST', 'ROLE_PATIENT', 'ROLE_PHARMACIST')" )
    @GetMapping ( BASE_PATH + "/prescriptions" )
    public List<Prescription> getPrescriptions () {
        final User self = User.getByName( LoggerUtil.currentUser() );
        if ( self.isDoctor() ) {
            // Return all prescriptions in system
            LoggerUtil.log( TransactionType.PRESCRIPTION_VIEW, LoggerUtil.currentUser(),
                    "HCP viewed a list of all prescriptions" );
            return Prescription.getPrescriptions();
        }
        else {
            // Issue #106
            // Return only prescriptions assigned to the patient
            LoggerUtil.log( TransactionType.PATIENT_PRESCRIPTION_VIEW, LoggerUtil.currentUser(),
                    "Patient viewed a list of their prescriptions" );
            return Prescription.getForPatient( LoggerUtil.currentUser() );
        }
    }

    /**
     * Returns a single prescription using the given id.
     *
     * @param id
     *            the id of the desired prescription
     * @return the requested prescription
     */
    @PreAuthorize ( "hasAnyRole('ROLE_HCP', 'ROLE_OD', 'ROLE_OPH', 'ROLE_VIROLOGIST', 'ROLE_PHARMACIST')" )
    @GetMapping ( BASE_PATH + "/prescriptions/{id}" )
    public ResponseEntity getPrescription ( @PathVariable final Long id ) {
        final Prescription p = Prescription.getById( id );
        if ( p == null ) {
            LoggerUtil.log( TransactionType.PRESCRIPTION_VIEW, LoggerUtil.currentUser(),
                    "Failed to find prescription with id " + id );
            return new ResponseEntity( errorResponse( "No prescription found for " + id ), HttpStatus.NOT_FOUND );
        }
        else {
            LoggerUtil.log( TransactionType.PRESCRIPTION_VIEW, LoggerUtil.currentUser(), "Viewed prescription  " + id );
            return new ResponseEntity( p, HttpStatus.OK );
        }
    }

    /**
     * Returns a list of unfilled prescriptions at a pharmacy
     *
     * @param id
     *            pharmacy id to look for
     * @return list of unfilled prescriptions
     */
    @PreAuthorize ( "hasAnyRole('ROLE_PHARMACIST')" )
    @GetMapping ( BASE_PATH + "/unfilledPrescriptions/{id}" )
    public ResponseEntity getUnfilledPrescriptionsByPharmacy ( @PathVariable final String id ) {
        if ( Pharmacy.getByName( id ) == null ) {
            return new ResponseEntity( "No pharmacy named " + id, HttpStatus.NOT_FOUND );
        }
        final List<Prescription> ret = new ArrayList<>();
        final List<Prescription> all = Prescription.getPrescriptions();
        for ( final Prescription p : all ) {
            if ( id.equals( p.getPharmacyName() ) && !p.isFilled() ) {
                ret.add( p );
            }
        }
        return new ResponseEntity( ret, HttpStatus.OK );
    }

    /**
     * Returns a list of filled prescriptions at a pharmacy
     *
     * @param id
     *            pharmacy id to look for
     * @return list of filled prescriptions
     */
    @PreAuthorize ( "hasAnyRole('ROLE_PHARMACIST')" )
    @GetMapping ( BASE_PATH + "/filledPrescriptions/{id}" )
    public ResponseEntity getFilledPrescriptionsByPharmacy ( @PathVariable final String id ) {
        if ( Pharmacy.getByName( id ) == null ) {
            return new ResponseEntity( "No pharmacy named " + id, HttpStatus.NOT_FOUND );
        }
        final List<Prescription> ret = new ArrayList<>();
        final List<Prescription> all = Prescription.getPrescriptions();
        for ( final Prescription p : all ) {
            if ( id.equals( p.getPharmacyName() ) && p.isFilled() && !p.isPickedUp() ) {
                ret.add( p );
            }
        }
        return new ResponseEntity( ret, HttpStatus.OK );
    }

    /**
     * Adds a picked up prescriptions to a pharmacy's database
     *
     * @param id
     *            pharmacy id to look for
     * @return added picked up prescription
     */
    @PreAuthorize ( "hasAnyRole('ROLE_PHARMACIST')" )
    @PostMapping ( BASE_PATH + "/pickedUpPrescriptions/{id}" )
    public ResponseEntity addPickedUpPrescription ( @PathVariable final Long id ) {
        try {
            final Prescription p = Prescription.getById( id );
            p.setPickedUp( true );
            p.save();
            LoggerUtil.log( TransactionType.PRESCRIPTION_CREATE, LoggerUtil.currentUser(), p.getPatient().getUsername(),
                    "Added picked up prescription with id " + p.getId() );
            return new ResponseEntity( p, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            LoggerUtil.log( TransactionType.PRESCRIPTION_CREATE, LoggerUtil.currentUser(),
                    "Failed to add picked up prescription" );
            return new ResponseEntity( errorResponse( "Could not save the picked up prescription: " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }
}
