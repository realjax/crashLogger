import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "crashReport"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "org.reactivemongo" %% "play2-reactivemongo" % "0.10.2"
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
