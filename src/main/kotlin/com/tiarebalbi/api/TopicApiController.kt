package com.tiarebalbi.api

import com.tiarebalbi.model.ColumnTopic
import com.tiarebalbi.model.Topic
import com.tiarebalbi.repository.ColumnTopicRepository
import com.tiarebalbi.repository.TopicRepository
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/topic")
class TopicApiController(val topicRepository: TopicRepository, val columnRepository: ColumnTopicRepository) {

  @GetMapping
  fun findAll() = this.topicRepository.findAll()

  @GetMapping("/{slug}")
  fun findBySlug(@PathVariable slug: String) = this.topicRepository.findBySlug(slug)

  @PostMapping
  fun saveTopic(@RequestBody topic: Topic) = this.topicRepository.save(topic)

  @DeleteMapping("/{slug}")
  fun deleteBySlug(@PathVariable slug: String) = this.topicRepository.deleteBySlug(slug)

  @GetMapping("/{slug}/columns")
  fun findColumnsByTopicSlug(@PathVariable slug: String) = this.columnRepository.findByTopicSlug(slug)

  @PostMapping("/{slug}/columns")
  fun saveColumnTopic(@RequestBody columnTopic: ColumnTopic) = this.columnRepository.save(columnTopic)

  @DeleteMapping("/{slug}/columns/{id}")
  fun deleteColumnById(@PathVariable id: String) = this.columnRepository.deleteById(id)

}

