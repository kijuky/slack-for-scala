lazy val root = project
  .in(file("."))
  .settings(
    name := "slack-for-scala",
    scalaVersion := "2.12.20",
    crossScalaVersions := Seq(scalaVersion.value, "3.3.4"),
    libraryDependencies ++= Seq("com.slack.api" % "slack-api-client" % "1.40.3")
  )

inThisBuild(
  Seq(
    organization := "io.github.kijuky",
    homepage := Some(url("https://github.com/kijuky/slack-for-scala")),
    licenses := Seq(
      "Apache-2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0")
    ),
    developers := List(
      Developer(
        "kijuky",
        "Kizuki YASUE",
        "ikuzik@gmail.com",
        url("https://github.com/kijuky")
      )
    ),
    versionScheme := Some("early-semver"),
    sonatypeCredentialHost := "s01.oss.sonatype.org",
    sonatypeRepository := "https://s01.oss.sonatype.org/service/local"
  )
)
