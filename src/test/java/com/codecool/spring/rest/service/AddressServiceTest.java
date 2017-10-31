package com.codecool.spring.rest.service;

import com.codecool.spring.rest.model.Address;
import com.codecool.spring.rest.repository.AddressRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AddressServiceTest {

    @InjectMocks
    private AddressService addressService;

    @Mock
    private AddressRepository addressRepository;

    private Address zalaegerszeg;
    private Address budapest;

    @Before
    public void setup() throws Exception {
        updateDb();
    }

    private void updateDb() {
        budapest = new Address(1146, "Budapest");
        zalaegerszeg = new Address(8900, "Babosdöbréte");
        List<Address> addressList = Arrays.asList(budapest, zalaegerszeg);

        when(addressRepository.findAll()).thenReturn(addressList);
        when(addressRepository.findOne(1L)).thenReturn(budapest);
    }

    @Test
    public void findAll_IfInvoke_ThenReturnWithList() {
        List<Address> addresses = addressService.findAll();
        assertEquals(2, addresses.size());
        assertEquals(budapest.getCity(), addresses.get(0).getCity());
        assertEquals(zalaegerszeg.getZipCode(), addresses.get(1).getZipCode());
    }

    @Test
    public void findOne_IfInvoke_ThenReturnAddress() {
        Address address = addressService.findById(1L);
        assertEquals(budapest.getZipCode(), address.getZipCode());
        assertEquals(budapest.getCity(), address.getCity());
    }

    @Test
    public void save_IfSaveAddress_ThenReturnNothing() {
        Address newAddress = new Address(2500, "Gyula");
        doNothing().when(addressRepository).save(newAddress);

        addressService.save(newAddress);
    }

    @Test
    public void update_IfInvoke_DoNothing() {
        doNothing().when(addressRepository).save(budapest);
        addressService.update(1, budapest);
    }

    @Test
    public void delete_IfInvoke_DoNothing() {
        doNothing().when(addressRepository).delete(1L);
        addressService.delete(1L);
    }

}
