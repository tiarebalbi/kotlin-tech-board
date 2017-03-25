package com.tiarebalbi.repository

import com.tiarebalbi.AbstractIntegrationTests
import com.tiarebalbi.model.Topic
import com.tiarebalbi.test.NeedsCleanUp
import com.tiarebalbi.test.NeedsMultipleTestData
import com.tiarebalbi.test.NeedsTestData
import org.assertj.core.api.Assertions.assertThat
import org.junit.Ignore
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import reactor.test.StepVerifier
import java.time.Duration

class TopicRepositoryTest : AbstractIntegrationTests() {

  @Autowired
  lateinit var repository: TopicRepository

  @Test
  @NeedsCleanUp
  fun `Should save a new topic in the database`() {
    // given
    val topic = Topic(id = "1", name = "New Topic")

    // when
    val result = this.repository.save(topic)

    // then
    StepVerifier.create(result)
      .consumeNextWith {
        assertThat(it.slug).isEqualTo("new-topic")
      }
      .verifyComplete()
  }

  @Test
  @Ignore
  @NeedsTestData(collection = "topic", value = "mock/topics.json")
  fun `Should update an existent topic`() {

    val topic = this.repository.findBySlug("my-new-topic").block(Duration.ofSeconds(5))

    topic.name = "New name"
    val update = this.repository.save(topic)

    StepVerifier.create(update)
      .consumeNextWith {
        assertThat(it).isNotNull()
        assertThat(it.name).isEqualTo("New name")
        assertThat(it.slug).isEqualTo("new-name")
        assertThat(it.version).isEqualTo(1)
        assertThat(it.id).isEqualTo(topic.id)
      }
      .verifyComplete()

    val count = this.repository.count()
    StepVerifier.create(count)
      .expectNextCount(1)
  }

  @Test
  @NeedsTestData(collection = "topic", value = "mock/topics.json")
  fun `Should find all topics`() {
    val result = this.repository.findAll()

    StepVerifier.create(result)
      .consumeNextWith {
        assertThat(it).isNotNull()
        assertThat(it.version).isNotNull()
        assertThat(it.id).isNotBlank()
        assertThat(it.name).isEqualTo("My New Topic")
        assertThat(it.slug).isEqualTo("my-new-topic")
      }
      .verifyComplete()
  }

  @Test
  @NeedsTestData(collection = "topic", value = "mock/topics.json")
  fun `Should be able to find by slug`() {
    val flow = this.repository.findBySlug("my-new-topic")

    StepVerifier.create(flow)
      .consumeNextWith {
        assertThat(it).isNotNull()
        assertThat(it.version).isNotNull()
        assertThat(it.id).isNotBlank()
        assertThat(it.name).isEqualTo("My New Topic")
        assertThat(it.slug).isEqualTo("my-new-topic")
      }
      .verifyComplete()
  }

  @Test
  @NeedsMultipleTestData(
    NeedsTestData(collection = "topic", value = "mock/topics.json"),
    NeedsTestData(collection = "topicFollower", value = "mock/topicFollowers.json")
  )
  fun `Should remove topic and followers`() {
    val result = this.repository.deleteBySlug("my-new-topic")

    StepVerifier.create(result)
      .consumeNextWith {
        assertThat(it.deletedCount)
          .isEqualTo(3)
      }
      .consumeNextWith {
        assertThat(it.deletedCount)
          .isEqualTo(1)
      }
      .verifyComplete()
  }

  @Test
  @Ignore
  @NeedsMultipleTestData(
    NeedsTestData(collection = "topic", value = "mock/topics.json"),
    NeedsTestData(collection = "topicFollower", value = "mock/topicFollowers.json")
  )
  fun `Should get all users topic`() {
    val result = this.repository.findTopicsByUser("2")

    StepVerifier.create(result)
      .consumeNextWith {
        assertThat(it).isNotNull()
        assertThat(it.name).isEqualTo("My New Topic")
        assertThat(it.slug).isEqualTo("my-new-topic")
      }
      .verifyComplete()

  }

}