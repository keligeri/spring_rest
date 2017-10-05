package com.codecool.spring.rest.controller;

import com.codecool.spring.rest.exception.AddressNotFoundException;
import com.codecool.spring.rest.model.Address;
import com.codecool.spring.rest.repository.AddressRepository;
import com.codecool.spring.rest.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping(value = "/address")
public class AddressController {

    private final AddressRepository addressRepository;
    private final AddressService addressService;

    @Autowired
    public AddressController(AddressRepository addressRepository, AddressService addressService) {
        this.addressRepository = addressRepository;
        this.addressService = addressService;
    }

    @GetMapping(value = {"/", ""})
    public Iterable<Address> read() {
        return addressRepository.findAll();
    }

    @RequestMapping(value = "/{addressId}", method = RequestMethod.GET)
    public Address getById(@PathVariable Long id) throws AddressNotFoundException {
        Address address = addressRepository.findOne(id);
        if (address == null) {
            throw new AddressNotFoundException("Address with id: " + id + " not found!");
        }

        return address;
    }

    @RequestMapping(value = "/add", produces = "application/json", method = RequestMethod.POST)
    public String save(@RequestBody Address address) {
        addressRepository.save(address);
        return "{\"status\": \"ok\"}";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable long id) throws AddressNotFoundException {
        Address address = addressRepository.findOne(id);
        if (address == null) {
            throw new AddressNotFoundException("Address with id: " + id + " not found!");
        }
        addressService.deleteDependency(address);
        addressRepository.delete(address);
        return "{\"status\": \"ok\"}";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public String update(@PathVariable long id, @RequestBody Address address) {
        addressService.update(id, address);

        return "{\"status\": \"ok\"}";
    }

    @ExceptionHandler(AddressNotFoundException.class)
    public void handleAddressNotFound(AddressNotFoundException exception, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }

}
