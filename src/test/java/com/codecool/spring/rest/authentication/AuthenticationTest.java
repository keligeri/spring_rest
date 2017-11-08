package com.codecool.spring.rest.authentication;

import com.codecool.spring.rest.Application;
import com.codecool.spring.rest.model.Address;
import com.codecool.spring.rest.service.AddressService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = Application.class)
@AutoConfigureMockMvc
public class AuthenticationTest {

    private static final String contentType = MediaType.APPLICATION_JSON_UTF8_VALUE;

    private MockMvc mockMvc;

    @MockBean
    private AddressService addressService;
    @Autowired
    private WebApplicationContext webApplicationContext;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private Address budapest;

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
        mockMvc = webAppContextSetup(webApplicationContext).apply(springSecurity()).build();

        updateDb();
    }

    private void updateDb() {
        budapest = new Address(1146, "Budapest");
        given(addressService.findById(1L)).willReturn(budapest);
    }

    @Test
    public void getVerb_Authenticate_IfLoginAsAdmin() throws Exception {
        ResultActions perform = mockMvc.perform(get("/address").with(user("admin").password("admin")));
        perform.andExpect(status().is2xxSuccessful());
    }

    @Test
    public void getVerb_NotAuthenticate_IfLoginWithWrongPassword() throws Exception {
        ResultActions perform = mockMvc.perform(get("/address").with(user("admin").password("wrongPassword")));
        perform.andExpect(status().is4xxClientError());
    }

    @Test
    public void getVerb_NotAuthenticate_IfLoginWithWrongUsername() throws Exception {
        ResultActions perform = mockMvc.perform(get("/address/1").with(user("adminka").password("admin")));
        perform.andExpect(status().is4xxClientError());
    }

    @Test
    public void getVerb_NotAuthenticate_IfLoginWithUserRole() throws Exception {
        ResultActions perform = mockMvc.perform(get("/address/1").with(user("adminka").password("admin").roles("USER")));
        perform.andExpect(status().is4xxClientError());
    }

    @Test
    public void getVerb_Authenticate_IfLoginAsUser() throws Exception {
        ResultActions perform = mockMvc.perform(get("/address").with(user("user").password("user").roles("USER")));
        perform.andExpect(status().is4xxClientError());
    }

    @Test
    public void postVerb_NotAuthenticate_IfLoginAsUser() throws Exception {
        Address newAddress = new Address(4500, "Zalaszentiván");
        String addressJson = json(newAddress);
        ResultActions perform = mockMvc.perform(post("/address/").with(user("user").password("user").roles("USER"))
                .contentType(contentType)
                .content(addressJson));
        perform.andExpect(status().is4xxClientError());
    }

    @Test
    public void postVerb_Authenticate_IfLoginAsAdmin() throws Exception {
        Address newAddress = new Address(4500, "Zalaszentiván");
        String addressJson = json(newAddress);
        ResultActions perform = mockMvc.perform(post("/address/").with(user("admin").password("admin").roles("ADMIN"))
                .contentType(contentType)
                .content(addressJson));
        perform.andExpect(status().is2xxSuccessful());
    }

    @Test
    public void deleteVerb_NotAuthenticate_IfLoginWithWrongPassword() throws Exception {
        ResultActions perform = mockMvc.perform(delete("/address/" + 1).with(user("asd").password("admin")));
        perform.andExpect(status().is4xxClientError());
    }

    @Test
    public void deleteVerb_Authenticate_IfLoginAsAdmin() throws Exception {
        ResultActions perform = mockMvc.perform(delete("/address/" + 1).with(user("admin").password("admin").roles("ADMIN")));
        perform.andExpect(status().is2xxSuccessful());
    }

    @Test
    public void putVerb_NotAuthenticate_IfLoginWithWrongPass() throws Exception {
        Address newAddress = new Address(4500, "Zalaszentiván");
        String addressJson = json(newAddress);
        ResultActions perform = mockMvc.perform(put("/address/" + 1).with(user("admin").password("wrong"))
                .contentType(contentType)
                .content(addressJson));
        perform.andExpect(status().is4xxClientError());
    }

    @Test
    public void putVerb_Authenticate_IfLoginAsAdmin() throws Exception {
        Address newAddress = new Address(4500, "Zalaszentiván");
        String addressJson = json(newAddress);
        ResultActions perform = mockMvc.perform(put("/address/" + 1).with(user("admin").password("admin").roles("ADMIN"))
                .contentType(contentType)
                .content(addressJson));
        perform.andExpect(status().is2xxSuccessful());
    }

    private String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON_UTF8, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }


}
