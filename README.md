# campsite-reservations-api
REST API service that will manage the campsite reservations

## Dev setup
* Requirements
  * Maven
  * Java 11
  * Docker

1. Launch a local postgres instance (todo: try to setup a docker compose)
   1. `docker run --name some-postgres -p 5432:5432 -e POSTGRES_PASSWORD=mysecretpassword -d postgres`
2. Once the db is up, we can run the application
   1. From the code
      1. `mvn clean package`
      2. `java -jar target/campsite-reservations-api-0.0.1-SNAPSHOT.jar`

## API Documentation
OpenAPI definition can be found at [ http://localhost:8080/swagger-ui.html]( http://localhost:8080/swagger-ui.html) while running the api

* `POST /reservation`
  * Create a reservation
* `PUT /reservation/{id}`
  * Modify a reservation by {id}
* `GET /reservation/{id}`
  * Get a reservation by {id}
* `GET /reservation?from={LocalDateTime}&to={LocalDateTime}`
  * Get all reservations in [from, to] range 
    * where {LocalDateTime} formatted like: "2021-09-19T12:00:00"
* `DELETE /reservation/{id}`
    * Cancel/Delete a reservation by {id}

## Load/Concurrency tests
* Run with a load test with gatling
  * `mvn gatling:test`
* find the results under
  * `/target/gatling/basicsimulation-{timestamp}/index.html`
* Basic in place test with
  * `rampUsers(3000).during(20.seconds)`

## Architecture Decision Record
* Use Spring Webflux to keep a lower thread count/resource footprint in case we need to scale
  * Because we don't have any other service dependency down the chain except the DB
  * This will need to be measure in a later performance benchmark with [Gatling](https://gatling.io/)
* ~~H2 for rapid prototyping in local as well as support for R2DBC~~
  * Switched to a local postgres docker container to use more elaborate type, [Range](https://www.postgresql.org/docs/13/rangetypes.html)
* Use Database constraint to prevent double booking
  * We can deal with the exception at the application level
* Since spring-data-r2dbc does not yet permit automatic creation of entity/table, we will use Flyway to handle that automatically