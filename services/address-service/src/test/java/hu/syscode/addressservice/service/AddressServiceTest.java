package hu.syscode.addressservice.service;

import hu.syscode.addressservice.exception.AddressNotFoundException;
import hu.syscode.addressservice.model.Address;
import hu.syscode.openapi.model.AddressDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AddressServiceTest {

    private static final UUID ADD_ID = UUID.randomUUID();
    private static final String ADD_ADDRESS = "ADDRESS";
    @Mock
    private ConversionService conversionService;

    private AddressService addressService;

    @Captor
    private ArgumentCaptor<Address> addressArgumentCaptor;

    @BeforeEach
    public void init() {
        addressService = new AddressServiceImpl(conversionService);
    }

    @Nested
    @DisplayName("Get address by id tests")
    class GetAddressByIdTests {

        @Test
        @DisplayName("successfully")
        public void successfully() {
            var mapMock = mock(ConcurrentHashMap.class);
            ReflectionTestUtils.setField(addressService, "inMemoryAddressHolder", mapMock);
            when(mapMock.get(ADD_ID)).thenReturn(new Address().setId(ADD_ID).setAddress(ADD_ADDRESS));
            addressService.getAddressById(ADD_ID);
            verify(conversionService, times(1)).convert(addressArgumentCaptor.capture(), eq(AddressDto.class));
            Address address = addressArgumentCaptor.getValue();
            assertEquals(ADD_ID, address.getId());
            assertEquals(ADD_ADDRESS, address.getAddress());
        }

        @Test
        @DisplayName("not found")
        public void notFound() {
            assertThrows(AddressNotFoundException.class, () -> addressService.getAddressById(ADD_ID));
        }
    }

    @Nested
    @DisplayName("Add new address tests")
    class AddNewAddressTests {

        @Test
        @DisplayName("successfully")
        public void successfully() {
            var mapMock = mock(ConcurrentHashMap.class);
            ReflectionTestUtils.setField(addressService, "inMemoryAddressHolder", mapMock);
            addressService.addNewAddress(ADD_ID, ADD_ADDRESS);
            verify(mapMock, times(1)).put(any(), addressArgumentCaptor.capture());
            Address address = addressArgumentCaptor.getValue();
            assertEquals(ADD_ID, address.getId());
            assertEquals(ADD_ADDRESS, address.getAddress());
        }

    }

    @Nested
    @DisplayName("Update address tests")
    class UpdateAddressTests {

        @Test
        @DisplayName("successfully")
        public void successfully() {
            var mapMock = mock(ConcurrentHashMap.class);
            ReflectionTestUtils.setField(addressService, "inMemoryAddressHolder", mapMock);
            when(mapMock.get(ADD_ID)).thenReturn(new Address().setId(ADD_ID).setAddress(ADD_ADDRESS));
            addressService.updateAddress(ADD_ID, "MOD_".concat(ADD_ADDRESS));
            verify(mapMock, times(1)).put(any(), addressArgumentCaptor.capture());
            Address address = addressArgumentCaptor.getValue();
            assertEquals(ADD_ID, address.getId());
            assertEquals("MOD_".concat(ADD_ADDRESS), address.getAddress());
        }

        @Test
        @DisplayName("not found")
        public void notFound() {
            assertThrows(AddressNotFoundException.class, () -> addressService.updateAddress(ADD_ID, ADD_ADDRESS));
        }
    }
}
