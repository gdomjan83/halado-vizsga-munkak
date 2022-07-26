package training360.sportresults.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import training360.sportresults.model.SportType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateResultCommand {
    @NotBlank(message = "Place cannot be blank!")
    private String place;

    @PastOrPresent(message = "Result date must be past or present!")
    private LocalDate resultDate;

    private SportType sportType;

    @Positive(message = "Measure must be positive!")
    private double measure;
}
