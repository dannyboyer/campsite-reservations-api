package io.dannyboyer.campsitereservationsapi.reservation;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService service;

    public ReservationController(ReservationService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Reservation> create(@Valid @RequestBody Reservation reservation) {
        return service.makeReservation(reservation);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<Reservation> findAllInRange(@RequestParam("from")
                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                    Optional<LocalDateTime> from,
                                            @RequestParam("to")
                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                    Optional<LocalDateTime> to) {
        return service.findAllInRange(from, to);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Reservation> read(@PathVariable Long id) {
        return service.getReservationById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Reservation> update(@PathVariable Long id,
                                    @Valid @RequestBody Reservation reservation) {
        return service.updateReservation(id, reservation);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Void> delete(@PathVariable Long id) {
        return service.cancelReservation(id);
    }
}
