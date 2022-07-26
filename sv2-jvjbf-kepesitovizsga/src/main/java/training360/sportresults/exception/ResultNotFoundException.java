package training360.sportresults.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class ResultNotFoundException extends AbstractThrowableProblem {
    public ResultNotFoundException(long id) {
        super(URI.create("results/not-found"), "Not found.", Status.NOT_FOUND, String.format("Result not found with id: %d", id));
    }
}
