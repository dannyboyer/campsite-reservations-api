package io.dannyboyer.campsitereservationsapi.reservation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservationControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void create() {
        var reservation = new Reservation(
                null,
                "dan@boy.com",
                "dan",
                "boy",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(3),
                ReservationStatus.ACTIVE
        );

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
        var reservation = new Reservation(
                1L,
                "dan@boy.com",
                "daniel",
                "boy",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(3),
                ReservationStatus.ACTIVE
        );

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