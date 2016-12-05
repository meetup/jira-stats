enablePlugins(CommonSettingsPlugin)
enablePlugins(DockerPackagePlugin)
enablePlugins(CoverallsWrapperPro)

name := "blt-best-sbt-docker"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.meetup" %% "jira" % "0.1.108",
  "org.slf4j" % "slf4j-nop" % "1.7.5"
)