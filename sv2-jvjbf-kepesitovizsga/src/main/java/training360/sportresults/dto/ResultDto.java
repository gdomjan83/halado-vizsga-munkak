package training360.sportresults.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import training360.sportresults.model.SportType;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResultDto {
    private Long id;
    private String place;
    private LocalDate resultDate;
    private SportType sportType;
    private double measure;
    private char measureUnit;
    private String athleteName;
}
