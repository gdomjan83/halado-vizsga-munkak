package training360.sv2jvjbfzarovizsga.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "schools")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class School {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "school_id")
    private Long id;

    @Column(name = "school_name")
    private String schoolName;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "school", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Student> students = new ArrayList<>();

    public School(String schoolName, Address address) {
        this.schoolName = schoolName;
        this.address = address;
    }

    public void addStudent(Student student) {
        students.add(student);
        student.setSchool(this);
    }

    public void removeStudent(Student student) {
        students.remove(student);
        student.setSchool(null);
    }
}
