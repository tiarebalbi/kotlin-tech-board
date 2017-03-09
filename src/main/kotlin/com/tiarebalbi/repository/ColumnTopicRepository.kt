package com.tiarebalbi.repository

import com.mongodb.client.result.DeleteResult
import com.tiarebalbi.model.ColumnTopic
import com.tiarebalbi.support.find
import com.tiarebalbi.support.remove
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
open class ColumnTopicRepository(val template: ReactiveMongoTemplate) {

  fun save(topic: ColumnTopic): Mono<ColumnTopic> = template.save(topic)

  fun findByTopicSlug(topicSlug: String): Flux<ColumnTopic> = template.find(query(where("topicSlug").`is`(topicSlug)))

  fun deleteById(id: String) = template.remove<ColumnTopic>(query(where("id").`is`(id)))

  fun deleteAll(): Mono<DeleteResult> = template.remove<ColumnTopic>(Query())

}