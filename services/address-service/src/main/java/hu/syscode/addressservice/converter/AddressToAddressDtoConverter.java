package hu.syscode.addressservice.converter;

import hu.syscode.addressservice.model.Address;
import hu.syscode.openapi.model.AddressDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AddressToAddressDtoConverter implements Converter<Address, AddressDto> {

    @Override
    public AddressDto convert(Address address) {
        return new AddressDto()
                .id(address.getId())
                .address(address.getAddress());
    }
}
