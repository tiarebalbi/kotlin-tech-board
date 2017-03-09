package com.tiarebalbi.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class ColumnTopic(
  @Id val id: String,
  val name: String,
  @Indexed val topicSlug: String,
  val color: Color = Color.BLUE,
  @Version var version: Int? = null
)