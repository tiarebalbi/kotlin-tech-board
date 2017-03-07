package com.tiarebalbi.api

import com.tiarebalbi.AbstractIntegrationTests
import com.tiarebalbi.model.Color
import com.tiarebalbi.model.DeleteResult
import com.tiarebalbi.model.Topic
import com.tiarebalbi.repository.TopicRepository
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Assert
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.bodyToFlux
import org.springframework.web.reactive.function.client.bodyToMono
import org.springframework.web.reactive.function.client.exchange
import reactor.test.StepVerifier
import toMono


class TopicApiControllerTest : AbstractIntegrationTests() {

  @Autowired
  lateinit var repository: TopicRepository

  @After
  fun tearDown() {
    this.repository.deleteAll()
  }

  @Test
  fun `Should access information about one topic`() {
    this.repository.save(Topic("New Topic"))
      .doOnSuccess {
        val topic = client
          .get()
          .uri("/api/topic/new-topic")
          .accept(MediaType.APPLICATION_JSON)
          .exchange()
          .then { r -> r.bodyToMono<Topic>() }

        StepVerifier.create(topic)
          .consumeNextWith {
            Assert.assertEquals("new-topic", it.slug)
            Assert.assertEquals("New Topic", it.name)
          }
          .verifyComplete()
      }
  }

  @Test
  fun `Should access all topics in the database`() {
    this.repository.save(Topic("New Topic"))
      .concatWith(this.repository.save(Topic("Custom Topic")))
      .doOnComplete {
        val topics = client
          .get()
          .uri("/api/topic")
          .accept(MediaType.APPLICATION_JSON).exchange()
          .flatMap { it.bodyToFlux<Topic>() }

        StepVerifier.create(topics)
          .expectNextCount(2)
          .verifyComplete()
      }

  }

  @Test
  fun `Should send a request to remove a topic`() {
    this.repository.save(Topic("New Topic"))
      .doOnSuccess {
        val topics = client
          .delete()
          .uri("/api/topic/new-topic")
          .accept(MediaType.APPLICATION_JSON)
          .exchange()
          .then { r -> r.bodyToMono<DeleteResult>() }

        StepVerifier.create(topics)
          .consumeNextWith {
            Assertions.assertThat(it.deletedCount).isEqualTo(1)
          }
          .verifyComplete()
      }
  }

  @Test
  fun `Should create a new topic using the API`() {
    val user = client
      .post()
      .uri("/api/topic")
      .accept(MediaType.APPLICATION_JSON)
      .contentType(MediaType.APPLICATION_JSON)
      .exchange(Topic("Topic").toMono())
      .flatMap { it.bodyToMono<Topic>() }

    StepVerifier.create(user)
      .consumeNextWith {
        assertThat(it.slug).isEqualTo("topic")
        assertThat(it.name).isEqualTo("Topic")
        assertThat(it.color).isEqualTo(Color.GREY)
      }
      .verifyComplete()
  }
}