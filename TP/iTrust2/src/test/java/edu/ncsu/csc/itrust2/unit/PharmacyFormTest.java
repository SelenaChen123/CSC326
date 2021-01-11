package edu.ncsu.csc.itrust2.unit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.ncsu.csc.itrust2.forms.admin.PharmacyForm;
import edu.ncsu.csc.itrust2.models.enums.State;
import edu.ncsu.csc.itrust2.models.persistent.Pharmacy;

/**
 * Test class for PharmacyForm.
 *
 * @author Gavin Shirey
 *
 */
public class PharmacyFormTest {

    /**
     * This method tests the PharmacyForm object.
     */
    @Test
    public void testPharmacyForm () {

        final PharmacyForm pf = new PharmacyForm();
        pf.setName( "Test Pharmacy" );
        pf.setAddress( "0000 Test Lane" );
        pf.setZip( "00000" );
        pf.setState( State.NC.getName() );
        final Pharmacy testPharm = new Pharmacy( pf );

        // Make sure the data is carried over.
        assertEquals( pf.getName(), testPharm.getName() );
        assertEquals( pf.getAddress(), testPharm.getAddress() );
        assertEquals( pf.getZip(), testPharm.getZip() );
        assertEquals( pf.getState(), testPharm.getState().getName() );

        final PharmacyForm pf2 = new PharmacyForm( testPharm );
        // Make sure the data is carried over.
        assertEquals( pf2.getName(), testPharm.getName() );
        assertEquals( pf2.getAddress(), testPharm.getAddress() );
        assertEquals( pf2.getZip(), testPharm.getZip() );
        assertEquals( pf2.getState(), testPharm.getState().getName() );

    }

}
