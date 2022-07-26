package training360.sv2jvjbfzarovizsga;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.zalando.problem.Problem;
import org.zalando.problem.violations.ConstraintViolationProblem;
import training360.sv2jvjbfzarovizsga.dtos.CreateSchoolCommand;
import training360.sv2jvjbfzarovizsga.dtos.CreateStudentCommand;
import training360.sv2jvjbfzarovizsga.dtos.SchoolDto;
import training360.sv2jvjbfzarovizsga.dtos.StudentDto;
import training360.sv2jvjbfzarovizsga.models.SchoolAgeStatus;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(statements = {"delete from students", "delete from schools"})
class SchoolControllerIT {

    @Autowired
    WebTestClient webTestClient;

    SchoolDto school;
    StudentDto studentDto;

    @BeforeEach
    void init() {
        school = webTestClient.post()
                .uri("/api/schools")
                .bodyValue(new CreateSchoolCommand("Petöfi Sándor School", "1123", "Budapest", "Petöfi u.", 8))
                .exchange().expectStatus().isCreated()
                .expectBody(SchoolDto.class).returnResult().getResponseBody();

        webTestClient.post()
                .uri("/api/schools")
                .bodyValue(new CreateSchoolCommand("Radnóti Miklós School", "8000", "Veszprém", "Petöfi u.", 17))
                .exchange();

        SchoolDto schoolWithStudent = webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("/api/schools/{id}/students").build(school.getId()))
                .bodyValue(new CreateStudentCommand("John Doe", LocalDate.of(2012, 11, 7)))
                .exchange().expectStatus().isCreated()
                .expectBody(SchoolDto.class).returnResult().getResponseBody();

        studentDto = schoolWithStudent.getStudents().get(0);

    }

    @Test
    void testCreateSchool() {
        assertThat(school.getSchoolName()).isEqualTo("Petöfi Sándor School");
        assertThat(school.getAddress().getCity()).isEqualTo("Budapest");
    }

    @Test
    void testCreateSchoolWithWrongName() {
        String message = webTestClient.post()
                .uri("/api/schools")
                .bodyValue(new CreateSchoolCommand("   ", "1123", "Budapest", "Petöfi u.", 8))
                .exchange().expectStatus().isBadRequest()
                .expectBody(ConstraintViolationProblem.class).returnResult().getResponseBody().getViolations()
                .get(0).getMessage();

        assertEquals("Schoolname cannot be blank!", message);

    }

    @Test
    void testCreateStudent() {
        assertEquals("John Doe", studentDto.getName());
        assertEquals(SchoolAgeStatus.SCHOOL_AGED, studentDto.getSchoolAgeStatus());
    }

    @Test
    void testCreateStudentWithWrongName() {
        String message = webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("/api/schools/{id}/students").build(school.getId()))
                .bodyValue(new CreateStudentCommand("   ", LocalDate.of(2012, 11, 7)))
                .exchange().expectStatus().isBadRequest()
                .expectBody(ConstraintViolationProblem.class).returnResult().getResponseBody()
                .getViolations().get(0).getMessage();

        assertEquals("Student name cannot be blank!", message);

    }

    @Test
    void testCreateStudentWithWrongDate() {
        String message = webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("/api/schools/{id}/students").build(school.getId()))
                .bodyValue(new CreateStudentCommand("Jack Doe", LocalDate.now().plusDays(5)))
                .exchange().expectStatus().isBadRequest()
                .expectBody(ConstraintViolationProblem.class).returnResult().getResponseBody()
                .getViolations().get(0).getMessage();

        assertEquals("Student date of birth must be in past!", message);
    }

    @Test
    void testCreateStudentWithWrongSchool(){
        long wrongId = school.getId()+10000;
        Problem problem =webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("/api/schools/{id}/students").build(wrongId))
                .bodyValue(new CreateStudentCommand("Jane Doe", LocalDate.of(2012, 11, 7)))
                .exchange().expectStatus().isNotFound()
                .expectBody(Problem.class).returnResult().getResponseBody();

        assertEquals(String.format("School not found with id: %d", wrongId),problem.getDetail());
    }
//
    @Test
    void testGetAllSchools(){
        List<SchoolDto> result = webTestClient.get()
                .uri("api/schools")
                .exchange().expectBodyList(SchoolDto.class).returnResult().getResponseBody();

        assertThat(result).extracting(SchoolDto::getSchoolName)
                .containsExactly("Petöfi Sándor School","Radnóti Miklós School");
    }

    @Test
    void testGetSchoolsByCity(){
        List<SchoolDto> result = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("api/schools").queryParam("city","Veszprém").build())
                .exchange().expectBodyList(SchoolDto.class).returnResult().getResponseBody();

        assertThat(result).extracting(SchoolDto::getSchoolName).
                containsExactly("Radnóti Miklós School");
    }

    @Test
    void testFireStudent(){

        SchoolDto result = webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("api/schools/{id}/students/{stdId}").build(school.getId(),studentDto.getId()))
                .exchange()
                .expectBody(SchoolDto.class).returnResult().getResponseBody();

        assertThat(result.getStudents()).isEmpty();
    }

    @Test
    void testFireStudentWithWrongStudentId(){
        long wrongId = studentDto.getId()+1000;
        Problem problem = webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("api/schools/{id}/students/{stdId}").build(school.getId(),wrongId))
                .exchange().expectStatus().isNotFound()
                .expectBody(Problem.class).returnResult().getResponseBody();

        assertEquals(String.format("Student not found with id: %d", wrongId),problem.getDetail());
    }

    @Test
    void testFireStudentWithNotInSchoolId(){

        SchoolDto other = webTestClient.post()
                .uri("/api/schools")
                .bodyValue(new CreateSchoolCommand("Other", "8000", "Veszprém", "Petöfi u.", 17))
                .exchange().expectBody(SchoolDto.class).returnResult().getResponseBody();

        StudentDto studentDtoOther = webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("/api/schools/{id}/students").build(other.getId()))
                .bodyValue(new CreateStudentCommand("John Doe", LocalDate.of(2012, 11, 7)))
                .exchange().expectBody(SchoolDto.class).returnResult().getResponseBody().getStudents().get(0);

        Problem problem = webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("api/schools/{id}/students/{stdId}").build(school.getId(),studentDtoOther.getId()))
                .exchange().expectStatus().isNotFound()
                .expectBody(Problem.class).returnResult().getResponseBody();

        assertEquals(String.format("Student not found with id: %d", studentDtoOther.getId()),problem.getDetail());
    }

}