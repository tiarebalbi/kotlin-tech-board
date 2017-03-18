package com.tiarebalbi.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import java.time.LocalDateTime

data class Card(
  @Id val id: String,
  val name: String,
  val description: String,
  val columnId: String,
  val color: Color = Color.GREEN,
  @CreatedDate var createdDate: LocalDateTime? = null,
  @Version var version: Int? = null
)