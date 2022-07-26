package training360.sportresults.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "results")
@Getter
@Setter
@NoArgsConstructor
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "result_place")
    private String place;

    @Column(name = "result_date")
    private LocalDate resultDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "sport_type")
    private SportType sportType;

    @Column(name = "measure_value")
    private double measure;

    @Column(name = "measure_unit")
    private char measureUnit;

    @ManyToOne
    @JoinColumn(name = "athlete_id")
    private Athlete athlete;

    public Result(Long id, String place, LocalDate resultDate, SportType sportType, double measure) {
        this.id = id;
        this.place = place;
        this.resultDate = resultDate;
        this.sportType = sportType;
        this.measure = measure;
        this.measureUnit = sportType.getMeasureUnit();
    }

    public Result(String place, LocalDate resultDate, SportType sportType, double measure) {
        this.place = place;
        this.resultDate = resultDate;
        this.sportType = sportType;
        this.measure = measure;
        this.measureUnit = sportType.getMeasureUnit();
    }

}
