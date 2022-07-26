package training360.sportresults.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import training360.sportresults.model.Sex;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AthleteDto {
    private Long id;
    private String name;
    private Sex sex;
    private List<ResultDto> results = new ArrayList<>();
}
