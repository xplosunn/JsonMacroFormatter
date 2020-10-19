lazy val V = _root_.scalafix.sbt.BuildInfo
inThisBuild(
  List(
    version := "0.0.2",
    scalaVersion := V.scala212,
    crossScalaVersions := List(V.scala213, V.scala212),
    organization := "com.github.xplosunn",
    licenses := List("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
    homepage := Some(url("https://github.com/xplosunn/JsonMacroFormatter")),
    developers := List(
      Developer(
        "xplosunn",
        "Hugo Sousa",
        "gi.ciberon@gmail.com",
        url("https://github.com/xplosunn")
      )
    ),
    addCompilerPlugin(scalafixSemanticdb),
    scalacOptions ++= List(
      "-Yrangepos",
      "-P:semanticdb:synthetics:on"
    ),
    scmInfo := Some(
      ScmInfo(
        url("https://github.com/xplosunn/JsonMacroFormatter"),
        "scm:git@github.com:xplosunn/JsonMacroFormatter.git"
      )
    ),
    pomIncludeRepository := { _ => false },
    publishTo := Some("releases" at "https://oss.sonatype.org/service/local/staging/deploy/maven2"),
    publishMavenStyle := true,
    credentials += Credentials(Path.userHome / ".sbt" / "sonatype_credentials")
  )
)

skip in publish := true

lazy val rules = project.settings(
  moduleName := "JsonMacroFormatter",
  libraryDependencies ++= Seq(
    "ch.epfl.scala" %% "scalafix-core" % V.scalafixVersion,
    "io.circe" %% "circe-core" % "0.12.3",
    "io.circe" %% "circe-parser" % "0.12.3",
  ),
)

lazy val input = project.settings(
  skip in publish := true,
  libraryDependencies ++= Seq(
    "io.circe" %% "circe-core" % "0.12.3",
    "io.circe" %% "circe-literal" % "0.12.3",
    "org.typelevel" %% "jawn-parser" % "0.14.0",
  )
)

lazy val output = project.settings(
  skip in publish := true,
  libraryDependencies ++= Seq(
    "io.circe" %% "circe-core" % "0.12.3",
    "io.circe" %% "circe-literal" % "0.12.3",
    "org.typelevel" %% "jawn-parser" % "0.14.0",
  )
)

lazy val tests = project
  .settings(
    skip in publish := true,
    libraryDependencies += "ch.epfl.scala" % "scalafix-testkit" % V.scalafixVersion % Test cross CrossVersion.full,
    compile.in(Compile) := 
      compile.in(Compile).dependsOn(compile.in(input, Compile)).value,
    scalafixTestkitOutputSourceDirectories :=
      sourceDirectories.in(output, Compile).value,
    scalafixTestkitInputSourceDirectories :=
      sourceDirectories.in(input, Compile).value,
    scalafixTestkitInputClasspath :=
      fullClasspath.in(input, Compile).value,
  )
  .dependsOn(rules)
  .enablePlugins(ScalafixTestkitPlugin)
