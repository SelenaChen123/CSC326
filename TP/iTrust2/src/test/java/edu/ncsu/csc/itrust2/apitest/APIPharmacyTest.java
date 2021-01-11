package edu.ncsu.csc.itrust2.apitest;

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

import edu.ncsu.csc.itrust2.apitest.TestUtils;
import edu.ncsu.csc.itrust2.config.RootConfiguration;
import edu.ncsu.csc.itrust2.models.enums.State;
import edu.ncsu.csc.itrust2.models.persistent.Pharmacy;
import edu.ncsu.csc.itrust2.mvc.config.WebMvcConfiguration;

/**
 * Test API functionality for the Pharmacy controller.
 *
 * @author Gavin Shirey
 *
 */
@RunWith ( SpringJUnit4ClassRunner.class )
@ContextConfiguration ( classes = { RootConfiguration.class, WebMvcConfiguration.class } )
@WebAppConfiguration
public class APIPharmacyTest {

    /**
     * This object will be used to send API requests to the application.
     */
    private MockMvc               mvc;

    /** Context used to build the sender object. */
    @Autowired
    private WebApplicationContext context;

    /**
     * Test set up.
     */
    @Before
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
    }

    /**
     * Tests trying to get a non existent pharmacy.
     *
     * @throws Exception
     *             In the case of test failure.
     */
    @Test
    @WithMockUser ( username = "admin", roles = { "ADMIN" } )
    public void testNonExistentPharmacy () throws Exception {
        mvc.perform( get( "/api/v1/pharmacies/-1" ) ).andExpect( status().isNotFound() );
        mvc.perform( delete( "/api/v1/pharmacies/-1" ) ).andExpect( status().isNotFound() );
        final Pharmacy p = new Pharmacy( "non existent", "non existent", "non existent", State.RI );
        mvc.perform( put( "/api/v1/pharmacies/-1" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( p ) ) ).andExpect( status().isNotFound() );
    }

    /**
     * Tests attempting to post a Pharmacy.
     *
     * @throws Exception
     *             In the case of test failure.
     */
    @Test
    @WithMockUser ( username = "admin", roles = { "ADMIN" } )
    public void testPharmacyAPI () throws Exception {

        mvc.perform( delete( "/api/v1/pharmacies" ) );
        mvc.perform( delete( "/api/v1/pharmacies/Testing Pharmacy Location" ) );
        mvc.perform( delete( "/api/v1/pharmacies/Updated Pharmacy Location" ) );

        final Pharmacy pharmacy = new Pharmacy( "Testing Pharmacy Location", "1001 Testing Road", "01010", State.NC );
        // insert the pharmacy and ensure that it exists.
        mvc.perform( post( "/api/v1/pharmacies" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( pharmacy ) ) ).andExpect( status().isOk() );
        mvc.perform( get( "/api/v1/pharmacies/Testing Pharmacy Location" ) ).andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) );

        // test putting a duplicate pharmacy
        mvc.perform( post( "/api/v1/pharmacies" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( pharmacy ) ) ).andExpect( status().isConflict() );

        // test updating pharmacy, including changing name
        pharmacy.setAddress( "1001 Updated Lane" );
        mvc.perform( put( "/api/v1/pharmacies/Testing Pharmacy Location" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( pharmacy ) ) ).andExpect( status().isOk() );

        pharmacy.setName( "Updated Pharmacy Location" );
        mvc.perform( put( "/api/v1/pharmacies/Testing Pharmacy Location" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( pharmacy ) ) ).andExpect( status().isOk() );

    }

}
