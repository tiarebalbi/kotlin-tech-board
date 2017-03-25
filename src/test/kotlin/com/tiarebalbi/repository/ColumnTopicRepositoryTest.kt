package com.tiarebalbi.repository

import com.tiarebalbi.AbstractIntegrationTests
import com.tiarebalbi.model.Color
import com.tiarebalbi.model.ColumnTopic
import com.tiarebalbi.model.Topic
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import reactor.test.StepVerifier

class ColumnTopicRepositoryTest : AbstractIntegrationTests() {

  @Autowired
  lateinit var topicRepository: TopicRepository

  @Autowired
  lateinit var columnRepository: ColumnTopicRepository

  @Before
  fun setUp() {
    this.topicRepository.save(Topic(name = "New Column Topic")).block()
  }

  @After
  fun tearDown() {
    this.topicRepository.deleteAll().block()
    this.columnRepository.deleteAll().block()
  }

  @Test
  fun `Should save a new column`() {

    val column = ColumnTopic("1", "My Column's Name", "new-column-topic")

    val result = this.columnRepository.save(column)

    StepVerifier.create(result)
      .consumeNextWith {
        assertThat(it.name).isEqualTo("My Column's Name")
        assertThat(it.topicSlug).isEqualTo("new-column-topic")
        assertThat(it.version).isEqualTo(0)
        assertThat(it.color).isEqualTo(Color.BLUE)
      }
      .verifyComplete()
  }

  @Test
  fun `Should find by topic slug`() {
    val column = ColumnTopic("1", "My Column's Name", "new-column-topic")
    this.columnRepository.save(column).block()

    val result = this.columnRepository.findByTopicSlug("new-column-topic")

    StepVerifier.create(result)
      .consumeNextWith {
        assertThat(it.name).isEqualTo("My Column's Name")
        assertThat(it.topicSlug).isEqualTo("new-column-topic")
        assertThat(it.version).isEqualTo(0)
        assertThat(it.color).isEqualTo(Color.BLUE)
      }
      .verifyComplete()
  }

  @Test
  fun `Should delete by column ID`() {
    val column = ColumnTopic("1", "My Column's Name", "new-column-topic")
    this.columnRepository.save(column).block()

    val result = this.columnRepository.deleteById("1")
    StepVerifier.create(result)
      .consumeNextWith {
        assertThat(it.deletedCount).isEqualTo(1)
      }

  }

  @Test
  fun `Should delete all column from the collection`() {
    this.columnRepository.save(ColumnTopic("1", "My Column 1", "new-column-topic")).block()
    this.columnRepository.save(ColumnTopic("2", "My Column 2", "new-column-topic")).block()

    val result = this.columnRepository.deleteAll()
    StepVerifier.create(result)
      .consumeNextWith {
        assertThat(it.deletedCount).isEqualTo(2)
      }
  }
}