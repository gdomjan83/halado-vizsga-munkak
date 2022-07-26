package training360.sv2jvjbfzarovizsga.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateStudentCommand {
    @NotBlank
    private String name;

    @Past
    private LocalDate dateOfBirth;
}
