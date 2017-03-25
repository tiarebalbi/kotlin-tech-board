package com.tiarebalbi.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
data class Card(
  @Id var id: String? = null,
  var name: String,
  var description: String,
  var columnId: String,
  var color: Color? = Color.GREEN,
  @CreatedDate var createdDate: LocalDateTime? = null,
  @Version var version: Int? = null
)