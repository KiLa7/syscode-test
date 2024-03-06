package hu.syscode.profileservice.service;

import feign.FeignException;
import hu.syscode.openapi.client.api.AddressApiClient;
import hu.syscode.openapi.client.model.AddressDto;
import hu.syscode.openapi.model.StudentDto;
import hu.syscode.profileservice.entity.Student;
import hu.syscode.profileservice.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final ConversionService conversionService;

    private final AddressApiClient addressApiClient;

    @Override
    public StudentDto createStudent(String name, String email, String address) {
        Student student = new Student()
                .setName(name)
                .setEmail(email);

        Student saved = studentRepository.save(student);
        StudentDto studentDto = conversionService.convert(saved, StudentDto.class);
        if (address != null) {
            ResponseEntity<AddressDto> response = addressApiClient.postAddress(createAddress(saved.getId(), address));
            studentDto.setAddress(Objects.requireNonNull(response.getBody()).getAddress());
        }
        return studentDto;
    }

    @Override
    public void deleteStudent(UUID id) {
        Student student = findStudent(id);
        studentRepository.delete(student);
    }

    @Override
    public List<StudentDto> getAllStudents() {
        List<StudentDto> list = studentRepository.findAll()
                .stream()
                .map(student -> conversionService.convert(student, StudentDto.class))
                .toList();

        getAddresses(list);
        return list;
    }

    @Override
    public StudentDto updateStudent(UUID id, String name, String email, String address) {
        Student student = findStudent(id);
        student.setName(name)
               .setEmail(email);

        Student saved = studentRepository.save(student);
        StudentDto studentDto = conversionService.convert(saved, StudentDto.class);
        updateAddress(address, saved, studentDto);
        return studentDto;
    }

    private Student findStudent(UUID id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Couldn't find student with id: %s", id)));
    }

    private AddressDto createAddress(UUID id, String address) {
        return new AddressDto()
                .id(id)
                .address(address);
    }

    private void getAddresses(List<StudentDto> list) {
        list.forEach(studentDto -> {
            try {
                ResponseEntity<AddressDto> addressById = addressApiClient.getAddressById(studentDto.getId());
                studentDto.setAddress(Objects.requireNonNull(addressById.getBody()).getAddress());
            } catch (FeignException.NotFound ex) {
                log.debug(ex.getMessage()); // address is optional therefore no exception is raised
            } catch (Exception ex) {
                log.error(ex.getMessage()); // service might not run, but both should run independently
            }
        });
    }

    private void updateAddress(String address, Student saved, StudentDto studentDto) {
        if (address != null) {
            try {
                ResponseEntity<AddressDto> response = addressApiClient.putAddress(createAddress(saved.getId(), address));
                studentDto.setAddress(Objects.requireNonNull(response.getBody()).getAddress());
            } catch (FeignException.NotFound ex) {
                log.debug(ex.getMessage()); // address is optional therefore no exception is raised
            } catch (Exception ex) {
                log.error(ex.getMessage()); // service might not run, but both should run independently
            }
        }
    }
}
