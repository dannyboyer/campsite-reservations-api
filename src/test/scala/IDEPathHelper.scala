import java.nio.file.Paths

/**
 * TAKEN FROM https://github.com/gatling/gatling-maven-plugin-demo
 */
object IDEPathHelper {

  private val projectRootDir = Paths.get(getClass.getClassLoader.getResource("gatling.conf").toURI).getParent.getParent.getParent
  private val mavenTargetDirectory = projectRootDir.resolve("target")
  private val mavenSrcTestDirectory = projectRootDir.resolve("src").resolve("test")

  val mavenSourcesDirectory = mavenSrcTestDirectory.resolve("scala")
  val mavenResourcesDirectory = mavenSrcTestDirectory.resolve("resources")
  val mavenBinariesDirectory = mavenTargetDirectory.resolve("test-classes")
  val resultsDirectory = mavenTargetDirectory.resolve("gatling")
  val recorderConfigFile = mavenResourcesDirectory.resolve("recorder.conf")
}