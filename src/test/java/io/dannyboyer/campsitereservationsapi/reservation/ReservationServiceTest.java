package io.dannyboyer.campsitereservationsapi.reservation;

import io.dannyboyer.campsitereservationsapi.problem.ReservationExceedLimit;
import io.dannyboyer.campsitereservationsapi.problem.ReservationTimeConstraint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository repository;

    @InjectMocks
    private ReservationService service;

    @Test
    void makeReservation_success() {
        var reservation = new Reservation(null,
                "dan@boy.com",
                "dan",
                "boy",
                LocalDateTime.of(2021, 9, 13, 12, 0),
                LocalDateTime.of(2021, 9, 15, 12, 0),
                false
        );

        Mockito.when(repository.save(reservation)).thenReturn(Mono.just(reservation));

        StepVerifier
                .create(service.makeReservation(reservation))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void makeReservation_exceedLimit() {
        var reservation = new Reservation(null,
                "dan@boy.com",
                "dan",
                "boy",
                LocalDateTime.of(2021, 9, 13, 12, 0),
                LocalDateTime.of(2021, 9, 16, 12, 0),
                false
        );

        StepVerifier
                .create(service.makeReservation(reservation))
                .expectErrorMatches(ex -> ex instanceof ReservationExceedLimit)
                .verify();
    }

    @Test
    void makeReservation_arrivalAfterDeparture() {
        var reservation = new Reservation(null,
                "dan@boy.com",
                "dan",
                "boy",
                LocalDateTime.of(2021, 9, 15, 12, 0),
                LocalDateTime.of(2021, 9, 13, 12, 0),
                false
        );

        StepVerifier
                .create(service.makeReservation(reservation))
                .expectErrorMatches(ex -> ex instanceof ReservationTimeConstraint)
                .verify();
    }
}