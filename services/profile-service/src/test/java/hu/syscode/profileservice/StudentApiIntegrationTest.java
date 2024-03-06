package hu.syscode.profileservice;

import hu.syscode.openapi.model.StudentDto;
import hu.syscode.profileservice.entity.Student;
import hu.syscode.profileservice.repository.StudentRepository;
import hu.syscode.profileservice.service.StudentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpStatus.CREATED;

@SpringBootTest(classes = ProfileServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentApiIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentService studentService;

    @Test
    @DisplayName("Create new student")
    public void createNewStudent() {
        StudentDto student = new StudentDto().id(UUID.randomUUID()).name("student").email("student@mail.hu");
        var studentDtoResponseEntity = restTemplate
                .postForEntity("http://localhost:" + port + "/student", student, StudentDto.class);
        assertEquals(CREATED, studentDtoResponseEntity.getStatusCode());
        assertEquals(1, studentRepository.findAll().size());
        assertEquals(1, studentService.getAllStudents().size());
    }

    @Test
    @DisplayName("delete student")
    @Sql(statements = "INSERT INTO student(id, name, email) VALUES ('3b7db010-d7c2-49cf-b765-4112d7029603', 'test', 'test@mail.hu')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void deleteStudents() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        var response = restTemplate.exchange("http://localhost:" + port + "/student" + "/3b7db010-d7c2-49cf-b765-4112d7029603", HttpMethod.DELETE,
                new HttpEntity<>(null, httpHeaders), String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Optional<Student> optionalStudent = studentRepository.findById(UUID.fromString("3b7db010-d7c2-49cf-b765-4112d7029603"));
        assertTrue(optionalStudent.isEmpty());
    }
}
