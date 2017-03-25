package com.tiarebalbi.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.CompoundIndexes
import org.springframework.data.mongodb.core.mapping.Document


@CompoundIndexes(
  CompoundIndex(name = "topic_user", def = "{'topicSlug' : 1, 'userId': 1}"),
  CompoundIndex(name = "user", def = "{'userId': 1}"),
  CompoundIndex(name = "topic", def = "{'topicSlug' : 1}")
)
@Document
data class TopicFollower(@Id var id: String? = null, val topicSlug: String, val userId: String)

