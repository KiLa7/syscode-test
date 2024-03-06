package hu.syscode.addressservice.service;

import hu.syscode.addressservice.converter.AddressToAddressDtoConverter;
import hu.syscode.addressservice.model.Address;
import hu.syscode.openapi.model.AddressDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.support.GenericConversionService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConversionServiceTest {

    private GenericConversionService conversionService;

    @BeforeEach
    public void init() {
        conversionService = new GenericConversionService();
        conversionService.addConverter(new AddressToAddressDtoConverter());
    }

    @Test
    @DisplayName("address to addressDto test")
    public void addressToAddressDtoTest() {
        UUID id = UUID.randomUUID();
        Address address = new Address().setId(id).setAddress("address");
        AddressDto addressDto = conversionService.convert(address, AddressDto.class);
        assertEquals(id, addressDto.getId());
        assertEquals("address", addressDto.getAddress());
    }
}
