package com.codecool.spring.rest.authentication;

import com.codecool.spring.rest.Application;
import com.codecool.spring.rest.controller.AddressController;
import com.codecool.spring.rest.model.Address;
import com.codecool.spring.rest.model.Person;
import com.codecool.spring.rest.repository.AddressRepository;
import com.codecool.spring.rest.repository.PersonRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class AuthenticationTest {

    @Rule public TestName testName = new TestName();
    private final static Logger logger = LoggerFactory.getLogger(AddressController.class);

    private MockMvc mockMvc;

    @Autowired private AddressRepository addressRepository;
    @Autowired private PersonRepository personRepository;
    @Autowired private WebApplicationContext webApplicationContext;

    private Address zalaegerszeg;
    private Address budapest;

    private Person geza;
    private Person sanyi;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
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
    public void getVerb_Authenticate_IfLoginAsAdmin() throws Exception {
        logger.info("About execute {}", testName.getMethodName());
        mockMvc.perform(get("/address").with(user("admin").password("admin").roles("ADMIN")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void getVerb_Authenticate_IfLoginWithWrongPassword() throws Exception {
        logger.info("About execute {}", testName.getMethodName());
        mockMvc.perform(get("/address").with(user("admin").password("wrongPassword").roles("ADMIN")))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void getVerb_Authenticate_IfLoginWithWrongUsername() throws Exception {
        logger.info("About execute {}", testName.getMethodName());
        mockMvc.perform(get("/address/1").with(user("adminka").password("admin").roles("ADMIN")))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void getVerb_Authenticate_IfLoginWithUserRole() throws Exception {
        logger.info("About execute {}", testName.getMethodName());
        mockMvc.perform(get("/address/1").with(user("adminka").password("admin").roles("USER")))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void getVerb_Authenticate_IfLoginAsUser() throws Exception {
        logger.info("About execute {}", testName.getMethodName());
        mockMvc.perform(get("/address").with(user("user").password("user").roles("USER")))
                .andExpect(status().is4xxClientError());
    }


}
