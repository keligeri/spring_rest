package com.codecool.spring.rest.controller;

import com.codecool.spring.rest.Application;
import com.codecool.spring.rest.model.Address;
import com.codecool.spring.rest.model.Person;
import com.codecool.spring.rest.repository.AddressRepository;
import com.codecool.spring.rest.repository.PersonRepository;
import org.junit.Before;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class AddressControllerTest {

    private final static Logger logger = LoggerFactory.getLogger(AddressController.class);
    private static final String contentType = MediaType.APPLICATION_JSON_UTF8_VALUE;

    @Rule public TestName testName = new TestName();

    private MockMvc mockMvc;
    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private Address zalaegerszeg;
    private Address budapest;

    private Person geza;
    private Person sanyi;

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {
        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
        .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
        .findAny()
        .orElse(null);

        assertNotNull("the JSON message converter must not be null", this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        this.personRepository.deleteAll();
        this.addressRepository.deleteAll();

        updateDb();
    }

    public void updateDb() {
        this.budapest = new Address(1146, "Budapest");
        this.zalaegerszeg = new Address(8900, "Babosdöbréte");

        this.addressRepository.save(budapest);
        this.addressRepository.save(zalaegerszeg);

        this.sanyi = new Person("Sándorka", 44, budapest);
        this.geza = new Person("GézaFiam", 17, zalaegerszeg);

        this.personRepository.save(sanyi);
        this.personRepository.save(geza);
    }

    @Test
    public void read_Equals_IfGetUserId() throws Exception {
        logger.info("About execute {}", testName.getMethodName());

        String addressId = String.valueOf(this.budapest.getId());
        int zipCode = (int) this.budapest.getZipCode();
        mockMvc.perform(get("/address/" + addressId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.zipCode", is(zipCode)));
    }

    @Test
    public void read_NotFound_IfGetInvalidAddressId() throws Exception {
        logger.info("About execute {}", testName.getMethodName());
        Address newAddress = new Address(5000, "Babosdöbréte");
        mockMvc.perform(get("/address/" + newAddress.getId()))
                .andExpect(status().isNotFound())
                .andExpect(status().is4xxClientError());
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
        o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

}
