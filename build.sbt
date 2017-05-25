enablePlugins(CommonSettingsPlugin)
enablePlugins(DockerPackagePlugin)
enablePlugins(CoverallsWrapperPro)

name := "jira-stats"

daemonUser in Docker := "root"

defaultLinuxInstallLocation in Docker := "/opt"

dockerEntrypoint := Seq("/opt/entrypoint.sh")

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.meetup" %% "jira" % "0.1.108",
  "org.slf4j" % "slf4j-nop" % "1.7.5"
)
