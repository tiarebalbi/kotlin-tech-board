package com.tiarebalbi.repository

import com.tiarebalbi.AbstractIntegrationTests
import com.tiarebalbi.model.Card
import com.tiarebalbi.test.NeedsCleanUp
import com.tiarebalbi.test.NeedsTestData
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import reactor.test.StepVerifier

class CardRepositoryTest : AbstractIntegrationTests() {

  @Autowired
  lateinit var repository: CardRepository

  @Test
  @NeedsCleanUp
  fun `Should Save a new Card`() {
    val result = this.repository.save(Card(id = "1", name = "My new card", description = "Description", columnId = "column1"))

    StepVerifier.create(result)
      .consumeNextWith {
        assertThat(it.name).isEqualTo("My new card")
        assertThat(it.description).isEqualTo("Description")
        assertThat(it.columnId).isEqualTo("column1")
        assertThat(it.id).isNotBlank().isEqualTo("1")
        assertThat(it.version).isEqualTo(0)
        assertThat(it.createdDate).isNotNull()
      }
      .verifyComplete()
  }

  @Test
  @NeedsTestData(collection = "card", value = "mock/cards.json")
  fun `Should delete a card by ID`() {
    val result = this.repository.deleteCardById("999332a2-6114-41c8-b98e-62cd62ff6602")

    StepVerifier.create(result)
      .consumeNextWith {
        assertThat(it.deletedCount).isEqualTo(1)
      }
  }

  @Test
  @NeedsTestData(collection = "card", value = "mock/cards.json")
  fun `Should Find All by the ColumnID`() {
    val result = this.repository.findByColumnTopic("column1")

    StepVerifier.create(result)
      .expectNextCount(2)
      .verifyComplete()
  }
}