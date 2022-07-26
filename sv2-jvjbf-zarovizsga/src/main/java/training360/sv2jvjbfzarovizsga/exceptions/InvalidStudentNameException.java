package training360.sv2jvjbfzarovizsga.exceptions;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class InvalidStudentNameException extends AbstractThrowableProblem {
    public InvalidStudentNameException() {
        super(URI.create("schools/invalid-name"),
                "Invalid name",
                Status.BAD_REQUEST,
                "Student name cannot be blank!");
    }
}
