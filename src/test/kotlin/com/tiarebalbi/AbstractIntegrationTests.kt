package com.tiarebalbi

import org.junit.runner.RunWith
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.web.reactive.function.client.ExchangeFunctions
import org.springframework.web.reactive.function.client.WebClient

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class AbstractIntegrationTests {

  @LocalServerPort
  var port: Int = 0

  val client = WebClient.builder()
    .exchangeFunction(ExchangeFunctions.create(ReactorClientHttpConnector()))
    .build()

}