package edu.ncsu.csc.itrust2.unit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.ncsu.csc.itrust2.forms.pharmacist.PharmacistForm;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.enums.State;
import edu.ncsu.csc.itrust2.models.persistent.Pharmacist;
import edu.ncsu.csc.itrust2.models.persistent.Pharmacy;
import edu.ncsu.csc.itrust2.models.persistent.User;

public class PharmacistFormTest {

    /**
     * Test the PersonnelForm class.
     */
    @Test
    public void testPharmacistForm () {
        final Pharmacist person = new Pharmacist();
        person.setSelf( new User( "username", "password", Role.ROLE_PHARMACIST, 1 ) );
        person.setFirstName( "first" );
        person.setLastName( "last" );
        person.setAddress1( "address1" );
        person.setAddress2( "address2" );
        person.setCity( "city" );
        person.setState( State.NC );
        person.setZip( "27606" );
        person.setPhone( "111-111-1111" );
        person.setEmail( "email@email.com" );

        final Pharmacy pharmacy = new Pharmacy();
        pharmacy.setAddress( "123 int main() street" );
        pharmacy.setName( "Big pharma" );
        pharmacy.setState( State.AR );
        pharmacy.setZip( "12345" );
        person.setPharmacyId( "Big pharma" );

        person.setId( 1L );
        final PharmacistForm form = new PharmacistForm( person );
        assertEquals( "username", form.getSelf() );
        assertEquals( "first", form.getFirstName() );
        assertEquals( "last", form.getLastName() );
        assertEquals( "address1", form.getAddress1() );
        assertEquals( "address2", form.getAddress2() );
        assertEquals( "city", form.getCity() );
        assertEquals( State.NC.getAbbrev(), form.getState() );
        assertEquals( "27606", form.getZip() );
        assertEquals( "111-111-1111", form.getPhone() );
        assertEquals( "email@email.com", form.getEmail() );
        assertEquals( "1", form.getId() );
    }

    /**
     * Test hospital id.
     */
    @Test
    public void testPharmacyID () {
        final Pharmacist person = new Pharmacist();
        final Pharmacy pharmacy = new Pharmacy();
        pharmacy.setName( "Green wall" );
        person.setPharmacyId( "Green wall" );
        assertEquals( "Green wall", person.getPharmacyId() );

    }
}
