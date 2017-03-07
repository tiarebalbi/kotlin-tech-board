package com.tiarebalbi.repository

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

  fun findAll(): Flux<User> = template.findAll(User::class)

  fun findOne(id: String) = template.findById(id, User::class)

  fun deleteAll() = template.remove(Query(), User::class)

  fun save(user: User) = template.save(user)

  fun save(user: Mono<User>) = template.save(user)

  fun count(query: Query) = template.count(query, User::class.java)
}