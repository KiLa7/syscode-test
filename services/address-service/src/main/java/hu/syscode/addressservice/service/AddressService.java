package hu.syscode.addressservice.service;

import hu.syscode.openapi.model.AddressDto;

import java.util.UUID;

public interface AddressService {
    AddressDto getAddressById(UUID id);

    AddressDto addNewAddress(UUID id, String address);

    AddressDto updateAddress(UUID id, String address);
}
