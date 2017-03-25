package com.tiarebalbi.repository

import com.mongodb.client.result.DeleteResult
import com.tiarebalbi.model.Card
import com.tiarebalbi.support.find
import com.tiarebalbi.support.remove
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class CardRepository(val template: ReactiveMongoTemplate) {

  fun save(card: Card): Mono<Card> = template.save(card)

  fun findByColumnTopic(column: String): Flux<Card> = template.find(query(where("columnId").`is`(column)))

  fun deleteCardById(id: String): Mono<DeleteResult> = template.remove<Card>(query(where("id").`is`(id)))
}