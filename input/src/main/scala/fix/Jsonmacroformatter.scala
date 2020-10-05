/*
rule = JsonMacroFormatter
*/
package fix

import io.circe.literal._

object Jsonmacroformatter {
  val str = s"""{"key":"value"}"""

  val obj = json"""{"key":"value"}"""

  val interpolatedObj =
    json"""{"key" : ${ obj }}"""

  val interpolatedObj2 =
    json"""{"key": $obj, "key2": $interpolatedObj}"""

  val inception =
    json"""{"key3": ${ json"""{"key2": ${ obj } }""" } }"""
}
