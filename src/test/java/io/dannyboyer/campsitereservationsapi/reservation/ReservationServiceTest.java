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

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository repository;

    @InjectMocks
    private ReservationService service;

    @Test
    void makeReservation_success() {
        var arrivalDate = LocalDateTime.now().plusDays(2);
        var departureDate = arrivalDate.plusDays(1);
        var reservation = new Reservation(1L,
                "dan@boy.com",
                "dan",
                "boy",
                arrivalDate,
                departureDate
        );

        ReservationEntity entity = ReservationEntity.builder()
                .id(1L)
                .email(reservation.getEmail())
                .firstName(reservation.getFirstName())
                .lastName(reservation.getLastName())
                .arrivalDate(reservation.getArrivalDate())
                .departureDate(reservation.getDepartureDate())
                .build();

        Mockito.when(repository.save(Mockito.any(ReservationEntity.class))).thenReturn(Mono.just(entity));

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
                LocalDateTime.of(2021, 9, 16, 12, 0)
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
                LocalDateTime.of(2021, 9, 13, 12, 0)
        );

        StepVerifier
                .create(service.makeReservation(reservation))
                .expectErrorMatches(ex -> ex instanceof ReservationTimeConstraint)
                .verify();
    }

    @Test
    void makeReservation_moreThanOneMonthBefore() {
        var from = LocalDateTime.now().plusMonths(1).plusDays(1);
        var to = from.plusDays(1);
        var reservation = new Reservation(null,
                "dan@boy.com",
                "dan",
                "boy",
                from,
                to
        );

        StepVerifier
                .create(service.makeReservation(reservation))
                .expectErrorMatches(ex -> ex instanceof ReservationTimeConstraint)
                .verify();
    }

    @Test
    void makeReservation_lessThanOneDayBefore() {
        var from = LocalDateTime.now();
        var to = from.plusDays(1);
        var reservation = new Reservation(null,
                "dan@boy.com",
                "dan",
                "boy",
                from,
                to
        );

        StepVerifier
                .create(service.makeReservation(reservation))
                .expectErrorMatches(ex -> ex instanceof ReservationTimeConstraint)
                .verify();
    }
}