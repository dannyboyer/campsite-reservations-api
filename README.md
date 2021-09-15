# campsite-reservations-api
REST API service that will manage the campsite reservations

## DOC
OpenAPI definition can be found at [ http://localhost:8080/swagger-ui.html]( http://localhost:8080/swagger-ui.html) while running the api

## Architecture Decision Record
* Use Webflux to keep a lower thread count/memory footprint in case we need to scale
  * Because we don't have any other service dependency down the chain except the DB
* H2 for rapid prototyping in local as well as support for R2DBC