package com.tiarebalbi.repository

import com.tiarebalbi.AbstractIntegrationTests
import com.tiarebalbi.model.Topic
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class TopicRepositoryTest : AbstractIntegrationTests() {

  @Autowired
  lateinit var repository: TopicRepository

  @Before
  fun setUp() {
    this.repository.deleteAll()
  }

  @Test
  fun `Should save a new topic in the database`() {
    // given
    val topic = getTopic()

    // when
    val result = this.repository.save(topic)

    // then
    StepVerifier.create(result)
      .consumeNextWith {
        assertThat(it.slug).isEqualTo("new-topic")
      }
      .expectComplete()
  }

  @Test
  fun `Should update an existent topic`() {
    val topic = this.repository.save(getTopic())

    val topicChanged = getTopic()
    topicChanged.name = "New name"
    val update = this.repository.save(topicChanged)


    val changes = Mono.from(topic).concatWith(update)

    StepVerifier.create(changes)
      .consumeNextWith {
        assertThat(it).isNotNull()
        assertThat(it.name).isEqualTo("New Topic")
        assertThat(it.slug).isEqualTo("new-topic")
      }
      .consumeNextWith {
        assertThat(it).isNotNull()
        assertThat(it.name).isEqualTo("New name")
        assertThat(it.slug).isEqualTo("new-name")
      }
      .expectComplete()

    val count = this.repository.count()
    StepVerifier.create(count)
      .expectNextCount(1)
  }

  @Test
  fun `Should find all topics`() {
    val topic1 = this.repository.save(getTopic())
    val topic2 = this.repository.save(getTopic("New Topic"))

    val flow = Mono.from(topic1)
      .concatWith(topic2)
      .concatWith(this.repository.findAll())

    StepVerifier.create(flow)
      .expectNextCount(2)
      .consumeNextWith {
        assertThat(it).isNotNull()
        assertThat(it.name).isEqualTo("New Topic")
        assertThat(it.slug).isEqualTo("new-topic")
      }
      .consumeNextWith {
        assertThat(it).isNotNull()
        assertThat(it.name).isEqualTo("New Topic")
        assertThat(it.slug).isEqualTo("new-topic")
      }
      .expectComplete()
  }

  @Test
  fun `Should be able to find by slug`() {
    val user1 = this.repository.save(getTopic("Custom Topic"))
    val flow = user1.concatWith(this.repository.findBySlug("custom-topic"))

    StepVerifier.create(flow)
      .expectNextCount(2)
      .consumeNextWith {
        assertThat(it).isNotNull()
        assertThat(it.name).isEqualTo("Custom Topic")
        assertThat(it.slug).isEqualTo("custom-topic")
      }
      .consumeNextWith {
        assertThat(it).isNotNull()
        assertThat(it.name).isEqualTo("Custom Topic")
        assertThat(it.slug).isEqualTo("custom-topic")
      }
      .expectComplete()
  }

  private fun getTopic(name: String = "New Topic") = Topic(name)
}