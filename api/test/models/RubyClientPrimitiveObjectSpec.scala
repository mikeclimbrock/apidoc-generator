package models

import lib.Primitives
import com.gilt.apidocgenerator.models._
import org.scalatest.{ShouldMatchers, FunSpec}

class RubyClientPrimitiveObjectSpec extends FunSpec with ShouldMatchers {

  describe("for a field with an object field") {

    val baseJson = """
    {
      "base_url": "http://localhost:9000",
      "name": "Api Doc Test",

      "models": {
        "content": {
          "fields": [
            { "name": "data", "type": "%s" }
          ]
        }
      }
    }
    """

    def service(typeString: String): Service = {
      TestHelper.service(baseJson.format(typeString))
    }

    def model(typeString: String): Model = {
      service(typeString).models.values.head
    }

    def dataField(typeString: String): Field = {
      model(typeString).fields.head
    }

    it("singleton object") {
      dataField("object").`type` should be("object")
    }

    it("list object") {
      dataField("[object]").`type` should be("[object]")
    }

    it("map object") {
      dataField("map[object]").`type` should be("map[object]")
    }

    describe("generates valid models") {

      it("singleton") {
        val code = RubyClientGenerator(InvocationForm(service("object"))).generateModel("content", model("object"))
        TestHelper.assertEqualsFile("test/resources/generators/ruby-client-primitive-object-singleton.txt", code)
      }

      it("list") {
        val code = RubyClientGenerator(InvocationForm(service("object"))).generateModel("content", model("[object]"))
        TestHelper.assertEqualsFile("test/resources/generators/ruby-client-primitive-object-list.txt", code)
      }

      it("map") {
        val code = RubyClientGenerator(InvocationForm(service("object"))).generateModel("content", model("map[object]"))
        TestHelper.assertEqualsFile("test/resources/generators/ruby-client-primitive-object-map.txt", code)
      }

    }

  }

  describe("for a response with an object field") {

    val baseJson = """
    {
      "base_url": "http://localhost:9000",
      "name": "Api Doc Test",

      "models": {
        "content": {
          "fields": [
            { "name": "id", "type": "long" }
          ]
        }
      },

      "resources": {
        "content": {
          "operations": [
            {
              "method": "GET",
              "path": "/data",
              "responses": {
                "200": { "type": "%s" }
              }
            }
          ]
        }
      }

    }
    """

    def service(typeString: String): Service = {
      TestHelper.service(baseJson.format(typeString))
    }

    def operation(typeString: String): Operation = {
      service(typeString).resources.values.head.operations.head
    }

    def response(typeString: String): Response = {
      operation(typeString).responses.values.head
    }


    it("singleton object") {
      response("object").`type` should be("object")
    }

    it("list object") {
      response("[object]").`type` should be("[object]")
    }

    it("map object") {
      response("map[object]").`type` should be("map[object]")
    }

    describe("generates valid response code") {

      it("singleton") {
        val code = RubyClientGenerator(InvocationForm(service("object"))).generateResponses(operation("object"))
        TestHelper.assertEqualsFile("test/resources/generators/ruby-client-primitive-object-response-singleton.txt", code)
      }

      it("list") {
        val code = RubyClientGenerator(InvocationForm(service("object"))).generateResponses(operation("[object]"))
        TestHelper.assertEqualsFile("test/resources/generators/ruby-client-primitive-object-response-list.txt", code)
      }

      it("map") {
        val code = RubyClientGenerator(InvocationForm(service("object"))).generateResponses(operation("map[object]"))
        TestHelper.assertEqualsFile("test/resources/generators/ruby-client-primitive-object-response-map.txt", code)
      }

    }
  }

}