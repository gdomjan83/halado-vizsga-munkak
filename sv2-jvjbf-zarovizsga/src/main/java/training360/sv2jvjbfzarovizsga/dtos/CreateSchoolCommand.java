package training360.sv2jvjbfzarovizsga.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateSchoolCommand {
    @NotBlank
    private String schoolName;

    private String postalCode;
    private String city;
    private String street;
    private int houseNumber;
}
