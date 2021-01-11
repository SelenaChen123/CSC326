package edu.ncsu.csc.itrust2.controllers.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.itrust2.forms.pharmacist.PharmacistForm;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.models.persistent.Pharmacist;
import edu.ncsu.csc.itrust2.models.persistent.Pharmacy;
import edu.ncsu.csc.itrust2.models.persistent.User;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

@RestController
@SuppressWarnings ( { "rawtypes", "unchecked" } )
public class APIPharmacistController extends APIController {

    /**
     * Retrieves and returns a list of all Pharmacist stored in the system
     *
     * @return list of pharmacist
     */
    @GetMapping ( BASE_PATH + "/pharmacists" )
    public List<Pharmacist> getPharmacists () {
        return Pharmacist.getPharmacists();
    }

    /**
     * Retrieves and returns the Pharmacist with the username provided
     *
     * @param id
     *            The username of the Pharmacist to be retrieved, as stored in
     *            the Users table
     * @return response
     */
    @GetMapping ( BASE_PATH + "/pharmacists/{id}" )
    public ResponseEntity getPharmacist ( @PathVariable ( "id" ) final String id ) {
        final Pharmacist pharmacist = Pharmacist.getByName( id );
        if ( null == pharmacist ) {
            return new ResponseEntity( errorResponse( "No pharmacist found for id " + id ), HttpStatus.NOT_FOUND );
        }
        else {
            LoggerUtil.log( TransactionType.VIEW_DEMOGRAPHICS, LoggerUtil.currentUser() );
            return new ResponseEntity( pharmacist, HttpStatus.OK );
        }
    }

    /**
     * If you are logged in as a pharmacist, then you can use this convenience
     * lookup to find your own information without remembering your id. This
     * allows you the shorthand of not having to look up the id in between.
     *
     * @return The pharmacist object for the currently authenticated user.
     */
    @GetMapping ( BASE_PATH + "/curPharmacist" )
    @PreAuthorize ( "hasAnyRole('ROLE_PHARMACIST')" )
    public ResponseEntity getCurrentPharmacist () {
        final User self = User.getByName( LoggerUtil.currentUser() );
        final Pharmacist pharmacist = Pharmacist.getByName( self.getUsername() );
        if ( pharmacist == null ) {
            return new ResponseEntity(
                    errorResponse( "Could not find a pharmacist entry for you, " + self.getUsername() ),
                    HttpStatus.NOT_FOUND );
        }
        else {
            LoggerUtil.log( TransactionType.VIEW_DEMOGRAPHICS, self.getUsername(),
                    "Retrieved demographics for user " + self.getUsername() );
            return new ResponseEntity( pharmacist, HttpStatus.OK );
        }
    }

    /**
     * Creates a new Pharmacist record for a User from the RequestBody provided.
     *
     * @param pharmacistF
     *            the Pharmacist to be validated and saved to the database
     * @return response
     */
    @PostMapping ( BASE_PATH + "/pharmacists" )
    public ResponseEntity createPharmacist ( @RequestBody final PharmacistForm pharmacistF ) {
        final User self = User.getByName( LoggerUtil.currentUser() );
        pharmacistF.setSelf( self.getUsername() );
        final Pharmacist pharmacist = new Pharmacist( pharmacistF );
        if ( null != Pharmacist.getByName( pharmacist.getSelf() ) ) {
            return new ResponseEntity(
                    errorResponse( "Pharmacist with the id " + pharmacist.getSelf() + " already exists" ),
                    HttpStatus.CONFLICT );
        }
        try {
            pharmacist.save();
            LoggerUtil.log( TransactionType.CREATE_DEMOGRAPHICS, self );
            return new ResponseEntity( pharmacist, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity(
                    errorResponse( "Could not create " + pharmacist.toString() + " because of " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Updates the Pharmacist with the id provided by overwriting it with the
     * new Pharmacist record that is provided. If the ID provided does not match
     * the ID set in the Patient provided, the update will not take place
     *
     * @param id
     *            The username of the Pharmacist to be updated
     * @param pharmacistF
     *            The updated Pharmacist to save
     * @return response
     */
    @PutMapping ( BASE_PATH + "/pharmacists/{id}" )
    public ResponseEntity updatePharmacist ( @PathVariable final String id,
            @RequestBody final PharmacistForm pharmacistF ) {
        final Pharmacist pharmacist = new Pharmacist( pharmacistF );
        if ( null != pharmacist.getSelf() && null != pharmacist.getSelf().getUsername()
                && !id.equals( pharmacist.getSelf().getUsername() ) ) {
            return new ResponseEntity(
                    errorResponse( "The ID provided does not match the ID of the Pharmacist provided" ),
                    HttpStatus.CONFLICT );
        }
        final Pharmacist dbPharmacist = Pharmacist.getByName( id );
        if ( null == dbPharmacist ) {
            return new ResponseEntity( errorResponse( "No pharmacist found for id " + id ), HttpStatus.NOT_FOUND );
        }
        pharmacist.setId( dbPharmacist.getId() );
        try {
            pharmacist.save();
            LoggerUtil.log( TransactionType.EDIT_DEMOGRAPHICS, LoggerUtil.currentUser() );
            return new ResponseEntity( pharmacist, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity(
                    errorResponse( "Could not update " + pharmacist.toString() + " because of " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Assigns a pharmacist to a new pharmacy
     *
     * @param pharmacistId
     *            pharmacist to assign to pharmacy
     * @param pharmacyId
     *            pharmacy to assign pharmacist to
     * @return response
     */
    @PostMapping ( BASE_PATH + "/assignPharmacist/{pharmacistId}" )
    public ResponseEntity assignPharmacist ( @PathVariable ( "pharmacistId" ) final String pharmacistId,
            @RequestBody final String pharmacyId ) {
        final Pharmacist pharmacist = Pharmacist.getByName( pharmacistId );
        if ( pharmacist == null ) {
            return new ResponseEntity( errorResponse( "No existing pharmacist with name " + pharmacyId ),
                    HttpStatus.NOT_FOUND );
        }
        final Pharmacy pharmacy = Pharmacy.getByName( pharmacyId );
        if ( pharmacy == null ) {
            return new ResponseEntity( errorResponse( "No existing pharmacy with name " + pharmacyId ),
                    HttpStatus.NOT_FOUND );
        }

        // Check if the pharmacist has been assigned previously
        final boolean isAssigned = pharmacist.getPharmacyId() != null;

        try {
            pharmacist.setPharmacyId( pharmacyId );
            pharmacist.save();
            if ( isAssigned ) {
                // If the pharmacist is being moved from a previous assignment,
                // log that
                LoggerUtil.log( TransactionType.MOVE_PHARMACIST, LoggerUtil.currentUser() );
            }
            else {
                LoggerUtil.log( TransactionType.ASSIGN_PHARMACIST, LoggerUtil.currentUser() );
            }
            return new ResponseEntity( HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity(
                    errorResponse( "Could not move " + pharmacist.toString() + " because of " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }

    }

}
