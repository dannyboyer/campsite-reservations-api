package io.dannyboyer.campsitereservationsapi.reservation;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

interface ReservationRepository extends ReactiveCrudRepository<ReservationEntity, Long> {
    @Query("SELECT * FROM reservation WHERE arrival_date >= :from AND departure_date <= :to")
    Flux<ReservationEntity> findAllByTimeRange(LocalDateTime from, LocalDateTime to);
}
