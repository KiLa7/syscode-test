package hu.syscode.addressservice.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
public class Address {
    private UUID id;
    private String address;
}
