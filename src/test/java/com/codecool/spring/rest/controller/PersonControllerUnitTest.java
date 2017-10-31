package com.codecool.spring.rest.controller;

import com.codecool.spring.rest.model.Address;
import com.codecool.spring.rest.model.Person;
import com.codecool.spring.rest.service.PersonService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
@WebMvcTest(PersonController.class)
@WithMockUser
public class PersonControllerUnitTest {

    private static final String contentType = MediaType.APPLICATION_JSON_UTF8_VALUE;

    @MockBean
    private PersonService personService;
    private HttpMessageConverter jsonConverter;

    private Person dani;
    private Person geza;

    @Autowired
    private MockMvc mockMvc;

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
        Address zalaegerszeg = new Address(8900, "Babosdöbréte");
        Address budapest = new Address(1146, "Budapest");

        geza = new Person("GézaFiam", 17, zalaegerszeg);
        dani = new Person("Dánielski", 44, budapest);
        List<Person> persons = Arrays.asList(geza, dani);

        given(personService.findById(dani.getId())).willReturn(dani);
        given(personService.findAll()).willReturn(persons);
    }

    @Test
    public void getAll_WhenInvoke_ThenReturnWithList() throws Exception {
        ResultActions perform = mockMvc.perform(get("/person/"));
        perform
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void getById_WhenInvoke_ThenReturnPersonObject() throws Exception {
        ResultActions perform = mockMvc.perform(get("/person/" + dani.getId()));
        perform
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.name", is(dani.getName())))
                .andExpect(jsonPath("$.age", is(dani.getAge())));
    }

    @Test
    public void getById_WhenGetWrongId_ThenReturn4xxError() throws Exception {
        ResultActions perform = mockMvc.perform(get("/person/" + 1200));
        perform
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void post_WhenPostPerson_ThenReturn2xx() throws Exception {
        Person person = new Person("Gizike", 44);
        String personJson = json(person);

        ResultActions perform = mockMvc.perform(post("/person/")
                .contentType(contentType)
                .content(personJson));
        perform
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void post_WhenPostNull_ThenReturn4xx() throws Exception {
        String personJson = json(null);

        ResultActions perform = mockMvc.perform(post("/person/")
                .contentType(contentType)
                .content(personJson));
        perform
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void update_IfGetWrongId_ThenReturn4xx() throws Exception {
        Person person = new Person("Gizike", 44);
        String personJson = json(person);

        ResultActions perform = mockMvc.perform(put("/person/" + 1000)
                .content(personJson)
                .contentType(contentType));

        perform.andExpect(status().is4xxClientError());
    }

    @Test
    public void update_IfGetNull_ThenReturn4xx() throws Exception {
        String personJson = json(null);

        ResultActions perform = mockMvc.perform(put("/person/" + 1000)
                .content(personJson)
                .contentType(contentType));

        perform.andExpect(status().is4xxClientError());
    }

    @Test
    public void update_IfGetValidPerson_ThenDoNothing() throws Exception {
        doNothing().when(personService).update(dani.getId(), dani);

        String personJson = json(dani);
        ResultActions perform = mockMvc.perform(put("/person/" + dani.getId())
                .content(personJson)
                .contentType(contentType));

        perform.andExpect(status().isOk());
    }

    @Test
    public void delete_WhenGetWrongId_ThenReturn4xxError() throws Exception {
        ResultActions perform = mockMvc.perform(delete("/person/" + 1200));
        perform
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void delete_WhenGetId_ThenReturn2xx() throws Exception {
        doNothing().when(personService).delete(dani.getId());
        ResultActions perform = mockMvc.perform(delete("/person/" + dani.getId()));
        perform
                .andExpect(status().is2xxSuccessful());
    }

    private String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.jsonConverter.write(
                o, MediaType.APPLICATION_JSON_UTF8, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

}
