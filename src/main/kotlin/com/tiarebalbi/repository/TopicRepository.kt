package com.tiarebalbi.repository

import com.tiarebalbi.model.Topic
import com.tiarebalbi.support.findOne
import com.tiarebalbi.support.remove
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
open class TopicRepository(val template: ReactiveMongoTemplate) {

  fun save(topic: Topic): Mono<Topic> = template.save(topic)

  fun findBySlug(slug: String): Mono<Topic> = template.findOne(query(where("slug").`is`(slug)))

  fun count(): Mono<Long> = template.count(Query(), Topic::class.java)

  fun findAll(): Flux<Topic> = template.findAll(Topic::class.java)

  fun deleteAll() = template.remove<Topic>(Query())

  fun deleteBySlug(slug: String?) = template.remove<Topic>(query(where("slug").`is`(slug)))
}