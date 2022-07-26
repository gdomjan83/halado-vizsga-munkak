package training360.sv2jvjbfzarovizsga.controllers;

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
import training360.sv2jvjbfzarovizsga.dtos.CreateSchoolCommand;
import training360.sv2jvjbfzarovizsga.dtos.CreateStudentCommand;
import training360.sv2jvjbfzarovizsga.dtos.SchoolDto;
import training360.sv2jvjbfzarovizsga.services.SchoolAndStudentServices;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/schools")
@AllArgsConstructor
public class SchoolController {
    private SchoolAndStudentServices service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SchoolDto addNewSchool(@Valid @RequestBody CreateSchoolCommand command) {
        return service.addNewSchool(command);
    }

    @PostMapping("/{id}/students")
    @ResponseStatus(HttpStatus.CREATED)
    public SchoolDto addStudentToSchool(@PathVariable("id") long id, @Valid @RequestBody CreateStudentCommand command) {
        return service.addNewStudent(id, command);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<SchoolDto> findAllSchools(@RequestParam Optional<String> city) {
        return service.findAllSchools(city);
    }

    @PutMapping("/{id}/students/{stdId}")
    @ResponseStatus(HttpStatus.OK)
    public SchoolDto removeStudentFromSchool(@PathVariable("id") long schoolId, @PathVariable("stdId") long studentId) {
        return service.removeStudentFromSchool(schoolId, studentId);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Problem> handleValidationError(MethodArgumentNotValidException exception) {
        List<Violation> violations =
                exception.getBindingResult().getFieldErrors().stream()
                        .map((FieldError fe) -> new Violation(fe.getField(), fe.getDefaultMessage()))
                        .collect(Collectors.toList());
        Problem problem = Problem.builder()
                .withType(URI.create("schools/validation-error"))
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
