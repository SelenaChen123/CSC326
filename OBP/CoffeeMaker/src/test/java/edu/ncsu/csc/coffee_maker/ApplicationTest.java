/*
 * Copyright 2012-2015 the original author or authors. Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package edu.ncsu.csc.coffee_maker;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import edu.ncsu.csc.coffee_maker.controllers.MappingController;

/**
 * Tests that the web application is built and runs correctly.
 *
 * Reference: https://spring.io/guides/gs/testing-web/
 *
 * @author Sarah Heckman
 * @author Alex Snezhko
 * @author Dominic Brown
 * @author Selena Chen
 */
@RunWith ( SpringRunner.class )
@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationTest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API.
     */
    @Autowired
    private MockMvc           mockMvc;

    /** Mapping controller for the web application. */
    @Autowired
    private MappingController mappingController;

    /**
     * Testing that the main page (as represented by the controller) loads.
     *
     * @throws Exception
     *             If any errors persist
     */
    @Test
    public void contextLoads () throws Exception {
        // Checks that page loads with status 400 (OK)
        this.mockMvc.perform( get( "/" ) ).andDo( print() ).andExpect( status().isOk() );

        // Checks that the controller for the index page is not null
        assertThat( mappingController ).isNotNull();
    }
}
