import io.gatling.recorder.GatlingRecorder
import io.gatling.recorder.config.RecorderPropertiesBuilder

/**
 * TAKEN FROM https://github.com/gatling/gatling-maven-plugin-demo
 */
object Recorder extends App {

  val props = new RecorderPropertiesBuilder()
    .simulationsFolder(IDEPathHelper.mavenSourcesDirectory.toString)
    .resourcesFolder(IDEPathHelper.mavenResourcesDirectory.toString)
    .simulationPackage("campsite")

  GatlingRecorder.fromMap(props.build, Some(IDEPathHelper.recorderConfigFile))
}