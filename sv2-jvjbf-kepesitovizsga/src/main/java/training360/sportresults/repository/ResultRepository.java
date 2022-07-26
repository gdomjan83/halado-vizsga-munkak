package training360.sportresults.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import training360.sportresults.model.Result;
import training360.sportresults.model.SportType;

import java.util.List;
import java.util.Optional;

public interface ResultRepository extends JpaRepository<Result, Long> {

    @Query("select r from Result r where :sportType is null or r.sportType = :sportType order by r.measure asc")
    List<Result> getAllResults(Optional<SportType> sportType);
}
