package io.dannyboyer.campsitereservationsapi.reservation;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;


interface ReservationRepository extends ReactiveCrudRepository<Reservation, Long> {
}
