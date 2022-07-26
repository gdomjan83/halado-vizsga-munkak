package training360.sportresults.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class AthleteNotFoundException extends AbstractThrowableProblem {
    public AthleteNotFoundException(long id) {
        super(URI.create("athletes/not-found"), "Not found.", Status.NOT_FOUND, String.format("Athlete not found with id: %d", id));
    }
}
