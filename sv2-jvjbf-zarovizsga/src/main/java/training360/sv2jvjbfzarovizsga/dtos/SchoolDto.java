package training360.sv2jvjbfzarovizsga.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import training360.sv2jvjbfzarovizsga.models.Address;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SchoolDto {
    private Long id;
    private String schoolName;
    private Address address;
    private List<StudentDto> students = new ArrayList<>();
}
