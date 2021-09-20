package io.dannyboyer.campsitereservationsapi.reservation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(summary = "Book a reservation")
    public Mono<Reservation> create(@Valid @RequestBody Reservation reservation) {
        return service.makeReservation(reservation);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Find all reservations in date range")
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
    @Operation(summary = "Find a reservation by Id")
    public Mono<Reservation> read(@PathVariable Long id) {
        return service.getReservationById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update a reservation by Id")
    public Mono<Reservation> update(@PathVariable Long id,
                                    @Valid @RequestBody Reservation reservation) {
        return service.updateReservation(id, reservation);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Cancel a reservation by deleting it by Id")
    public Mono<Void> delete(@PathVariable Long id) {
        return service.cancelReservation(id);
    }
}
