package training360.sportresults.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResultRankingDto {
    private Long id;
    private String athleteName;
    private String place;
    private LocalDate resultDate;
    private double measure;

}
