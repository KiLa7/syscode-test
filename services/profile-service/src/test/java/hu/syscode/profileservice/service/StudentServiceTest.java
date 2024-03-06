package hu.syscode.profileservice.service;

import hu.syscode.openapi.client.api.AddressApiClient;
import hu.syscode.openapi.model.StudentDto;
import hu.syscode.profileservice.entity.Student;
import hu.syscode.profileservice.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    private static final UUID STD_ID = UUID.randomUUID();
    private static final String STD_NAME = "student";
    private static final String STD_MAIL = "student@mail.com";
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private ConversionService conversionService;
    @Mock
    private AddressApiClient addressApiClient;

    private StudentService studentService;

    @Captor
    private ArgumentCaptor<Student> studentArgumentCaptor;


    @BeforeEach
    public void init() {
        studentService = new StudentServiceImpl(studentRepository, conversionService, addressApiClient);
    }

    @Nested
    @DisplayName("Create student tests")
    class CreateStudent {

        @Test
        @DisplayName("successfully")
        public void successfully() {
            studentService.createStudent(STD_NAME, STD_MAIL, null);
            verify(studentRepository, times(1)).save(studentArgumentCaptor.capture());
            Student createdStudent = studentArgumentCaptor.getValue();
            assertStudent(STD_NAME, STD_MAIL, createdStudent);
        }
    }

    @Nested
    @DisplayName("Delete student tests")
    class DeleteStudent {

        @Test
        @DisplayName("successfully")
        public void successfully() {
            Student student = createTestStudent(STD_ID, STD_NAME, STD_MAIL);
            when(studentRepository.findById(STD_ID)).thenReturn(Optional.of(student));
            studentService.deleteStudent(STD_ID);
            verify(studentRepository, times(1)).delete(studentArgumentCaptor.capture());
            Student deletedStudent = studentArgumentCaptor.getValue();
            assertEquals(STD_ID, deletedStudent.getId());
            assertStudent(STD_NAME, STD_MAIL, deletedStudent);
        }

        @Test
        @DisplayName("not found")
        public void notFound() {
            assertThrows(EntityNotFoundException.class, () -> studentService.deleteStudent(STD_ID));
        }
    }

    @Nested
    @DisplayName("Get all students tests")
    class GetAllStudents {

        @Test
        @DisplayName("successfully")
        public void successfully() {
            UUID anotherId = UUID.randomUUID();
            Student student = createTestStudent(STD_ID, STD_NAME, STD_MAIL);
            Student another = createTestStudent(anotherId, "another_".concat(STD_NAME), "another_".concat(STD_MAIL));

            when(studentRepository.findAll()).thenReturn(List.of(student, another));
            studentService.getAllStudents();
            verify(conversionService, times(2)).convert(studentArgumentCaptor.capture(), eq(StudentDto.class));
            List<Student> allValues = studentArgumentCaptor.getAllValues();

            Optional<Student> defStudent = allValues.stream().filter(s -> s.getId().equals(STD_ID)).findAny();
            Optional<Student> anotherStudent = allValues.stream().filter(s -> s.getId().equals(anotherId)).findAny();
            assertEquals(2, allValues.size());
            assertStudent(STD_NAME, STD_MAIL, defStudent.get());
            assertStudent("another_".concat(STD_NAME), "another_".concat(STD_MAIL), anotherStudent.get());
        }
    }

    @Nested
    @DisplayName("Update student tests")
    class UpdateStudent {

        @Test
        @DisplayName("successfully")
        public void successfully() {
            Student student = createTestStudent(STD_ID, STD_NAME, STD_MAIL);
            when(studentRepository.findById(STD_ID)).thenReturn(Optional.of(student));
            studentService.updateStudent(STD_ID, "mod_".concat(STD_NAME), "mod_".concat(STD_MAIL), null);
            verify(studentRepository, times(1)).save(studentArgumentCaptor.capture());
            Student updatedStudent = studentArgumentCaptor.getValue();
            assertEquals(STD_ID, updatedStudent.getId());
            assertStudent("mod_".concat(STD_NAME), "mod_".concat(STD_MAIL), updatedStudent);
        }

        @Test
        @DisplayName("not found")
        public void notFound() {
            assertThrows(EntityNotFoundException.class, () -> studentService.updateStudent(STD_ID, STD_NAME, STD_MAIL, null));
        }
    }

    private Student createTestStudent(UUID id, String name, String email) {
        return new Student()
                .setId(id)
                .setName(name)
                .setEmail(email);
    }

    private void assertStudent(String expectedName, String expectedEmail, Student actual) {
        assertEquals(expectedName, actual.getName());
        assertEquals(expectedEmail, actual.getEmail());
    }
}
