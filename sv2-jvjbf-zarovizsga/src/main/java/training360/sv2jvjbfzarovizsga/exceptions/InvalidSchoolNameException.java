package training360.sv2jvjbfzarovizsga.exceptions;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class InvalidSchoolNameException extends AbstractThrowableProblem {
    public InvalidSchoolNameException() {
        super(URI.create("schools/invalid-name"),
                "Invalid name",
                Status.BAD_REQUEST,
                "Schoolname cannot be blank!");
    }
}
