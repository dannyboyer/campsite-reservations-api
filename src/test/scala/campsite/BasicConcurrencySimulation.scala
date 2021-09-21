package campsite

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import java.nio.file.Paths

class BasicConcurrencySimulation extends Simulation {
  val httpProtocol = http
    .baseUrl("http://localhost:8080") // Here is the root for all relative URLs
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // Here are the common headers
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  val scn = scenario("Try to book the same time range at once") // A scenario is a chain of requests and pauses
    .exec(
      http("Book Resservation")
        .post("/reservations")
        .body(RawFileBody(Paths.get(getClass.getClassLoader.getResource("reservation.json").toURI).toString))
        .asJson
    )

  setUp(scn.inject(atOnceUsers(30)).protocols(httpProtocol))
}