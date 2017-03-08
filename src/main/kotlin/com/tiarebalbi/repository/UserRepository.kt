package com.tiarebalbi.repository

import com.tiarebalbi.model.Topic
import com.tiarebalbi.model.User
import com.tiarebalbi.support.findAll
import com.tiarebalbi.support.findById
import com.tiarebalbi.support.remove
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
open class UserRepository(val template: ReactiveMongoTemplate) {

  fun findAll(): Flux<User> = template.findAll()

  fun findOne(id: String): Mono<User> = template.findById(id)

  fun deleteAll() = template.remove<Topic>(Query())

  fun save(user: User) = template.save(user)

  fun save(user: Mono<User>) = template.save(user)

  fun count(query: Query) = template.count(query, User::class.java)
}