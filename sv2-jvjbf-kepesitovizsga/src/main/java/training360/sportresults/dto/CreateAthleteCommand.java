package training360.sportresults.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import training360.sportresults.model.Sex;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateAthleteCommand {
    @NotBlank(message = "Name cannot be blank!")
    private String name;
    private Sex sex;
}
