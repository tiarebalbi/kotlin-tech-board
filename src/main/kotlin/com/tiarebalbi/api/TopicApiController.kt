package com.tiarebalbi.api

import com.tiarebalbi.model.Topic
import com.tiarebalbi.repository.TopicRepository
import com.tiarebalbi.support.RouterFunctionProvider
import com.tiarebalbi.support.json
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.reactive.function.server.Routes
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse.created
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.bodyToMono
import toMono
import java.net.URI

@Controller
class TopicApiController(val repository: TopicRepository) : RouterFunctionProvider() {

  override val routes: Routes = {
    accept(MediaType.APPLICATION_JSON).route {
      "/api/topic".route {
        GET("/", this@TopicApiController::findAll)
        GET("/{slug}", this@TopicApiController::findBySlug)
        DELETE("/{slug}", this@TopicApiController::deleteBySlug)
        POST("/", this@TopicApiController::save)
      }
    }
  }

  fun findAll(req: ServerRequest) = ok().json().body(this.repository.findAll())

  fun findBySlug(req: ServerRequest) = ok().json().body(this.repository.findBySlug(req.pathVariable("slug")))

  fun deleteBySlug(req: ServerRequest) = ok().json().body(this.repository.deleteBySlug(req.pathVariable("slug")))

  fun save(req: ServerRequest) = this.repository.save(req.bodyToMono<Topic>()).then { topic ->
    created(URI.create("/api/topic/${topic.slug}")).json().body(topic.toMono())
  }
}