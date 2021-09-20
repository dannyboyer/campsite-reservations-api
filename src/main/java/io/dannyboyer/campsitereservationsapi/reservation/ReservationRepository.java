package io.dannyboyer.campsitereservationsapi.reservation;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

interface ReservationRepository extends ReactiveCrudRepository<ReservationEntity, Long> {
    @Query("SELECT * FROM reservation WHERE :from < departure_date AND :to > arrival_date ORDER BY arrival_date ASC")
    Flux<ReservationEntity> findAllByTimeRange(LocalDateTime from, LocalDateTime to);
}
