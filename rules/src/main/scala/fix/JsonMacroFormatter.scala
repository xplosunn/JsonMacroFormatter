package fix

import scalafix.v1._

import java.util.UUID
import scala.meta._

class JsonMacroFormatter extends SemanticRule("fix.JsonMacroFormatter") {

  implicit class StringOps(str: String) {
    def replaceFirstLiterally(literal: String, replacement: String): String = {
      val index = str.indexOf(literal)
      if (index >= 0)
        str.take(index) + replacement + str.drop(index + literal.size)
      else
        str
    }
  }

  def indent(str: String, spaces: Int): String = {
    val spacesStr = (0 until spaces).map(_ => " ").mkString
    str.split('\n').map(spacesStr + _).mkString("\n")
  }

  override def fix(implicit doc: SemanticDocument): Patch = {
    val visitedInterpolations = scala.collection.mutable.ListBuffer.empty[Term.Interpolate]

    def parentInterpolationVisited(t: Tree): Boolean =
      scala.util
        .Try {
          t.parent match {
            case Some(parent) =>
              visitedInterpolations.contains(parent) || parentInterpolationVisited(parent)
            case None => false
          }
        }
        .getOrElse(false)

    doc.tree.collect {
      case t @ Term.Interpolate(Term.Name("json"), lits, terms) if !parentInterpolationVisited(t) =>
        visitedInterpolations += t

        val unlikelyToBeMatchedString =
          "temporary_replacement_string_" + UUID.randomUUID().toString.replace('-', '0') + UUID
            .randomUUID()
            .toString
            .replace('-', '0')

        val jsonWithReplacementsStr =
          lits.map(_.toString()).reduce(_ + s""""$unlikelyToBeMatchedString"""" + _)

        _root_.io.circe.parser.parse(jsonWithReplacementsStr) match {
          case Right(json) =>
            val formatted = indent(json.spaces2, t.pos.startColumn).trim
            val newJson = terms.foldLeft(formatted)((str, term) =>
              str.replaceFirstLiterally(("\"" + unlikelyToBeMatchedString + "\""), s"$${$term}")
            )
            if (newJson.chars().anyMatch(c => c.equals('\n'.toInt) || c.equals('"'.toInt))) {
              Patch.replaceTree(t, "json\"\"\"" + newJson + "\"\"\"")
            } else {
              Patch.replaceTree(t, "json\"" + newJson + "\"")
            }
          case Left(error) =>
            Patch.empty
        }
    }.asPatch
  }

}
