resolvers ++= Resolver.sonatypeOssRepos("releases")
addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.14.3")
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.5.4")
addSbtPlugin("com.jsuereth" % "sbt-pgp" % "2.0.1")
