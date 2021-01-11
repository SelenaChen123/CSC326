package edu.ncsu.csc.itrust2.controllers.pharmacist;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller class responsible for managing the behavior for the Pharmacist
 * Landing Screen
 */
@Controller
public class PharmacistController {

    /**
     * Returns the Landing screen for the Pharmacist
     *
     * @param model
     *            Data from the front end
     * @return The page to display
     */
    @RequestMapping ( value = "pharmacist/index" )
    @PreAuthorize ( "hasRole('ROLE_PHARMACIST')" )
    public String index ( final Model model ) {
        return edu.ncsu.csc.itrust2.models.enums.Role.ROLE_PHARMACIST.getLanding();
    }

    @RequestMapping ( value = "pharmacist/editDemographicsPharmacist" )
    @PreAuthorize ( "hasRole('ROLE_PHARMACIST')" )
    public String editDemo ( final Model model ) {
        return "/pharmacist/editDemographicsPharmacist";
    }

    @RequestMapping ( value = "pharmacist/fillPrescriptions" )
    @PreAuthorize ( "hasRole('ROLE_PHARMACIST')" )
    public String fillPrescriptions ( final Model model ) {
        return "/pharmacist/fillPrescriptions";
    }

    @RequestMapping ( value = "pharmacist/recordPrescriptions" )
    @PreAuthorize ( "hasRole('ROLE_PHARMACIST')" )
    public String recordPrescription ( final Model model ) {
        return "/pharmacist/recordPrescriptions";
    }
}
