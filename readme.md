# JsonMacroFormatter

[Scalafix](https://github.com/scalacenter/scalafix) rule to format json contained within [Circe](https://github.com/circe/circe) json literals.

E.g.

Before formatting:

```scala
val obj = json"""{"key":"value"}"""

val interpolatedObj =
  json"""{"key" : ${ obj }}"""
```

After formatting:

```scala
val obj = json"""{
            "key" : "value"
          }"""

val interpolatedObj =
  json"""{
    "key" : ${obj}
  }"""
```

## Usage

Add to `build.sbt`:

`ThisBuild / scalafixDependencies += "com.github.xplosunn" %% "JsonMacroFormatter" % "0.0.8"`

Add to `.scalafix.conf`:

`fix.JsoMacroFormatter`
