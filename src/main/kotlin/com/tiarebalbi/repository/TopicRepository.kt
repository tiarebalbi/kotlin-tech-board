package com.tiarebalbi.repository

import com.mongodb.client.result.DeleteResult
import com.tiarebalbi.model.Topic
import com.tiarebalbi.support.findAll
import com.tiarebalbi.support.findOne
import com.tiarebalbi.support.remove
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration

@Repository
open class TopicRepository(val template: ReactiveMongoTemplate) {

  @Autowired
  lateinit var topicFollowerRepository: TopicFollowerRepository

  fun save(topic: Mono<Topic>): Mono<Topic> = template.save(topic)

  fun save(topic: Topic): Mono<Topic> = template.save(topic)

  fun findBySlug(slug: String): Mono<Topic> = template.findOne(query(where("slug").`is`(slug)))

  fun count(): Mono<Long> = template.count(Query(), Topic::class.java)

  fun findAll(pageable: Pageable = PageRequest.of(0, 25)): Flux<Topic> = template.findAll(pageable)

  fun deleteAll() = template.remove<Topic>(Query())

  fun deleteBySlug(slug: String): Flux<DeleteResult> {
    val removeTopic = template.remove<Topic>(query(where("slug").`is`(slug)))
    val removeFollowers = this.topicFollowerRepository.removeFollowerFromTopic(slug)

    return Flux.concat(removeFollowers, removeTopic)
  }

  fun findTopicsByUser(userId: String): Flux<Topic> {
    return this.topicFollowerRepository.findTopicsByUser(userId)
      .map {
        this.findBySlug(it.topicSlug).block(Duration.ofSeconds(5))
      }
  }

}