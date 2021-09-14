package io.dannyboyer.campsitereservationsapi.reservation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("reservations")
public class ReservationController {
    private final ReservationService service;

    public ReservationController(ReservationService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Reservation> create(@RequestBody Reservation reservation) {
        return service.makeReservation(reservation);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<String> read(@PathVariable String id) {
        return Mono.just(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<String> update(@PathVariable String id) {
        return Mono.just(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<String> delete(@PathVariable String id) {
        return Mono.just(id);
    }
}
