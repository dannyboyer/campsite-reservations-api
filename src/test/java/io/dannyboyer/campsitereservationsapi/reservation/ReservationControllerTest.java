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

    @MockBean
    private ReservationRepository repository;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void create() {
        var reservation = new Reservation(
                1L,
                "dan@boy.com",
                "dan",
                "boy",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(3)
        );

        Mockito.when(repository.save(reservation)).thenReturn(Mono.just(reservation));

        webTestClient.post()
                .uri("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(reservation))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Reservation.class);
    }

//    @Test
//    void read() {
//
//    }
//
//    @Test
//    void update() {
//    }
//
//    @Test
//    void delete() {
//    }
}