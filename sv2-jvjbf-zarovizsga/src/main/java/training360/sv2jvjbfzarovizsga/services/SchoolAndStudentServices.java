package training360.sv2jvjbfzarovizsga.services;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import training360.sv2jvjbfzarovizsga.dtos.CreateSchoolCommand;
import training360.sv2jvjbfzarovizsga.dtos.CreateStudentCommand;
import training360.sv2jvjbfzarovizsga.dtos.SchoolDto;
import training360.sv2jvjbfzarovizsga.dtos.StudentDto;
import training360.sv2jvjbfzarovizsga.exceptions.SchoolNotFoundException;
import training360.sv2jvjbfzarovizsga.exceptions.StudentNotFoundException;
import training360.sv2jvjbfzarovizsga.models.Address;
import training360.sv2jvjbfzarovizsga.models.School;
import training360.sv2jvjbfzarovizsga.models.Student;
import training360.sv2jvjbfzarovizsga.repositories.SchoolRepository;
import training360.sv2jvjbfzarovizsga.repositories.StudentRepository;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SchoolAndStudentServices {
    private SchoolRepository schoolRepository;
    private StudentRepository studentRepository;
    private ModelMapper modelMapper;


    public SchoolDto addNewSchool(CreateSchoolCommand command) {
        Address address = new Address(command.getPostalCode(), command.getCity(), command.getStreet(), command.getHouseNumber());
        School school = new School(command.getSchoolName(), address);
        schoolRepository.save(school);
        SchoolDto result = new SchoolDto(school.getId(), school.getSchoolName(), school.getAddress(),
                createStudentDtoList(school.getStudents()));
        return result;
    }

    public SchoolDto addNewStudent(long id, CreateStudentCommand command) {
        Student student = new Student(command.getName(), command.getDateOfBirth());
        School school = schoolRepository.findById(id)
                .orElseThrow(() -> new SchoolNotFoundException(id));
        school.addStudent(student);
        studentRepository.save(student);
        SchoolDto result = new SchoolDto(school.getId(), school.getSchoolName(), school.getAddress(),
                createStudentDtoList(school.getStudents()));
        return result;
    }

    public List<SchoolDto> findAllSchools(Optional<String> city) {
        List<School> found = schoolRepository.findSchoolByCity(city);
        Type resultList = new TypeToken<List<SchoolDto>>(){}.getType();
        return modelMapper.map(found, resultList);
    }

    public SchoolDto removeStudentFromSchool(long schoolId, long studentId) {
        School foundSchool = schoolRepository.findById(schoolId).orElseThrow(() -> new SchoolNotFoundException(schoolId));
        Student foundStudent = foundSchool.getStudents().stream().filter(s -> s.getId() == studentId).findFirst().orElseThrow(() -> new StudentNotFoundException(studentId));
        foundSchool.removeStudent(foundStudent);
        SchoolDto result = new SchoolDto(foundSchool.getId(), foundSchool.getSchoolName(), foundSchool.getAddress(),
                createStudentDtoList(foundSchool.getStudents()));
        return result;
    }

    private List<StudentDto> createStudentDtoList(List<Student> students) {
        return students.stream()
                .map(s -> new StudentDto(s.getId(), s.getStudentName(), s.getDateOfBirth(), s.getSchoolAgeStatus(),
                        s.getSchool().getSchoolName()))
                .collect(Collectors.toList());
    }
}
