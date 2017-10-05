package com.codecool.spring.rest.repository;

import com.codecool.spring.rest.model.Address;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends CrudRepository<Address, Long>{

    Address findByCityAndZipCode(String city, Long zipCode);

}
