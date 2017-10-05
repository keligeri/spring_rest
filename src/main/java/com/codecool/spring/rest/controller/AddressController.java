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
    private static final String statusOk = "{\"status\": \"ok\"}";

    @Autowired
    public AddressController(AddressRepository addressRepository, AddressService addressService) {
        this.addressRepository = addressRepository;
        this.addressService = addressService;
    }

    @GetMapping(value = {"/", ""})
    public Iterable<Address> read() {
        return addressRepository.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Address getById(@PathVariable long id) throws AddressNotFoundException {
        Address address = addressRepository.findOne(id);
        isValidAddress(address, id);

        return address;
    }

    @RequestMapping(value = "/add", produces = "application/json", method = RequestMethod.POST)
    public String save(@RequestBody Address address) {
        addressRepository.save(address);
        return statusOk;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public String update(@PathVariable long id, @RequestBody Address updatedAddress) throws AddressNotFoundException {
        Address address = addressRepository.findOne(id);
        isValidAddress(address, id);

        addressService.update(id, updatedAddress);
        return statusOk;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable long id) throws AddressNotFoundException {
        Address address = addressRepository.findOne(id);
        isValidAddress(address, id);

        addressService.deleteDependency(address);
        addressRepository.delete(address);
        return statusOk;
    }

    @ExceptionHandler(AddressNotFoundException.class)
    public void handleAddressNotFound(AddressNotFoundException exception, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }

    private void isValidAddress(Address address, long id) throws AddressNotFoundException {
        if (address == null) {
            throw new AddressNotFoundException("Address with id: " + id + " not found!");
        }
    }

}
