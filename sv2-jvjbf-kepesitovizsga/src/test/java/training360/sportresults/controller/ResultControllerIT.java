package training360.sportresults.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.zalando.problem.Problem;
import org.zalando.problem.violations.ConstraintViolationProblem;
import training360.sportresults.dto.*;
import training360.sportresults.model.Sex;
import training360.sportresults.model.SportType;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(statements = {"delete from results", "delete from athletes"})
class ResultControllerIT {

    @Autowired
    WebTestClient webClient;

    long wrongId;
    AthleteDto athleteDtoWithResults;

    @BeforeEach
    void init() {
        AthleteDto athleteDto1 = webClient.post()
                .uri("api/athletes")
                .bodyValue(new CreateAthleteCommand("John Doe", Sex.MALE))
                .exchange()
                .expectBody(AthleteDto.class).returnResult().getResponseBody();

        AthleteDto athleteDto2 = webClient.post()
                .uri("api/athletes")
                .bodyValue(new CreateAthleteCommand("Jane Doe", Sex.FEMALE))
                .exchange()
                .expectBody(AthleteDto.class).returnResult().getResponseBody();

        webClient.post()
                .uri(uriBuilder -> uriBuilder.path("api/athletes/{id}/results").build(athleteDto1.getId()))
                .bodyValue(new CreateResultCommand("Budapest", LocalDate.of(2022, 5, 10), SportType.HAMMER_THROWING, 65.6))
                .exchange();

        athleteDtoWithResults = webClient.post()
                .uri(uriBuilder -> uriBuilder.path("api/athletes/{id}/results").build(athleteDto1.getId()))
                .bodyValue(new CreateResultCommand("Budapest", LocalDate.of(2022, 5, 11), SportType.HAMMER_THROWING, 67.6))
                .exchange()
                .expectBody(AthleteDto.class).returnResult().getResponseBody();

        webClient.post()
                .uri(uriBuilder -> uriBuilder.path("api/athletes/{id}/results").build(athleteDto2.getId()))
                .bodyValue(new CreateResultCommand("Budapest", LocalDate.of(2022, 5, 10), SportType.SWIMMING, 120.6))
                .exchange();

        AthleteDto athleteDtoWithResult2 = webClient.post()
                .uri(uriBuilder -> uriBuilder.path("api/athletes/{id}/results").build(athleteDto2.getId()))
                .bodyValue(new CreateResultCommand("Budapest", LocalDate.of(2022, 5, 11), SportType.SWIMMING, 119.8))
                .exchange()
                .expectBody(AthleteDto.class).returnResult().getResponseBody();

        wrongId = createWrongId(athleteDtoWithResults, athleteDtoWithResult2);
    }

    @Test
    void testGetAllResults() {
        List<ResultRankingDto> allResults = webClient.get()
                .uri("api/results")
                .exchange()
                .expectBodyList(ResultRankingDto.class)
                .returnResult().getResponseBody();

        assertThat(allResults).hasSize(4).extracting(ResultRankingDto::getAthleteName).contains("John Doe", "Jane Doe");
    }

    @Test
    void testGetResultsBySportTypeSecond() {
        List<ResultRankingDto> results = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("api/results").queryParam("sportType", "SWIMMING").build())
                .exchange()
                .expectBodyList(ResultRankingDto.class)
                .returnResult().getResponseBody();

        assertThat(results).hasSize(2)
                .extracting(ResultRankingDto::getMeasure)
                .containsExactly(119.8, 120.6);
    }

    @Test
    void testGetResultsBySportTypeMeter() {
        List<ResultRankingDto> results = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("api/results").queryParam("sportType", "HAMMER_THROWING").build())
                .exchange()
                .expectBodyList(ResultRankingDto.class)
                .returnResult().getResponseBody();

        assertThat(results).hasSize(2)
                .extracting(ResultRankingDto::getMeasure)
                .containsExactly(67.6, 65.6);
    }

    @Test
    void testUpdateMeasureSuccess() {
        ResultDto resultDto = webClient.put()
                .uri(uriBuilder -> uriBuilder.path("api/results/{id}").build(athleteDtoWithResults.getResults().get(0).getId()))
                .bodyValue(new UpdateMeasureCommand(65.7))
                .exchange()
                .expectBody(ResultDto.class)
                .returnResult().getResponseBody();

        assertThat(resultDto.getMeasure()).isEqualTo(65.7);
    }

    @Test
    void testUpdateMeasureWrongId() {
        String message = webClient.put()
                .uri(uriBuilder -> uriBuilder.path("api/results/{id}").build(wrongId))
                .bodyValue(new UpdateMeasureCommand(65.7))
                .exchange()
                .expectBody(Problem.class)
                .returnResult().getResponseBody().getDetail();

        assertEquals("Result not found with id: " + wrongId, message);
    }


    private long createWrongId(AthleteDto athleteDtoWithResult1, AthleteDto athleteDtoWithResult2) {
        return athleteDtoWithResult1.getResults().get(0).getId() +
                athleteDtoWithResult1.getResults().get(1).getId() +
                athleteDtoWithResult2.getResults().get(0).getId() +
                athleteDtoWithResult2.getResults().get(1).getId();
    }

}