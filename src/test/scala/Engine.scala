import io.gatling.app.Gatling
import io.gatling.core.config.GatlingPropertiesBuilder

/**
 * TAKEN FROM https://github.com/gatling/gatling-maven-plugin-demo
 */
object Engine extends App {

  val props = new GatlingPropertiesBuilder()
    .resourcesDirectory(IDEPathHelper.mavenResourcesDirectory.toString)
    .resultsDirectory(IDEPathHelper.resultsDirectory.toString)
    .binariesDirectory(IDEPathHelper.mavenBinariesDirectory.toString)

  Gatling.fromMap(props.build)
}