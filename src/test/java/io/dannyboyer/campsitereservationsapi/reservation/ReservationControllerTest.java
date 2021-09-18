package io.dannyboyer.campsitereservationsapi.reservation;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservationControllerTest {

    //todo: setup a test db to avoid writing in the main one

    @Autowired
    private WebTestClient webTestClient;

    //@Test
    void create() {
        var arrivalDate = LocalDateTime.now().plusDays(2);
        var departureDate = arrivalDate.plusDays(1);
        var reservation = new Reservation(
                null,
                "dan@boy.com",
                "dan",
                "boy",
                arrivalDate,
                departureDate
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

    //@Test
    void update() {
        var arrivalDate = LocalDateTime.now().plusDays(2);
        var departureDate = arrivalDate.plusDays(1);
        var reservation = new Reservation(
                1L,
                "dan@boy.com",
                "daniel",
                "boy",
                arrivalDate,
                departureDate
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