package com.codecool.spring.rest.controller;

import com.codecool.spring.rest.exception.AddressNotFoundException;
import com.codecool.spring.rest.model.Address;
import com.codecool.spring.rest.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/address")
public class AddressController {

    private static final String statusOk = "{\"status\": \"ok\"}";
    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping(value = {"/", ""})
    public List<Address> getAll() {
        return addressService.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Address getById(@PathVariable long id) throws AddressNotFoundException {
        isValidAddress(id);
        return addressService.findById(id);
    }

    @RequestMapping(value = "/", produces = "application/json", method = RequestMethod.POST)
    public String save(@RequestBody Address address) {
        addressService.save(address);
        return statusOk;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public String update(@PathVariable long id, @RequestBody Address updatedAddress) throws AddressNotFoundException {
        isValidAddress(id);

        addressService.update(id, updatedAddress);
        return statusOk;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable long id) throws AddressNotFoundException {
        isValidAddress(id);

        addressService.delete(id);
        return statusOk;
    }

    @ExceptionHandler(AddressNotFoundException.class)
    public void handleAddressNotFound(AddressNotFoundException exception, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }

    private void isValidAddress(long id) throws AddressNotFoundException {
        Address address = addressService.findById(id);
        if (address == null) {
            throw new AddressNotFoundException("Address with id: " + id + " not found!");
        }
    }

}
