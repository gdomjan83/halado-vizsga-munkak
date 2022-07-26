package training360.sportresults.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import training360.sportresults.model.Athlete;

public interface AthleteRepository extends JpaRepository<Athlete, Long> {

}
