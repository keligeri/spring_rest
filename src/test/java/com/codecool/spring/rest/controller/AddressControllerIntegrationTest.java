package com.codecool.spring.rest.controller;

import com.codecool.spring.rest.Application;
import com.codecool.spring.rest.model.Address;
import com.codecool.spring.rest.repository.AddressRepository;
import com.codecool.spring.rest.repository.PersonRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.io.IOException;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


// http://www.baeldung.com/spring-boot-testing !!
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = Application.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
@WithMockUser(username = "admin", roles = {"ADMIN"})
public class AddressControllerIntegrationTest {

    private static final String contentType = MediaType.APPLICATION_JSON_UTF8_VALUE;

    private HttpMessageConverter jsonConverter;

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private MockMvc mvc;

    private Address budapest;
    private Address zalaegerszeg;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {
        this.jsonConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);
        assertNotNull("the JSON message converter must not be null", this.jsonConverter);
    }

    @Before
    public void setup() {
        personRepository.deleteAll();
        addressRepository.deleteAll();
        updateDb();

    }

    private void updateDb() {
        budapest = new Address(1146, "Budapest");
        zalaegerszeg = new Address(8900, "Babosdöbréte");

        addressRepository.save(budapest);
        addressRepository.save(zalaegerszeg);
    }

    @Test
    public void getAll_IfFindAll_ThenReturnWithList() throws Exception {
        ResultActions perform = mvc.perform(get("/address/"));
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void getById_IfGetGoodId_ThenReturn2xx() throws Exception {
        ResultActions perform = mvc.perform(get("/address/" + zalaegerszeg.getId()));

        perform.andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.zipCode", is((int) zalaegerszeg.getZipCode())))
                .andExpect(jsonPath("$.city", is(zalaegerszeg.getCity())));
    }

    @Test
    public void getById_IfGetInvalidAddressId_ThenReturn4xxClientError() throws Exception {
        ResultActions perform = mvc.perform(get("/address/" + 1000));
        perform.andExpect(status().isNotFound())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void save_IfGetNullValue_ThenReturn4xxError() throws Exception {
        String addressJson = json(null);
        ResultActions perform = mvc.perform(post("/address/")
                .contentType(contentType)
                .content(addressJson));

        perform.andExpect(status().is4xxClientError());
    }

    @Test
    public void save_IfAddNewAddress_ThenReturn2xxSuccessfulAndSaveToDb() throws Exception {
        Address newAddress = new Address(4500, "Zalaszentiván");
        String addressJson = json(newAddress);

        ResultActions perform = mvc.perform(post("/address/")
                .contentType(contentType).content(addressJson));

        perform.andExpect(status().isOk())
                .andExpect(status().is2xxSuccessful());

        Address savedAddress = addressRepository.findOne(5L);
        assertEquals(newAddress.getCity(), savedAddress.getCity());
        assertEquals(newAddress.getZipCode(), savedAddress.getZipCode());
    }

    @Test
    public void update_IfUpdateAddress_ThenSaveToDb() throws Exception {
        budapest.setCity("XIV. kerület");
        String addressJson = json(budapest);
        ResultActions perform = mvc.perform(put("/address/" + budapest.getId())
                .content(addressJson)
                .contentType(contentType));

        perform.andExpect(status().isOk());
        Address savedAddress = addressRepository.findOne(3L);
        assertEquals(budapest.getCity(), savedAddress.getCity());
        assertEquals(budapest.getZipCode(), savedAddress.getZipCode());
    }

    @Test
    public void update_IfGetWrongId_ThenReturn4xx() throws Exception {
        String addressJson = json(budapest);
        ResultActions perform = mvc.perform(put("/address/" + 1000)
                .content(addressJson)
                .contentType(contentType));

        perform.andExpect(status().is4xxClientError());
    }

    @Test
    public void update_IfGetNullValue_ThenReturn4xx() throws Exception {
        String addressJson = json(null);
        ResultActions perform = mvc.perform(put("/address/" + 2)
                .content(addressJson)
                .contentType(contentType));

        perform.andExpect(status().is4xxClientError());
    }

    @Test
    public void delete_IfDeleteAddress_ThenDeleteFromDb() throws Exception {
        ResultActions perform = mvc.perform(delete("/address/" + zalaegerszeg.getId()));
        perform.andExpect(status().is2xxSuccessful())
                .andExpect(status().isOk());
        assertNull(personRepository.findOne(zalaegerszeg.getId()));
    }

    @Test
    public void delete_IfGetWrongId_ThenReturn4xx() throws Exception {
        ResultActions perform = mvc.perform(delete("/address/" + 1000));
        perform.andExpect(status().is4xxClientError());
    }

    private String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.jsonConverter.write(
                o, MediaType.APPLICATION_JSON_UTF8, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

}
