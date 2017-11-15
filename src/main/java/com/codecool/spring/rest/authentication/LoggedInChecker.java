package com.codecool.spring.rest.authentication;

import com.codecool.spring.rest.model.User;
import com.codecool.spring.rest.service.UserDetailsImpl;
import com.codecool.spring.rest.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class LoggedInChecker {

    private final AuthenticationFacade authenticationFacade;

    @Autowired
    public LoggedInChecker(AuthenticationFacade authenticationFacade) {
        this.authenticationFacade = authenticationFacade;
    }

    public User getLoggedInUser() {
        User user = null;

        Authentication authentication = authenticationFacade.getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails) { // principal can be 'anonymousUser'!
                UserDetailsImpl userDetails = (UserDetailsImpl) principal;
                user = userDetails.getUser();
                return user;
            }
        }

        return user;
    }

}
