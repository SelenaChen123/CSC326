package edu.ncsu.csc.itrust2.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust2.models.enums.State;
import edu.ncsu.csc.itrust2.models.persistent.Pharmacy;

/**
 * This class will test the persistence of the Pharmacy class.
 *
 * @author Gavin Shirey
 *
 */
public class PharmacyTest {

    /** A reference that can be used for testing. */
    Pharmacy p;

    /**
     * Creates some test data that can be used throughout further tests.
     */
    @Before
    public void setup () {

        p = new Pharmacy( "Walgreems", "123 Pog Lane", "99099", State.NC );
        p.save();

    }

    /**
     * Tests the basic persistence of the Pharmacy object.
     */
    @Test
    public void testPersistence () {

        assertTrue( Pharmacy.getPharmacies().size() > 0 );

        final Pharmacy temp = Pharmacy.getByName( p.getName() );
        assertNotNull( temp );
        assertEquals( p.getName(), temp.getName() );
        assertEquals( p.getAddress(), temp.getAddress() );
        assertEquals( p.getZip(), temp.getZip() );
        assertEquals( p.getState(), temp.getState() );

    }

    /**
     * This will test the toString method of Pharmacy.
     */
    @Test
    public void testToString () {
        assertEquals( "Walgreems  123 Pog Lane", p.toString() );
    }

    /**
     * This test will attempt to get a Pharmacy that should not exist in the
     * database.
     */
    @Test
    public void testInvalidPharmacy () {
        assertNull( Pharmacy.getByName( "nopharm" ) );
    }

    /**
     * This method will test updating the pharmacy and resaving it.
     */
    @Test
    public void testUpdate () {

        p.setAddress( "123 Not Pog Lane" );
        p.setZip( "00000" );
        p.setState( State.CA );
        Pharmacy p2 = Pharmacy.getByName( p.getName() );
        assertNotEquals( p.getAddress(), p2.getAddress() );
        assertNotEquals( p.getZip(), p2.getZip() );
        assertNotEquals( p.getState(), p2.getState() );

        p.save();
        p2 = Pharmacy.getByName( p.getName() );
        assertEquals( p.getAddress(), p2.getAddress() );
        assertEquals( p.getZip(), p2.getZip() );
        assertEquals( p.getState(), p2.getState() );

    }

}
