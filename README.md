# JsonMacroFormatter

Scalafix rule to format json contained within [Circe](https://github.com/circe/circe) json literals. 

E.g.

Before formatting:

```
val obj = json"""{"key":"value"}"""

val interpolatedObj =
  json"""{"key" : ${ obj }}"""
```

After formatting:

```
val obj = json"""{
            "key" : "value"
          }"""

val interpolatedObj =
  json"""{
    "key" : ${obj}
  }"""
```
