package training360.sportresults.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.zalando.problem.Problem;
import org.zalando.problem.violations.ConstraintViolationProblem;
import training360.sportresults.dto.AthleteDto;
import training360.sportresults.dto.CreateAthleteCommand;
import training360.sportresults.dto.CreateResultCommand;
import training360.sportresults.model.Sex;
import training360.sportresults.model.SportType;

import javax.validation.ConstraintViolation;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(statements = {"delete from results", "delete from athletes"})
class AthleteControllerIT {

    @Autowired
    WebTestClient webClient;

    AthleteDto athleteDto;

    @BeforeEach
    void init() {
        athleteDto = webClient.post()
                .uri("api/athletes")
                .bodyValue(new CreateAthleteCommand("John Doe", Sex.MALE))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(AthleteDto.class).returnResult().getResponseBody();
    }


    @Test
    void createAthleteSuccessful() {
        assertThat(athleteDto.getId()).isNotEqualTo(null);
        assertThat(athleteDto.getName()).isEqualTo("John Doe");
        assertThat(athleteDto.getResults()).isEmpty();
    }

    @Test
    void testCreateAthleteWrongName() {
        String message = webClient.post()
                .uri("api/athletes")
                .bodyValue(new CreateAthleteCommand("", Sex.MALE))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ConstraintViolationProblem.class).returnResult().getResponseBody().getViolations().get(0).getMessage();

        assertEquals("Name cannot be blank!", message);
    }

    @Test
    void testAddNewResultToAthleteSuccess() {
        AthleteDto withResult = webClient.post()
                .uri(uriBuilder -> uriBuilder.path("api/athletes/{id}/results").build(athleteDto.getId()))
                .bodyValue(new CreateResultCommand("Budapest", LocalDate.of(2022, 5, 11), SportType.HAMMER_THROWING, 65.6))
                .exchange().expectStatus().isCreated()
                .expectBody(AthleteDto.class).returnResult().getResponseBody();


        assertThat(withResult.getResults()).hasSize(1);
        assertThat(withResult.getResults().get(0).getMeasure()).isEqualTo(65.6);
        assertThat(withResult.getResults().get(0).getMeasureUnit()).isEqualTo('m');
    }


    @Test
    void testAthleteNotFound() {
        long wrongId = athleteDto.getId() + 1;

        String message = webClient.post()
                .uri(uriBuilder -> uriBuilder.path("api/athletes/{id}/results").build(wrongId))
                .bodyValue(new CreateResultCommand("Budapest", LocalDate.of(2022, 5, 11), SportType.HAMMER_THROWING, 65.6))
                .exchange().expectStatus().isNotFound()
                .expectBody(Problem.class).returnResult().getResponseBody().getDetail();

        assertEquals("Athlete not found with id: " + wrongId, message);
    }

    @Test
    void testAddNewResultWithWrongPlace() {
        String message = webClient.post()
                .uri(uriBuilder -> uriBuilder.path("api/athletes/{id}/results").build(athleteDto.getId()))
                .bodyValue(new CreateResultCommand("    ", LocalDate.of(2022, 5, 11), SportType.HAMMER_THROWING, 65.6))
                .exchange().expectStatus().isBadRequest()
                .expectBody(ConstraintViolationProblem.class).returnResult().getResponseBody().getViolations().get(0).getMessage();

        assertEquals("Place cannot be blank!", message);
    }

    @Test
    void testAddNewResultWithWrongDate() {
        String message = webClient.post()
                .uri(uriBuilder -> uriBuilder.path("api/athletes/{id}/results").build(athleteDto.getId()))
                .bodyValue(new CreateResultCommand("Budapest", LocalDate.of(2024, 5, 11), SportType.HAMMER_THROWING, 65.6))
                .exchange().expectStatus().isBadRequest()
                .expectBody(ConstraintViolationProblem.class).returnResult().getResponseBody().getViolations().get(0).getMessage();

        assertEquals("Result date must be past or present!", message);
    }

    @Test
    void testAddNewResultWithWrongMeasure() {
        String message = webClient.post()
                .uri(uriBuilder -> uriBuilder.path("api/athletes/{id}/results").build(athleteDto.getId()))
                .bodyValue(new CreateResultCommand("Budapest", LocalDate.of(2022, 5, 11), SportType.HAMMER_THROWING, -23.1))
                .exchange().expectStatus().isBadRequest()
                .expectBody(ConstraintViolationProblem.class).returnResult().getResponseBody().getViolations().get(0).getMessage();

        assertEquals("Measure must be positive!", message);
    }

}