package com.tiarebalbi.api

import com.tiarebalbi.AbstractIntegrationTests
import com.tiarebalbi.model.Color
import com.tiarebalbi.model.ColumnTopic
import com.tiarebalbi.model.DeleteResult
import com.tiarebalbi.model.Topic
import com.tiarebalbi.repository.ColumnTopicRepository
import com.tiarebalbi.repository.TopicRepository
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Assert
import org.junit.Before
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
  lateinit var topicRepository: TopicRepository

  @Autowired
  lateinit var columnRepository: ColumnTopicRepository

  @After
  fun setUp() {
    this.topicRepository.deleteAll()
    this.columnRepository.deleteAll()
  }

  @Test
  fun `Should access information about one topic`() {
    this.topicRepository.save(Topic("This is a cool topic")).doOnSuccess {

      val topic = client
        .get()
        .uri("/api/topic/this-is-a-cool-topic")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .then { r -> r.bodyToMono<Topic>() }

      StepVerifier.create(topic)
        .consumeNextWith {
          Assert.assertEquals("this-is-a-cool-topic", it.slug)
          Assert.assertEquals("This is a cool topic", it.name)
        }
        .verifyComplete()

    }
  }

  @Test
  fun `Should access all topics in the database`() {
    this.topicRepository.save(Topic("New Topic"))
      .concatWith(this.topicRepository.save(Topic("Custom Topic")))
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
    this.topicRepository.save(Topic("New Topic")).block()

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

  @Test
  fun `Should create a new topic using the API`() {
    val user = client
      .post()
      .uri("/api/topic")
      .accept(MediaType.APPLICATION_JSON)
      .contentType(MediaType.APPLICATION_JSON)
      .exchange(Topic("Data Topic").toMono())
      .flatMap { it.bodyToMono<Topic>() }

    StepVerifier.create(user)
      .consumeNextWith {
        assertThat(it.slug).isEqualTo("data-topic")
        assertThat(it.name).isEqualTo("Data Topic")
        assertThat(it.color).isEqualTo(Color.GREY)
        assertThat(it.version).isEqualTo(0)
      }
      .verifyComplete()
  }

  @Test
  fun `Should find all column from a topic`() {
    val topic = this.topicRepository.save(Topic("New Topic")).block()
    this.columnRepository.save(ColumnTopic("1", "Column 1", topic.slug)).block()
    this.columnRepository.save(ColumnTopic("2", "Column 2", topic.slug)).block()

    val columns = client
      .get()
      .uri("/api/topic/new-topic/columns")
      .accept(MediaType.APPLICATION_JSON)
      .exchange()
      .flatMap { it.bodyToFlux<ColumnTopic>() }

    StepVerifier.create(columns)
      .consumeNextWith {
        assertThat(it.topicSlug).isEqualTo("new-topic")
        assertThat(it.name).isEqualTo("Column 1")
        assertThat(it.color).isEqualTo(Color.BLUE)
        assertThat(it.version).isEqualTo(0)
      }
      .consumeNextWith {
        assertThat(it.topicSlug).isEqualTo("new-topic")
        assertThat(it.name).isEqualTo("Column 2")
        assertThat(it.color).isEqualTo(Color.BLUE)
        assertThat(it.version).isEqualTo(0)
      }
      .verifyComplete()

  }

  @Test
  fun `Should save a new column in a topic`() {
    val topic = this.topicRepository.save(Topic("New Topic")).block()

    val column = ColumnTopic("1", "Column Name", topic.slug)

    val columns = client
      .post()
      .uri("/api/topic/new-topic/columns")
      .accept(MediaType.APPLICATION_JSON)
      .contentType(MediaType.APPLICATION_JSON)
      .exchange(column.toMono())
      .then { r -> r.bodyToMono<ColumnTopic>() }

    StepVerifier.create(columns)
      .consumeNextWith {
        assertThat(it.topicSlug).isEqualTo("data-topic")
        assertThat(it.name).isEqualTo("Column Name")
        assertThat(it.color).isEqualTo(Color.BLUE)
        assertThat(it.version).isEqualTo(0)
      }
      .expectComplete()
  }

  @Test
  fun `Should delete a column from a topic`() {
    val topic = this.topicRepository.save(Topic("New Topic")).block()
    this.columnRepository.save(ColumnTopic("1", "Column 1", topic.slug)).block()

    val columns = client
      .delete()
      .uri("/api/topic/new-topic/columns/1")
      .accept(MediaType.APPLICATION_JSON)
      .exchange()
      .then { r -> r.bodyToMono<DeleteResult>() }

    StepVerifier.create(columns)
      .consumeNextWith {
        Assertions.assertThat(it.deletedCount).isEqualTo(1)
      }
      .verifyComplete()

  }
}