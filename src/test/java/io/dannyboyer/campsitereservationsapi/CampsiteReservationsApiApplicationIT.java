package io.dannyboyer.campsitereservationsapi;

import io.dannyboyer.campsitereservationsapi.reservation.Reservation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CampsiteReservationsApiApplicationIT {
    @Autowired
    private WebTestClient webTestClient;

    @Container
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres")
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

    @Test
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
                .value(r -> assertEquals(r.getEmail(), reservation.getEmail()));
    }

    @Test
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
}