package edu.ncsu.csc.itrust2.controllers.api;

import java.util.List;

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

import edu.ncsu.csc.itrust2.forms.admin.PharmacyForm;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.models.persistent.Pharmacy;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

/**
 * This class provides API REST endpoints for the Pharmacy model class. Can get
 * and edit Pharmacies.
 *
 * @author Gavin Shirey
 *
 */
@RestController
@SuppressWarnings ( { "unchecked", "rawtypes" } )
public class APIPharmacyController extends APIController {

    /**
     * Gets the list of pharmacies saved in the database and returns it.
     *
     * @return Returns the list of pharmacies.
     */
    @GetMapping ( BASE_PATH + "/pharmacies" )
    public List<Pharmacy> getPharmacies () {
        return Pharmacy.getPharmacies();
    }

    /**
     * Gets a specific pharmacy based on the given id.
     *
     * @param id
     *            The name of the pharmacy to search.
     * @return Returns the pharmacy and an OK status response if the pharmacy
     *         exists.
     */
    @GetMapping ( BASE_PATH + "/pharmacies/{id}" )
    public ResponseEntity getPharmacy ( @PathVariable ( "id" ) final String id ) {
        final Pharmacy pharmacy = Pharmacy.getByName( id );
        if ( pharmacy != null ) {
            LoggerUtil.log( TransactionType.VIEW_PHARMACY, LoggerUtil.currentUser() );
        }
        return pharmacy == null
                ? new ResponseEntity( errorResponse( "No pharmacy found for name " + id ), HttpStatus.NOT_FOUND )
                : new ResponseEntity( pharmacy, HttpStatus.OK );
    }

    /**
     * API endpoint for creating a new pharmacy.
     *
     * @param pf
     *            The form containing that data associated with the pharmacy.
     * @return Returns an OK status with the pharmacy's data or an error status
     *         in failing cases.
     */
    @PostMapping ( BASE_PATH + "/pharmacies" )
    @PreAuthorize ( "hasRole('ROLE_ADMIN') " )
    public ResponseEntity createPharmacy ( @RequestBody final PharmacyForm pf ) {

        final Pharmacy pharmacy = new Pharmacy( pf );
        if ( Pharmacy.getByName( pharmacy.getName() ) != null ) {
            return new ResponseEntity(
                    errorResponse( "Pharmacy with the name " + pharmacy.getName() + " already exists" ),
                    HttpStatus.CONFLICT );
        }

        try {
            pharmacy.save();
            LoggerUtil.log( TransactionType.CREATE_PHARMACY, LoggerUtil.currentUser() );
            return new ResponseEntity( pharmacy, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity( errorResponse( "Error occured while validating or saving " + pharmacy.toString()
                    + " because of " + e.getMessage() ), HttpStatus.BAD_REQUEST );
        }

    }

    /**
     * API endpoint for editing a currently existing pharmacy.
     *
     * THIS WILL NEED TO BE UPDATED LATER ONCE PRESCRIPTION LISTS ARE PROPERLY
     * UPDATED.
     *
     * @param id
     *            The name of the pharmacy to be updated.
     * @param pf
     *            The form containing the updated data of the pharmacy.
     * @return Returns the data of the Pharmacy on a success, or an error status
     *         in other cases.
     */
    @PutMapping ( BASE_PATH + "/pharmacies/{id}" )
    @PreAuthorize ( "hasRole('ROLE_ADMIN') " )
    public ResponseEntity updatePharmacy ( @PathVariable final String id, @RequestBody final PharmacyForm pf ) {
        final Pharmacy pharmacy = new Pharmacy( pf );
        final Pharmacy dbPharmacy = Pharmacy.getByName( id );

        if ( dbPharmacy == null ) {
            return new ResponseEntity( errorResponse( "No pharmacy found for name " + id ), HttpStatus.NOT_FOUND );
        }

        try {

            pharmacy.save();
            if ( !pharmacy.getName().equals( id ) ) {
                // removes the old pharmacy if it exists.
                dbPharmacy.delete();
            }
            LoggerUtil.log( TransactionType.EDIT_PHARMACY, LoggerUtil.currentUser() );
            return new ResponseEntity( pharmacy, HttpStatus.OK );

        }
        catch ( final Exception e ) {
            return new ResponseEntity( errorResponse( "Could not update " + id + " because of " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * API endpoint for deleting a pharmacy.
     *
     * @param id
     *            The name of the pharmacy to delete.
     * @return Returns an OK status on success, error status with details on
     *         failure.
     */
    @PreAuthorize ( "hasRole('ROLE_ADMIN')" )
    @DeleteMapping ( BASE_PATH + "/pharmacies/{id}" )
    public ResponseEntity deletePharmacy ( @PathVariable final String id ) {
        try {
            final Pharmacy pharmacy = Pharmacy.getByName( id );

            if ( pharmacy == null ) {
                LoggerUtil.log( TransactionType.DELETE_PHARMACY, LoggerUtil.currentUser(),
                        "Could not find pharmacy with id " + id );
                return new ResponseEntity( errorResponse( "No pharmacy found with name " + id ), HttpStatus.NOT_FOUND );
            }

            pharmacy.delete();
            LoggerUtil.log( TransactionType.DELETE_PHARMACY, LoggerUtil.currentUser(),
                    "Deleted pharmacy with name " + pharmacy.getName() );
            return new ResponseEntity( id, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            LoggerUtil.log( TransactionType.DELETE_PHARMACY, LoggerUtil.currentUser(), "Failed to delete pharmacy" );
            return new ResponseEntity( errorResponse( "Could not delete pharmacy: " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

}
