package training360.sv2jvjbfzarovizsga.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import training360.sv2jvjbfzarovizsga.models.School;

import java.util.List;
import java.util.Optional;

public interface SchoolRepository extends JpaRepository<School, Long> {
    @Query("select s from School s where :city is null or s.address.city = :city")
    List<School> findSchoolByCity(Optional<String> city);
}
