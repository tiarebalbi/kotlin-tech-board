package com.tiarebalbi.support

import com.mongodb.client.result.DeleteResult
import org.springframework.boot.SpringApplication
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.core.ReactiveMongoOperations
import org.springframework.data.mongodb.core.query.Query
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.text.Normalizer
import java.util.*
import kotlin.reflect.KClass

// ----------------------
// Spring Boot extensions
// ----------------------

fun run(type: KClass<*>, vararg args: String) = SpringApplication.run(type.java, *args)

// ----------------------
// Spring Data extensions
// ----------------------

inline fun <reified T : Any> ReactiveMongoOperations.findById(id: Any): Mono<T> = findById(id, T::class.java)

inline fun <reified T : Any> ReactiveMongoOperations.find(query: Query): Flux<T> = find(query, T::class.java)

inline fun <reified T : Any> ReactiveMongoOperations.findAll(): Flux<T> = findAll(T::class.java)

inline fun <reified T : Any> ReactiveMongoOperations.findAll(pageable: Pageable): Flux<T> {
  val query = Query()
  query.with(pageable)

  return find(query, T::class.java)
}

inline fun <reified T : Any> ReactiveMongoOperations.findOne(query: Query): Mono<T> = find(query, T::class.java).next()

inline fun <reified T : Any> ReactiveMongoOperations.remove(query: Query): Mono<DeleteResult> = remove(query, T::class.java)

// -------------------------
// Spring WebFlux extensions
// -------------------------

fun ServerResponse.BodyBuilder.json() = contentType(MediaType.APPLICATION_JSON_UTF8)

fun ServerResponse.BodyBuilder.xml() = contentType(MediaType.APPLICATION_XML)

fun ServerResponse.BodyBuilder.html() = contentType(MediaType.TEXT_HTML)


// ----------------
// Other extensions
// ----------------

fun String.stripAccents() = Normalizer
  .normalize(this, Normalizer.Form.NFD)
  .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")

fun String.toSlug() =
  toLowerCase()
    .stripAccents()
    .replace("\n", " ")
    .replace("[^a-z\\d\\s]".toRegex(), " ")
    .split(" ")
    .joinToString("-")

fun <T> Iterable<T>.shuffle(): Iterable<T> =
  toMutableList().apply { Collections.shuffle(this) }