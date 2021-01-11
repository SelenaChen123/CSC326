package edu.ncsu.csc.itrust2.apitest;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.itrust2.config.RootConfiguration;
import edu.ncsu.csc.itrust2.forms.admin.PharmacyForm;
import edu.ncsu.csc.itrust2.forms.admin.UserForm;
import edu.ncsu.csc.itrust2.forms.pharmacist.PharmacistForm;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.enums.State;
import edu.ncsu.csc.itrust2.models.persistent.Pharmacy;
import edu.ncsu.csc.itrust2.mvc.config.WebMvcConfiguration;

@RunWith ( SpringJUnit4ClassRunner.class )
@ContextConfiguration ( classes = { RootConfiguration.class, WebMvcConfiguration.class } )
@WebAppConfiguration
public class APIPharmacistTest {

    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    /**
     * Sets up test
     */
    @Before
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
    }

    /**
     * Tests getting a non existent pharmacist and ensures that the correct
     * status is returned.
     *
     * @throws Exception
     */
    @Test
    public void testGetNonExistentPharmacist () throws Exception {
        mvc.perform( get( "/api/v1/pharmacists/-1" ) ).andExpect( status().isNotFound() );
    }

    /**
     * Tests PharmacistAPI
     *
     * @throws Exception
     */
    @Test
    @WithMockUser ( username = "pharmacist", roles = { "PHARMACIST" } )
    // @WithMockUser ( username = "admin", roles = { "ADMIN" } )
    public void testPharmacistAPI () throws Exception {
        mvc.perform( delete( "/api/v1/pharmacists" ) );

        final UserForm pharmacistUser = new UserForm( "pharmacist", "123456", Role.ROLE_PHARMACIST, 1 );
        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( pharmacistUser ) ) );

        final PharmacistForm pharmacist = new PharmacistForm();

        pharmacist.setAddress1( "1 Test Street" );
        pharmacist.setAddress2( "Address Part 2" );
        pharmacist.setCity( "Prag" );
        pharmacist.setEmail( "pharmacist@itrust.cz" );
        pharmacist.setFirstName( "Test" );
        pharmacist.setLastName( "Pharmacist" );
        pharmacist.setPhone( "123-456-7890" );
        pharmacist.setSelf( "pharmacist" );
        pharmacist.setState( State.NC.toString() );
        pharmacist.setZip( "27514" );

        mvc.perform( post( "/api/v1/pharmacists" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( pharmacist ) ) ).andExpect( status().isOk() );

        // Second create should fail
        mvc.perform( post( "/api/v1/pharmacists" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( pharmacist ) ) ).andExpect( status().isConflict() );

        mvc.perform( get( "/api/v1/pharmacists" ) ).andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) );

        mvc.perform( get( "/api/v1/pharmacists/pharmacist" ) ).andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) );

        pharmacist.setCity( "Raleigh" );

        mvc.perform( put( "/api/v1/pharmacists/pharmacist" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( pharmacist ) ) ).andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) );

        // Edit with wrong url ID should fail
        mvc.perform( put( "/api/v1/pharmacists/badpharmacist" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( pharmacist ) ) ).andExpect( status().isConflict() );

        // Edit with matching, but nonexistent ID should fail.
        pharmacist.setSelf( "badpharmacist" );
        mvc.perform( put( "/api/v1/pharmacists/badpharmacist" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( pharmacist ) ) ).andExpect( status().isNotFound() );

        // --------------------------
        // Test pharmacist assignment
        // --------------------------

        final PharmacyForm pharmacy = new PharmacyForm();
        pharmacy.setAddress( "Unknown" );
        pharmacy.setName( "Ominous Lair" );
        pharmacy.setState( State.NC.toString() );
        pharmacy.setZip( "27607" );

        final PharmacyForm pharmacyNew = new PharmacyForm();
        pharmacyNew.setAddress( "Wild west" );
        pharmacyNew.setName( "Le ol pharmacy" );
        pharmacyNew.setState( State.NJ.toString() );
        pharmacyNew.setZip( "12345" );

        final Pharmacy p1 = new Pharmacy( pharmacy );
        p1.save();
        final Pharmacy p2 = new Pharmacy( pharmacyNew );
        p2.save();
        Thread.sleep( 1000 );

        // mvc.perform( post( "/api/v1/pharmacies" ).contentType(
        // MediaType.APPLICATION_JSON )
        // .content( TestUtils.asJsonString( pharmacy ) ) ).andExpect(
        // status().isOk() );
        // mvc.perform( post( "/api/v1/pharmacies" ).contentType(
        // MediaType.APPLICATION_JSON )
        // .content( TestUtils.asJsonString( pharmacyNew ) ) ).andExpect(
        // status().isOk() );

        mvc.perform( post( "/api/v1/assignPharmacist/pharmacist" ).content( "Ominous Lair" ) )
                .andExpect( status().isOk() );
        final String response = mvc.perform( get( "/api/v1/pharmacists/pharmacist" ) ).andReturn().getResponse()
                .getContentAsString();
        assertTrue( response.contains( "Ominous Lair" ) );

        mvc.perform( post( "/api/v1/assignPharmacist/pharmacist" ).content( "Le ol pharmacy" ) )
                .andExpect( status().isOk() );
        assertTrue( mvc.perform( get( "/api/v1/pharmacists/pharmacist" ) ).andReturn().getResponse()
                .getContentAsString().contains( "Le ol pharmacy" ) );

        mvc.perform( delete( "/api/v1/pharmacists" ) );

    }

    @Test
    @WithMockUser ( username = "pharmacist", roles = { "PHARMACIST" } )
    public void testAssignPharmacist () throws Exception {
        // mvc.perform( delete( "/api/v1/pharmacists" ) );
        //
        // final UserForm pharmacistUser = new UserForm( "pharmacist", "123456",
        // Role.ROLE_PHARMACIST, 1 );
        // mvc.perform( post( "/api/v1/users" ).contentType(
        // MediaType.APPLICATION_JSON )
        // .content( TestUtils.asJsonString( pharmacistUser ) ) );
        //
        // final PharmacistForm pharmacist = new PharmacistForm();
        // pharmacist.setAddress1( "1 Test Street" );
        // pharmacist.setAddress2( "Address Part 2" );
        // pharmacist.setCity( "Prag" );
        // pharmacist.setEmail( "pharmacist@itrust.cz" );
        // pharmacist.setFirstName( "Test" );
        // pharmacist.setLastName( "Pharmacist" );
        // pharmacist.setPhone( "123-456-7890" );
        // pharmacist.setSelf( "pharmacist" );
        // pharmacist.setState( State.NC.toString() );
        // pharmacist.setZip( "27514" );
        //
        // mvc.perform( post( "/api/v1/pharmacists" ).contentType(
        // MediaType.APPLICATION_JSON )
        // .content( TestUtils.asJsonString( pharmacist ) ) );

    }

}
