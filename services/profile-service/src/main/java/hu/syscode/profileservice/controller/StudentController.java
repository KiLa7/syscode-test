package hu.syscode.profileservice.controller;

import hu.syscode.openapi.api.StudentApi;
import hu.syscode.openapi.model.StudentDto;
import hu.syscode.profileservice.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class StudentController implements StudentApi {

    private final StudentService studentService;

    @Override
    public ResponseEntity<StudentDto> postStudent(StudentDto studentDto) {
        return new ResponseEntity<>(studentService.createStudent(studentDto.getName(),
                studentDto.getEmail()), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<String> deleteStudentById(UUID studentId) {
        studentService.deleteStudent(studentId);
        return ResponseEntity.ok("Student deleted successfully");
    }

    @Override
    public ResponseEntity<List<StudentDto>> listStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @Override
    public ResponseEntity<StudentDto> putStudent(StudentDto studentDto) {
        return new ResponseEntity<>(studentService.updateStudent(studentDto.getId(),
                studentDto.getName(), studentDto.getEmail()), HttpStatus.OK);
    }
}
