package com.tiarebalbi.api

import com.tiarebalbi.AbstractIntegrationTests
import com.tiarebalbi.model.Card
import com.tiarebalbi.model.DeleteResult
import com.tiarebalbi.test.NeedsCleanUp
import com.tiarebalbi.test.NeedsTestData
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.bodyToFlux
import org.springframework.web.reactive.function.client.bodyToMono
import org.springframework.web.reactive.function.client.exchange
import reactor.core.publisher.toMono
import reactor.test.StepVerifier

class CardApiControllerTest : AbstractIntegrationTests() {

  @Test
  @NeedsCleanUp
  fun `Should save a new card using the API`() {
    val result = this.client
      .post()
      .uri("/api/card")
      .accept(MediaType.APPLICATION_JSON)
      .contentType(MediaType.APPLICATION_JSON)
      .exchange(Card(id = "1", name = "Card 1", description = "Description", columnId = "column2").toMono())
      .then { r -> r.bodyToMono<Card>() }

    StepVerifier.create(result)
      .consumeNextWith {
        assertThat(it.version).isEqualTo(0)
        assertThat(it.id).isEqualTo("1")
        assertThat(it.name).isEqualTo("Card 1")
        assertThat(it.description).isEqualTo("Description")
        assertThat(it.columnId).isEqualTo("column2")
        assertThat(it.createdDate).isNotNull()
      }
      .verifyComplete()
  }

  @Test
  @NeedsTestData(collection = "card", value = "mock/cards.json")
  fun `Should delete a card by ID using the API`() {
    val result = this.client
      .delete()
      .uri("/api/card/999332a2-6114-41c8-b98e-62cd62ff6602")
      .accept(MediaType.APPLICATION_JSON)
      .exchange()
      .then { r -> r.bodyToMono<DeleteResult>() }

    StepVerifier.create(result)
      .consumeNextWith {
        assertThat(it.deletedCount).isEqualTo(1)
      }
      .verifyComplete()
  }

  @Test
  @NeedsTestData(collection = "card", value = "mock/cards.json")
  fun `Should find all card by ID using the API`() {
    val result = this.client
      .get()
      .uri("/api/card/column1")
      .accept(MediaType.APPLICATION_JSON)
      .contentType(MediaType.APPLICATION_JSON)
      .exchange()
      .flatMap { r -> r.bodyToFlux<Card>() }

    StepVerifier.create(result)
      .expectNextCount(2)
      .verifyComplete()
  }
}