import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName = "tasker"
  val appVersion = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaEbean,
    "org.springframework" % "spring-jdbc" % "3.1.2.RELEASE",
    "mysql" % "mysql-connector-java" % "5.1.22",
    "com.intellij" % "annotations" % "12.0"
  )


  val commonUtil = play.Project("common-util", dependencies = appDependencies, path = file("common")).settings(
  )

  val aMain = play.Project(appName, appVersion, appDependencies, path = file("main")).dependsOn(commonUtil).settings(
  )


}
