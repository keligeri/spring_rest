package com.codecool.spring.rest.authentication;

import com.codecool.spring.rest.model.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

// https://stackoverflow.com/questions/24122586/how-to-represent-the-spring-security-custom-filter-using-java-configuration
public class JsonAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        if (!request.getHeader("Content-Type").equals("application/json")) {
            throw new AuthenticationServiceException("Not supported Content-Type");
        }

        LoginRequest loginRequest = getLoginRequest(request);

        UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    private LoginRequest getLoginRequest(HttpServletRequest request) {
        LoginRequest loginRequest = null;
        try {
            StringBuffer sb = new StringBuffer();
            String line = null;

            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null){
                sb.append(line);
            }

            //json transformation
            ObjectMapper mapper = new ObjectMapper();
            loginRequest = mapper.readValue(sb.toString(), LoginRequest.class);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return loginRequest;
    }


}
