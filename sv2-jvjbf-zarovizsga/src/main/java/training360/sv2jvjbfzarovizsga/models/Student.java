package training360.sv2jvjbfzarovizsga.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "students")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long id;

    @Column(name = "student_name")
    private String studentName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "school_age_status")
    private SchoolAgeStatus schoolAgeStatus;

    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;

    public Student(String studentName, LocalDate dateOfBirth) {
        this.studentName = studentName;
        this.dateOfBirth = dateOfBirth;
        this.schoolAgeStatus = calculateAgeStatus(dateOfBirth);
    }

    private SchoolAgeStatus calculateAgeStatus(LocalDate dateOfBirth) {
        long age = ChronoUnit.YEARS.between(dateOfBirth, LocalDate.now());
        return (age <= 16 ? SchoolAgeStatus.SCHOOL_AGED : SchoolAgeStatus.NOT_SCHOOL_AGED);
    }
}
