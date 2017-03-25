package com.tiarebalbi.api

import com.tiarebalbi.AbstractIntegrationTests
import com.tiarebalbi.model.Color
import com.tiarebalbi.model.ColumnTopic
import com.tiarebalbi.model.DeleteResult
import com.tiarebalbi.model.Topic
import com.tiarebalbi.repository.ColumnTopicRepository
import com.tiarebalbi.test.NeedsCleanUp
import com.tiarebalbi.test.NeedsMultipleTestData
import com.tiarebalbi.test.NeedsTestData
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.bodyToFlux
import org.springframework.web.reactive.function.client.bodyToMono
import org.springframework.web.reactive.function.client.exchange
import reactor.core.publisher.toMono
import reactor.test.StepVerifier


class TopicApiControllerTest : AbstractIntegrationTests() {

  @Autowired
  lateinit var columnRepository: ColumnTopicRepository

  @Test
  @NeedsTestData(value = "mock/topics.json", collection = "topic")
  fun `Should access information about one topic`() {
    val topic = client
      .get()
      .uri("/api/topic/my-new-topic")
      .accept(MediaType.APPLICATION_JSON)
      .exchange()
      .then { r -> r.bodyToMono<Topic>() }

    StepVerifier.create(topic)
      .consumeNextWith {
        Assert.assertEquals("my-new-topic", it.slug)
        Assert.assertEquals("My New Topic", it.name)
        Assert.assertEquals(Color.BLUE, it.color)
      }
      .verifyComplete()
  }

  @Test
  @NeedsTestData(value = "mock/topics.json", collection = "topic")
  fun `Should access all topics in the database`() {
    val topics = client
      .get()
      .uri("/api/topic")
      .accept(MediaType.APPLICATION_JSON).exchange()
      .flatMap { it.bodyToFlux<Topic>() }

    StepVerifier.create(topics)
      .consumeNextWith {
        Assert.assertEquals("my-new-topic", it.slug)
        Assert.assertEquals("My New Topic", it.name)
        Assert.assertEquals(Color.BLUE, it.color)
      }
      .verifyComplete()
  }

  @Test
  @NeedsTestData(value = "mock/topics.json", collection = "topic")
  fun `Should send a request to remove a topic`() {
    val topics = client
      .delete()
      .uri("/api/topic/my-new-topic")
      .accept(MediaType.APPLICATION_JSON)
      .contentType(MediaType.APPLICATION_JSON)
      .exchange()
      .flatMap { r -> r.bodyToFlux<DeleteResult>() }

    StepVerifier.create(topics)
      .consumeNextWith {
        Assertions.assertThat(it.deletedCount).isEqualTo(0)
      }
      .consumeNextWith {
        Assertions.assertThat(it.deletedCount).isEqualTo(1)
      }
      .verifyComplete()

  }

  @Test
  @NeedsCleanUp
  fun `Should create a new topic using the API`() {
    val user = client
      .post()
      .uri("/api/topic")
      .accept(MediaType.APPLICATION_JSON)
      .contentType(MediaType.APPLICATION_JSON)
      .exchange(Topic(name = "Data Topic").toMono())
      .flatMap { it.bodyToMono<Topic>() }

    StepVerifier.create(user)
      .consumeNextWith {
        Assert.assertEquals("data-topic", it.slug)
        Assert.assertEquals("Data Topic", it.name)
        Assert.assertEquals(Color.GREY, it.color)
        assertThat(it.version).isEqualTo(0)
      }
      .verifyComplete()
  }

  @Test
  @NeedsTestData(value = "mock/topics.json", collection = "topic")
  fun `Should find all column from a topic`() {
    val columns = client
      .get()
      .uri("/api/topic/my-new-topic/columns")
      .accept(MediaType.APPLICATION_JSON)
      .exchange()
      .flatMap { it.bodyToFlux<ColumnTopic>() }

    StepVerifier.create(columns)
      .consumeNextWith {
        assertThat(it.topicSlug).isEqualTo("my-new-topic")
        assertThat(it.name).isEqualTo("Column 1")
        assertThat(it.color).isEqualTo(Color.BLUE)
        assertThat(it.version).isEqualTo(0)
      }
      .consumeNextWith {
        assertThat(it.topicSlug).isEqualTo("my-new-topic")
        assertThat(it.name).isEqualTo("Column 2")
        assertThat(it.color).isEqualTo(Color.BLUE)
        assertThat(it.version).isEqualTo(0)
      }
      .verifyComplete()
  }

  @Test
  @NeedsTestData(value = "mock/topics.json", collection = "topic")
  fun `Should save a new column in a topic`() {
    val column = ColumnTopic("1", "Column Name", "new-topic")

    val columns = client
      .post()
      .uri("/api/topic/new-topic/columns")
      .accept(MediaType.APPLICATION_JSON)
      .contentType(MediaType.APPLICATION_JSON)
      .exchange(column.toMono())
      .then { r -> r.bodyToMono<ColumnTopic>() }

    StepVerifier.create(columns)
      .consumeNextWith {
        assertThat(it.topicSlug).isEqualTo("new-topic")
        assertThat(it.name).isEqualTo("Column Name")
        assertThat(it.color).isEqualTo(Color.BLUE)
        assertThat(it.version).isEqualTo(0)
      }
      .verifyComplete()
  }

  @Test
  @NeedsTestData(value = "mock/topics.json", collection = "topic")
  fun `Should delete a column from a topic`() {
    this.columnRepository.save(ColumnTopic("1", "Column 1", "new-topic")).doOnSubscribe {

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

  @Test
  @NeedsMultipleTestData(
    NeedsTestData(collection = "topic", value = "mock/topics.json"),
    NeedsTestData(collection = "topicFollower", value = "mock/topicFollowers.json")
  )
  fun `Should find topics by the user id`() {
    val topics = client
      .get()
      .uri("/api/topic/user/1")
      .accept(MediaType.APPLICATION_JSON).exchange()
      .flatMap { it.bodyToFlux<Topic>() }

    StepVerifier.create(topics)
      .consumeNextWith {
        assertThat(it.slug).isEqualTo("my-new-topic")
        assertThat(it.name).isEqualTo("My New Topic")
        assertThat(it.color).isEqualTo(Color.BLUE)
        assertThat(it.version).isEqualTo(0)
      }
      .verifyComplete()

  }
}