package hu.syscode.addressservice.controller;

import hu.syscode.addressservice.service.AddressService;
import hu.syscode.openapi.api.AddressApi;
import hu.syscode.openapi.model.AddressDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AddressController implements AddressApi {

    private final AddressService addressService;

    @Override
    public ResponseEntity<AddressDto> getAddressById(UUID id) {
        return ResponseEntity.ok(addressService.getAddressById(id));
    }

    @Override
    public ResponseEntity<AddressDto> postAddress(AddressDto addressDto) {
        return new ResponseEntity<>(addressService.addNewAddress(addressDto.getId(), addressDto.getAddress()), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<AddressDto> putAddress(AddressDto addressDto) {
        return ResponseEntity.ok(addressService.updateAddress(addressDto.getId(), addressDto.getAddress()));
    }
}
