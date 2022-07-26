package training360.sportresults.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "athletes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Athlete {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "athlete_name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "athlete_sex")
    private Sex sex;

    @OneToMany(mappedBy = "athlete")
    private List<Result> results = new ArrayList<>();

    public Athlete(String name, Sex sex) {
        this.name = name;
        this.sex = sex;
    }

    public List<Result> getResults() {
        return new ArrayList<>(results);
    }

    public void addResult(Result result) {
        results.add(result);
        result.setAthlete(this);
    }
}
