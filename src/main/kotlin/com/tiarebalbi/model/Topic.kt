package com.tiarebalbi.model

import com.tiarebalbi.support.toSlug
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class Topic(
  name: String,
  var description: String? = "",
  var color: Color = Color.GREY,
  @Id var slug: String = name.toSlug()) {
  var name: String = name
    set(value) {
      field = value
      slug = name.toSlug()
    }
}