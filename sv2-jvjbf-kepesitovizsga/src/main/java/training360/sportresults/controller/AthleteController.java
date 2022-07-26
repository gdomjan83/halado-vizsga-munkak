package training360.sportresults.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.violations.Violation;
import training360.sportresults.dto.AthleteDto;
import training360.sportresults.dto.CreateAthleteCommand;
import training360.sportresults.dto.CreateResultCommand;
import training360.sportresults.service.AthleteResultService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/athletes")
@AllArgsConstructor
public class AthleteController {
    private AthleteResultService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AthleteDto saveAthlete(@Valid @RequestBody CreateAthleteCommand createAthleteCommand) {
        return service.saveAthlete(createAthleteCommand);
    }

    @PostMapping("/{id}/results")
    @ResponseStatus(HttpStatus.CREATED)
    public AthleteDto saveResultToAthlete(@PathVariable("id") long id, @Valid @RequestBody CreateResultCommand createResultCommand) {
        return service.saveResultToAthlete(id, createResultCommand);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Problem> handleValidationError(MethodArgumentNotValidException exception) {
        List<Violation> violations =
                exception.getBindingResult().getFieldErrors().stream()
                        .map((FieldError fe) -> new Violation(fe.getField(), fe.getDefaultMessage()))
                        .collect(Collectors.toList());
        Problem problem = Problem.builder()
                .withType(URI.create("athletes/validation-error"))
                .withTitle("Validation error")
                .withStatus(Status.BAD_REQUEST)
                .withDetail(exception.getMessage())
                .with("violations", violations)
                .build();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problem);
    }
}
