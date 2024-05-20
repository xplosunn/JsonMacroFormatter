lazy val V = _root_.scalafix.sbt.BuildInfo

inThisBuild(
  List(
    version := "0.0.7",
    organization := "com.github.xplosunn",
    homepage := Some(url("https://github.com/xplosunn/JsonMacroFormatter")),
    licenses := List("Apache-2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0")),
    scalaVersion := V.scala212,
    crossScalaVersions := List(V.scala212, V.scala213),
    addCompilerPlugin(scalafixSemanticdb),
    scalacOptions ++= List(
      "-Yrangepos",
      "-P:semanticdb:synthetics:on",
      "-deprecation"
    ),
    publish / skip := true,
    scmInfo := Some(
      ScmInfo(
        url("https://github.com/xplosunn/JsonMacroFormatter"),
        "scm:git@github.com:xplosunn/JsonMacroFormatter.git"
      )
    ),
    pomIncludeRepository := { _ => false },
    publishTo := Some("releases" at "https://oss.sonatype.org/service/local/staging/deploy/maven2"),
    publishMavenStyle := true,
    credentials += Credentials(Path.userHome / ".sbt" / "sonatype_credentials"),
    developers := List(
      Developer(
        "xplosunn",
        "Hugo Sousa",
        "gi.ciberon@gmail.com",
        url("https://github.com/xplosunn")
      )
    ),
  )
)

lazy val rules = project.settings(
  moduleName := "JsonMacroFormatter",
  publish / skip := false,
  libraryDependencies ++= Seq(
    "ch.epfl.scala" %% "scalafix-core" % V.scalafixVersion,
    "io.circe" %% "circe-core" % "0.13.0",
    "io.circe" %% "circe-parser" % "0.13.0",
  ),
)

lazy val input = project.settings(
  publish/skip := true,
  libraryDependencies ++= Seq(
    "io.circe" %% "circe-core" % "0.14.7",
    "io.circe" %% "circe-literal" % "0.14.7",
    "org.typelevel" %% "jawn-parser" % "1.5.1",
  )
)

lazy val output = project.settings(
  publish/skip := true,
  libraryDependencies ++= Seq(
    "io.circe" %% "circe-core" % "0.14.7",
    "io.circe" %% "circe-literal" % "0.14.7",
    "org.typelevel" %% "jawn-parser" % "1.5.1",
  )
)

lazy val tests = project
  .settings(
    publish/skip := true,
    libraryDependencies += "ch.epfl.scala" % "scalafix-testkit" % V.scalafixVersion % Test cross CrossVersion.full,
    (Compile / compile) :=
      (Compile/compile).dependsOn(input / Compile / compile).value,
    scalafixTestkitOutputSourceDirectories :=
      (output/ Compile / sourceDirectories).value,
    scalafixTestkitInputSourceDirectories :=
      (input / Compile /sourceDirectories).value,
    scalafixTestkitInputClasspath :=
      (input / Compile/ fullClasspath).value
  )
  .dependsOn(rules)
  .enablePlugins(ScalafixTestkitPlugin)
