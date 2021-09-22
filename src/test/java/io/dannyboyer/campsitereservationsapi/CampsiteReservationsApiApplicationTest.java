package io.dannyboyer.campsitereservationsapi;

import io.dannyboyer.campsitereservationsapi.problem.*;
import io.dannyboyer.campsitereservationsapi.reservation.Reservation;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CampsiteReservationsApiApplicationTest {
    @Autowired
    private WebTestClient webTestClient;

    @Container
    public static final PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres")
            .withUsername("postgres")
            .withPassword("mysecretpassword");

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        String url = String.format("r2dbc:postgresql://%s:%s/%s",
                postgreSQLContainer.getHost(),
                postgreSQLContainer.getFirstMappedPort(),
                postgreSQLContainer.getDatabaseName());
        registry.add("spring.r2dbc.url", () -> url);
        registry.add("spring.r2dbc.username", postgreSQLContainer::getUsername);
        registry.add("spring.r2dbc.password", postgreSQLContainer::getPassword);
        registry.add("spring.flyway.url", postgreSQLContainer::getJdbcUrl);
    }

    @BeforeEach
    public void setUp(ApplicationContext applicationContext, RestDocumentationContextProvider restDocumentation) {
        this.webTestClient = WebTestClient.bindToApplicationContext(applicationContext)
                .configureClient()
                .filter(documentationConfiguration(restDocumentation)
                        .operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint()))
                .build();
    }

    @Test
    @Order(1)
    void create() {
        var arrivalDate = LocalDateTime.now().plusDays(2);
        var departureDate = arrivalDate.plusDays(1);
        var reservation = Reservation.builder()
                .email("dan@boy.com")
                .firstName("dan")
                .lastName("boy")
                .arrivalDate(arrivalDate)
                .departureDate(departureDate)
                .build();

        webTestClient.post()
                .uri("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(reservation))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Reservation.class)
                .value(r -> assertEquals(r.getEmail(), reservation.getEmail()))
                .consumeWith(document("create",
                        responseFields(
                                fieldWithPath("id").description("The user's reservation unique id for confirmation"),
                                fieldWithPath("email").description("The user's email address"),
                                fieldWithPath("firstName").description("The user's first name"),
                                fieldWithPath("lastName").description("The user's last name"),
                                fieldWithPath("arrivalDate").description("The user's desired arrival date"),
                                fieldWithPath("departureDate").description("The user's desired departure date")
                        )));
    }

    @Test
    @Order(2)
    void create_bookingOverlap() {
        var arrivalDate = LocalDateTime.now().plusDays(2);
        var departureDate = arrivalDate.plusDays(1);
        var reservation = Reservation.builder()
                .email("dan@boy.com")
                .firstName("dan")
                .lastName("boy")
                .arrivalDate(arrivalDate)
                .departureDate(departureDate)
                .build();

        webTestClient.post()
                .uri("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(reservation))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ApiError.class)
                .value(e -> assertEquals(e.getErrorCode(), ReservationDatabaseIntegrity.API_ERROR_CODE));
    }

    @Test
    @Order(3)
    void update() {
        var arrivalDate = LocalDateTime.now().plusDays(2);
        var departureDate = arrivalDate.plusDays(1);
        var reservation = Reservation.builder()
                .id(1L)
                .email("dan@boy.com")
                .firstName("dan")
                .lastName("boy")
                .arrivalDate(arrivalDate)
                .departureDate(departureDate)
                .build();

        webTestClient.put()
                .uri("/reservations/1")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(reservation))
                .exchange()
                .expectStatus().isOk()
                .expectBody(Reservation.class)
                .value(r -> assertEquals(r.getEmail(), reservation.getEmail()));
    }

    @Test
    @Order(4)
    void get() {
        webTestClient.get()
                .uri("/reservations/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Reservation.class)
                .value(r -> assertEquals("dan", r.getFirstName()));
    }

    @Test
    @Order(5)
    void getInRange() {
        webTestClient.get()
                .uri("/reservations/")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ArrayList.class)
                .value(list -> assertEquals(1, list.size()));
    }

    @Test
    @Order(6)
    void delete() {
        webTestClient.delete()
                .uri("/reservations/1")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void reservationNotFound() {
        webTestClient.get()
                .uri("/reservations/111")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ApiError.class)
                .value(e -> assertEquals(ReservationNotFoundException.API_ERROR_CODE, e.getErrorCode()));
    }

    @Test
    void reservationExceed3Days() {
        var arrivalDate = LocalDateTime.now().plusDays(2);
        var departureDate = arrivalDate.plusDays(3);
        var reservation = Reservation.builder()
                .email("dan@boy.com")
                .firstName("dan")
                .lastName("boy")
                .arrivalDate(arrivalDate)
                .departureDate(departureDate)
                .build();

        webTestClient.post()
                .uri("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(reservation))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ApiError.class)
                .value(e -> assertEquals(ReservationExceedLimit.API_ERROR_CODE, e.getErrorCode()));

    }

    @Test
    void reservationShortNotice() {
        var arrivalDate = LocalDateTime.now().plusDays(1);
        var departureDate = arrivalDate.plusDays(2);
        var reservation = Reservation.builder()
                .email("dan@boy.com")
                .firstName("dan")
                .lastName("boy")
                .arrivalDate(arrivalDate)
                .departureDate(departureDate)
                .build();

        webTestClient.post()
                .uri("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(reservation))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ApiError.class)
                .value(e -> assertEquals(ReservationTimeConstraint.API_ERROR_CODE, e.getErrorCode()));

    }

    @Test
    void badRequest() {
        var arrivalDate = LocalDateTime.now().plusDays(1);
        var departureDate = arrivalDate.plusDays(2);
        var reservation = Reservation.builder()
                .email("dan@boy.com")
                .arrivalDate(arrivalDate)
                .departureDate(departureDate)
                .build();

        webTestClient.post()
                .uri("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(reservation))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ApiError.class)
                .value(e -> assertEquals(0, e.getErrorCode()));
    }
}