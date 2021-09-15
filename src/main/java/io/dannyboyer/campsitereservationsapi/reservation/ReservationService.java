package io.dannyboyer.campsitereservationsapi.reservation;

import io.dannyboyer.campsitereservationsapi.problem.ReservationNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ReservationService {
    private final ReservationRepository repository;

    public ReservationService(ReservationRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Mono<Reservation> makeReservation(Reservation reservation) {
        // The campsite can be reserved for max 3 days.
        // The campsite can be reserved minimum 1 day(s) ahead of arrival and up to 1 month in advance
        return repository.save(reservation);
    }

    public Mono<Reservation> getReservationById(Long id) {
        return repository.findById(id);
    }

    public Mono<Reservation> updateReservation(Long id, Reservation updatedReservation) {
        return repository.findById(id)
                .doOnSuccess(r -> {
                    r.setArrivalDate(updatedReservation.getArrivalDate());
                    r.setDepartureDate(updatedReservation.getDepartureDate());
                    repository.save(r).subscribe();
                });
    }

    public Mono<Void> cancelReservation(Long id) {
        return repository.deleteById(id);
    }
}
