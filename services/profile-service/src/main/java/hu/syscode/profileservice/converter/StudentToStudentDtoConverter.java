package hu.syscode.profileservice.converter;

import hu.syscode.openapi.model.StudentDto;
import hu.syscode.profileservice.entity.Student;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StudentToStudentDtoConverter implements Converter<Student, StudentDto> {

    @Override
    public StudentDto convert(Student source) {
        return new StudentDto()
                .id(source.getId())
                .name(source.getName())
                .email(source.getEmail());
    }
}
