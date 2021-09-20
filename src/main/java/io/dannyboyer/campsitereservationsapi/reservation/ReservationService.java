package io.dannyboyer.campsitereservationsapi.reservation;

import io.dannyboyer.campsitereservationsapi.problem.ReservationExceedLimit;
import io.dannyboyer.campsitereservationsapi.problem.ReservationNotFoundException;
import io.dannyboyer.campsitereservationsapi.problem.ReservationDatabaseIntegrity;
import io.dannyboyer.campsitereservationsapi.problem.ReservationTimeConstraint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class ReservationService {
    /**
     * Max number of days is 3, but we need to take into consideration
     * that we arrive and leave halfway into the day
     * Therefore, it's really 2 days
     */
    public static final int MAX_NUMBER_OF_DAYS = 2;
    private final ReservationRepository repository;

    public ReservationService(ReservationRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Mono<Reservation> makeReservation(Reservation reservation) {
        var from = reservation.getArrivalDate();
        var to = reservation.getDepartureDate();

        if (from.isAfter(to)) {
            return Mono.error(new ReservationTimeConstraint("arrivalDate cannot be after departureDate"));
        }

        if (Duration.between(from, to).toDays() > MAX_NUMBER_OF_DAYS) {
            return Mono.error(new ReservationExceedLimit());
        }

        if (reservation.getArrivalDate().isAfter(LocalDateTime.now().plusMonths(1))) {
            return Mono.error(new ReservationTimeConstraint("arrivalDate can be up to 1 month in advance"));
        }

        if (reservation.getArrivalDate().isBefore(LocalDateTime.now().plusDays(1))) {
            return Mono.error(new ReservationTimeConstraint("reservation can be minimum 1 day(s) ahead of arrival"));
        }

        return repository
                .save(toEntity(reservation))
                .map(this::toDto)
                .onErrorResume(e -> Mono.error(new ReservationDatabaseIntegrity(e)));
    }

    public Mono<Reservation> getReservationById(Long id) {
        return repository
                .findById(id)
                .switchIfEmpty(notFound(id))
                .map(this::toDto);
    }

    @Transactional
    public Mono<Reservation> updateReservation(Long id, Reservation updatedReservation) {
        return repository
                .findById(id)
                .switchIfEmpty(notFound(id))
                .doOnNext(r -> {
                    // took the liberty of restraining what can be updated
                    r.setArrivalDate(updatedReservation.getArrivalDate());
                    r.setDepartureDate(updatedReservation.getDepartureDate());
                })
                .flatMap(repository::save)
                .map(this::toDto);
    }

    public Flux<Reservation> findAllInRange(Optional<LocalDateTime> from, Optional<LocalDateTime> to) {
        var defaultFrom = LocalDateTime.now().plusDays(1);
        var defaultTo = defaultFrom.plusMonths(1);
        return repository.findAllByTimeRange(from.orElseGet(() -> defaultFrom), to.orElseGet(() -> defaultTo)).map(this::toDto);
    }

    public Mono<Void> cancelReservation(Long id) {
        return repository.deleteById(id);
    }

    private Mono<ReservationEntity> notFound(Long id) {
        return Mono.error(new ReservationNotFoundException(id));
    }

    private ReservationEntity toEntity(Reservation reservation) {
        return ReservationEntity.builder()
                .email(reservation.getEmail())
                .firstName(reservation.getFirstName())
                .lastName(reservation.getLastName())
                .arrivalDate(reservation.getArrivalDate())
                .departureDate(reservation.getDepartureDate())
                .build();
    }

    private Reservation toDto(ReservationEntity entity) {
        return Reservation.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .arrivalDate(entity.getArrivalDate())
                .departureDate(entity.getDepartureDate())
                .build();
    }
}
