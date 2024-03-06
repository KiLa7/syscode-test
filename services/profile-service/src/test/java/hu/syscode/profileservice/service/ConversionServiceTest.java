package hu.syscode.profileservice.service;

import hu.syscode.openapi.model.StudentDto;
import hu.syscode.profileservice.converter.StudentToStudentDtoConverter;
import hu.syscode.profileservice.entity.Student;
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
        conversionService.addConverter(new StudentToStudentDtoConverter());
    }

    @Test
    @DisplayName("student to studentDto test")
    public void studentToStudentDtoTest() {
        UUID id = UUID.randomUUID();
        Student student = new Student().setId(id).setName("name").setEmail("email");
        StudentDto studentDto = conversionService.convert(student, StudentDto.class);
        assertEquals(id, studentDto.getId());
        assertEquals("name", studentDto.getName());
        assertEquals("email", studentDto.getEmail());
    }
}
