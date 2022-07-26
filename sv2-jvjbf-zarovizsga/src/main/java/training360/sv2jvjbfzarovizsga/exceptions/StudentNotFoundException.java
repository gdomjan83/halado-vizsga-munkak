package training360.sv2jvjbfzarovizsga.exceptions;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class StudentNotFoundException extends AbstractThrowableProblem {
    public StudentNotFoundException(long id) {
        super(URI.create("schools/not-found"),
                "Not Found",
                Status.NOT_FOUND,
                String.format("Student not found with id: %d", id));
    }
}
