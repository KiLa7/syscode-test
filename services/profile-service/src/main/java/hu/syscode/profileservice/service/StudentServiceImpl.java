package hu.syscode.profileservice.service;

import hu.syscode.openapi.model.StudentDto;
import hu.syscode.profileservice.entity.Student;
import hu.syscode.profileservice.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final ConversionService conversionService;

    @Override
    public StudentDto createStudent(String name, String email) {
        Student student = new Student()
                .setName(name)
                .setEmail(email);

        Student saved = studentRepository.save(student);
        return conversionService.convert(saved, StudentDto.class);
    }

    @Override
    public void deleteStudent(UUID id) {
        Student student = findStudent(id);
        studentRepository.delete(student);
    }

    @Override
    public List<StudentDto> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(student -> conversionService.convert(student, StudentDto.class))
                .toList();
    }

    @Override
    public StudentDto updateStudent(UUID id, String name, String email) {
        Student student = findStudent(id);
        student.setName(name)
               .setEmail(email);

        Student saved = studentRepository.save(student);
        return conversionService.convert(saved, StudentDto.class);
    }

    private Student findStudent(UUID id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Couldn't find student with id: %s", id)));
    }
}
