package training360.sv2jvjbfzarovizsga.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import training360.sv2jvjbfzarovizsga.models.SchoolAgeStatus;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto {

    private Long id;
    private String name;
    private LocalDate dateOfBirth;
    private SchoolAgeStatus schoolAgeStatus;
    private String schoolName;
}
