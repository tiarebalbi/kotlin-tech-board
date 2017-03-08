package com.tiarebalbi.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class User(
  @Id val email: String,
  val firstName: String,
  val lastName: String? = "",
  val role: Role = Role.USER
)

