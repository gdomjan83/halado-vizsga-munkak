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
import training360.sportresults.dto.ResultRankingDto;
import training360.sportresults.dto.ResultDto;
import training360.sportresults.dto.UpdateMeasureCommand;
import training360.sportresults.model.SportType;
import training360.sportresults.service.AthleteResultService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/results")
@AllArgsConstructor
public class ResultController {
    private AthleteResultService service;

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultDto updateResult(@PathVariable("id") long id, @Valid @RequestBody UpdateMeasureCommand updateMeasureCommand) {
        return service.updateResult(id, updateMeasureCommand);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ResultRankingDto> getAllResults(@RequestParam Optional<SportType> sportType) {
        return service.getAllResults(sportType);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Problem> handleValidationError(MethodArgumentNotValidException exception) {
        List<Violation> violations =
                exception.getBindingResult().getFieldErrors().stream()
                        .map((FieldError fe) -> new Violation(fe.getField(), fe.getDefaultMessage()))
                        .collect(Collectors.toList());
        Problem problem = Problem.builder()
                .withType(URI.create("results/validation-error"))
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
