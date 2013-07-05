import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "queers"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    "joda-time" % "joda-time" % "2.2"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
