package models

import java.nio.file
import java.nio.charset.StandardCharsets

import play.api.libs.json._
import com.gilt.apidocgenerator.models.json._
import com.gilt.apidocgenerator.models.Service
import generator.ScalaService
import lib.Text

object TestHelper {

  lazy val referenceApiService = parseFile(s"../reference-api/api.json")

  def writeToFile(path: String, contents: String) {
    val outputPath = file.Paths.get(path)
    val bytes = contents.getBytes(StandardCharsets.UTF_8)
    file.Files.write(outputPath, bytes)
  }

  def readFile(path: String): String = {
    scala.io.Source.fromFile(path).getLines.mkString("\n")
  }

  def parseFile(filename: String): Service = {
    service(readFile(filename))
  }

  def assertEqualsFile(filename: String, contents: String) {
    if (contents.trim != readFile(filename).trim) {
      val tmpPath = "/tmp/apidoc.tmp." + Text.safeName(filename)
      TestHelper.writeToFile(tmpPath, contents.trim)
      sys.error(s"Test output did not match. diff $tmpPath $filename")
    }
  }

  def service(json: String): Service = {
    Json.parse(json).validate[Service] match {
      case e: JsError => sys.error("Failed to parse json: " + e)
      case s: JsSuccess[Service] => s.get
    }
  }

  def scalaService(json: String): ScalaService = {
    ScalaService(service(json))
  }

}