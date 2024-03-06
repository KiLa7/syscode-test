package hu.syscode.addressservice.service;

import hu.syscode.addressservice.exception.AddressNotFoundException;
import hu.syscode.addressservice.model.Address;
import hu.syscode.openapi.model.AddressDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final ConversionService conversionService;
    private final ConcurrentHashMap<UUID, Address> inMemoryAddressHolder = new ConcurrentHashMap<>();

    @Override
    public AddressDto getAddressById(UUID id) {
        Address address = getAddress(id);
        return conversionService.convert(address, AddressDto.class);
    }

    @Override
    public AddressDto addNewAddress(UUID id, String address) {
        Address nAddress = new Address().setId(id).setAddress(address);
        inMemoryAddressHolder.put(id, nAddress);
        return conversionService.convert(nAddress, AddressDto.class);
    }

    @Override
    public AddressDto updateAddress(UUID id, String address) {
        Address nAddress = getAddress(id);
        nAddress.setAddress(address);
        inMemoryAddressHolder.put(id, nAddress);
        return conversionService.convert(nAddress, AddressDto.class);
    }

    private Address getAddress(UUID id) {
        Address address = inMemoryAddressHolder.get(id);
        if (address == null)
            throw new AddressNotFoundException(String.format("Couldn't find address with id: %s", id));
        return address;
    }

}
