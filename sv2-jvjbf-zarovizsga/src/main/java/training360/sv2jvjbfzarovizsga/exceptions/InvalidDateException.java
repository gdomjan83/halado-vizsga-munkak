package training360.sv2jvjbfzarovizsga.exceptions;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class InvalidDateException extends AbstractThrowableProblem {
    public InvalidDateException() {
        super(URI.create("schools/invalid-date"),
                "Invalid name",
                Status.BAD_REQUEST,
                "Student date of birth must be in past!");
    }
}
