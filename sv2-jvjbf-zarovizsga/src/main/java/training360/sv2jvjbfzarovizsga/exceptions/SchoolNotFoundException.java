package training360.sv2jvjbfzarovizsga.exceptions;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class SchoolNotFoundException extends AbstractThrowableProblem {

    public SchoolNotFoundException(long id) {
        super(URI.create("schools/not-found"),
                "Not Found",
                Status.NOT_FOUND,
                String.format("School not found with id: %d", id));
    }
}
