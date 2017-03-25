package com.tiarebalbi.repository

import com.mongodb.client.result.DeleteResult
import com.tiarebalbi.model.TopicFollower
import com.tiarebalbi.support.find
import com.tiarebalbi.support.remove
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class TopicFollowerRepository(val template: ReactiveMongoTemplate) {

  fun save(follower: TopicFollower): Mono<TopicFollower> = template.save(follower)

  fun findTopicsByUser(userId: String): Flux<TopicFollower> {
    return template.find<TopicFollower>(query(where("userId").`is`(userId)))
  }

  fun findUsersByTopic(topicSlug: String): Flux<TopicFollower> {
    return template.find<TopicFollower>(query(where("topicSlug").`is`(topicSlug)))
  }

  fun removeFollowerFromTopic(topicSlug: String): Mono<DeleteResult> = template.remove<TopicFollower>(query(where("topicSlug").`is`(topicSlug)))

  fun removeTopicFromUser(topicFollower: TopicFollower) = template.remove<TopicFollower>(query(
    where("topicSlug").`is`(topicFollower.topicSlug).and("userId").`is`(topicFollower.userId)
  ))

  fun totalOfFollowers(topicSlug: String): Mono<Long> = template.count(query(where("topicSlug").`is`(topicSlug)), TopicFollower::class.java)
}