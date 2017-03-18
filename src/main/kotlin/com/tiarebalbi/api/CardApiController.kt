package com.tiarebalbi.api

import com.tiarebalbi.model.Card
import com.tiarebalbi.repository.CardRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/card")
class CardApiController {

  @Autowired
  lateinit var repository: CardRepository

  @PostMapping
  fun save(card: Card) = this.repository.save(card)

  @GetMapping("/{columnId}")
  fun findByColumnId(@PathVariable columnId: String) = this.repository.findByColumnTopic(columnId)


  @DeleteMapping("/{id}")
  fun deleteById(@PathVariable id: String) = this.repository.deleteCardById(id)

}
