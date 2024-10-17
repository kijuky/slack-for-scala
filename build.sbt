val slackDependencies = Seq("com.slack.api" % "slack-api-client" % "1.43.1")

lazy val root = project
  .in(file("."))
  .aggregate(vanilla, zio)
  .settings(publish / skip := true)

lazy val vanilla = project
  .in(file("vanilla"))
  .settings(
    name := "slack-for-scala",
    scalaVersion := "2.12.20", // scala-steward:off
    crossScalaVersions := Seq(scalaVersion.value, "3.3.4"),
    libraryDependencies ++= slackDependencies
  )

lazy val zio = project
  .in(file("zio"))
  .settings(
    name := "slack-for-zio",
    scalaVersion := "3.3.4",
    libraryDependencies ++= slackDependencies ++
      Seq("dev.zio" %% "zio" % "2.1.6")
>>>>>>> afe4aa8 (add zio support)
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
