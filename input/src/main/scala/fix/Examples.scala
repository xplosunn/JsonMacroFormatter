/*
rule = fix.JsonMacroFormatter
 */
package fix

import io.circe.literal._

object Examples {
  val str = s"""{"key":"value"}"""

  val jsonNum = json"""1"""

  val jsonStr = json""""str""""

  val jsonEmptyArray = json"""[]"""

  val jsonEmptyObj = json"""{}"""

  val obj = json"""{"key":"value"}"""

  val interpolatedObj =
    json"""{"key" : ${obj}}"""

  val interpolatedObj2 =
    json"""{"key": $obj, "key2": $interpolatedObj}"""

  val inception =
    json"""{"key3": ${json"""{"key2": ${obj} }"""} }"""

  val key1 = "a"
  val key2 = "b"
  val dynamicKeys =
    json"""{${key1}:"x", ${key2}:"y"}"""

  val dynamicVals =
    json"""{"x":${key1}, "y":${key2}}"""
}
