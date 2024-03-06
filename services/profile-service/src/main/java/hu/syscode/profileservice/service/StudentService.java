package hu.syscode.profileservice.service;

import hu.syscode.openapi.model.StudentDto;

import java.util.List;
import java.util.UUID;

public interface StudentService {
    StudentDto createStudent(String name, String email);

    void deleteStudent(UUID id);

    List<StudentDto> getAllStudents();

    StudentDto updateStudent(UUID id, String name, String email);
}
