package computerdatabase

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class BasicSimulation extends Simulation {

  private val baseUrl = "https://52.78.155.6:8081" //"http://localhost:8063"
  private val connection= "keep-alive"
  private val contentType = "application/json"
  private val requestCount = 10
  private val noOfUsers = 10


  val httpProtocol = http
    .baseUrl(baseUrl) // Here is the root for all relative URLs
    .acceptHeader("application/json")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")

  val dummyPost = scenario("dummypost con 8000") // A scenario is a chain of requests and pauses
    .repeat( times = requestCount)(
      exec(
        http("dummypost")
          .post("/dummyPost")
          .body(StringBody(
            """{
          }""".stripMargin)).asJson
      )
    )

  val postWithResponseAndContent = scenario("postWithResponseAndContent") // A scenario is a chain of requests and pauses
    .repeat( times = requestCount)(
      exec(
        http("postWithResponseAndContent")
          .post("/postWithResponseAndContent")
          .body(StringBody(
            """{
        "username": "abc",
        "email": "abc@gmail.com",
        "password": "123"
          }""".stripMargin)).asJson
      )
    )

  val dummyGet = scenario("dummyGet") // A scenario is a chain of requests and pauses
    .repeat( times = requestCount)(
      exec(
        http("Get")
          .get("/dummyGet")
      )
    )

  val getWithReponse = scenario("getWithReponse") // A scenario is a chain of requests and pauses
    .repeat( times = requestCount)(
      exec(
        http("GetWithReponse")
          .get("/GetWithReponse")
      )
    )

  setUp(

    dummyPost.inject(rampUsers(noOfUsers) during (60 seconds)).protocols(httpProtocol),
    postWithResponseAndContent.inject(nothingFor(60 second), rampUsers(noOfUsers) during (60 seconds)).protocols(httpProtocol),
    dummyGet.inject(nothingFor(160 second), rampUsers(noOfUsers) during (60 seconds)).protocols(httpProtocol),
    getWithReponse.inject(nothingFor(300 second), rampUsers(noOfUsers) during (60 seconds)).protocols(httpProtocol)
  )
}
