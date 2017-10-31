package com.codecool.spring.rest.controller;

import com.codecool.spring.rest.model.Address;
import com.codecool.spring.rest.model.Person;
import com.codecool.spring.rest.service.AddressService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*
 * @WebMvcTest --> auto-configure Spring mvc infrastructure for unit tests. Test controllers without starting full HTTP server.
 * @WithMockUser --> mock authentication, because these unitTest use default pass
 */
@RunWith(SpringRunner.class)
@WebMvcTest(AddressController.class)
@WithMockUser
public class AddressControllerTest {

    private final static Logger logger = LoggerFactory.getLogger(AddressController.class);
    private static final String contentType = MediaType.APPLICATION_JSON_UTF8_VALUE;

    @Autowired
    private MockMvc mockMvc;
    private HttpMessageConverter jsonConverter;

    @MockBean
    private AddressService addressService;

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
    public void setup() throws Exception {
        zalaegerszeg = new Address(8900, "Babosdöbréte");
        budapest = new Address(1146, "Budapest");
        Person geza = new Person("GézaFiam", 17, zalaegerszeg);
        Person sanyi = new Person("Sándorka", 44, budapest);
        List<Address> addresses = Arrays.asList(zalaegerszeg, budapest);

        given(addressService.findAll()).willReturn(addresses);
        given(addressService.findById(zalaegerszeg.getId())).willReturn(zalaegerszeg);

    }

    @Test
    public void getAll_IfFindAll_ThenReturnWithList() throws Exception {
        ResultActions perform = mockMvc.perform(get("/address/"));
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void getById_IfGetGoodId_ThenReturn2xx() throws Exception {
        ResultActions perform = mockMvc.perform(get("/address/" + zalaegerszeg.getId()));

        perform.andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.zipCode", is((int) zalaegerszeg.getZipCode())))
                .andExpect(jsonPath("$.city", is(zalaegerszeg.getCity())));
    }

    @Test
    public void getById_IfGetInvalidAddressId_ThenReturn4xxClientError() throws Exception {
        ResultActions perform = mockMvc.perform(get("/address/" + 1000));
        perform.andExpect(status().isNotFound())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void save_IfGetNullValue_ThenReturn4xxError() throws Exception {
        String addressJson = json(null);
        ResultActions perform = mockMvc.perform(post("/address/")
                .contentType(contentType)
                .content(addressJson));

        perform.andExpect(status().is4xxClientError());
    }

    @Test
    public void save_IfAddNewAddress_ThenReturn2xxSuccessful() throws Exception {
        Address newAddress = new Address(4500, "Zalaszentiván");
        doNothing().when(addressService).save(newAddress);

        String addressJson = json(newAddress);
        ResultActions perform = mockMvc.perform(post("/address/")
                .contentType(contentType).content(addressJson));

        perform.andExpect(status().isOk())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void update_IfUpdateAddress_ThenDoNothing() throws Exception {
        doNothing().when(addressService).update(2L, budapest);

        String addressJson = json(budapest);
        ResultActions perform = mockMvc.perform(put("/address/" + budapest.getId())
                .content(addressJson)
                .contentType(contentType));

        perform.andExpect(status().isOk());
    }

    @Test
    public void update_IfGetWrongId_ThenThrowsException() throws Exception {
        String addressJson = json(budapest);
        ResultActions perform = mockMvc.perform(put("/address/" + 1000)
                .content(addressJson)
                .contentType(contentType));

        perform.andExpect(status().is4xxClientError());
    }

    @Test
    public void update_IfGetNullValue_ThenThrowsException() throws Exception {
        String addressJson = json(null);
        ResultActions perform = mockMvc.perform(put("/address/" + 2)
                .content(addressJson)
                .contentType(contentType));

        perform.andExpect(status().is4xxClientError());
    }

    @Test
    public void delete_IfDeleteAddress_ThenDoNothing() throws Exception {
        doNothing().when(addressService).delete(1L);

        ResultActions perform = mockMvc.perform(delete("/address/" + zalaegerszeg.getId()));
        perform.andExpect(status().is2xxSuccessful())
                .andExpect(status().isOk());
    }

    @Test
    public void delete_IfGetWrongId_ThenThrowsException() throws Exception {
        ResultActions perform = mockMvc.perform(delete("/address/" + 1000));
        perform.andExpect(status().is4xxClientError());
    }

    private String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.jsonConverter.write(
                o, MediaType.APPLICATION_JSON_UTF8, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

}
