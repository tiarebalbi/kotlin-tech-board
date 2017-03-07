package com.tiarebalbi.support

import com.mongodb.ConnectionString
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.SimpleReactiveMongoDatabaseFactory
import org.springframework.web.reactive.function.server.HandlerStrategies

@Configuration
open class MongoConfiguration {

  @Bean
  open fun setupMongoTemplate(env: Environment): ReactiveMongoTemplate =
    ReactiveMongoTemplate(SimpleReactiveMongoDatabaseFactory(ConnectionString(env.getProperty("mongo.uri"))))

  @Bean
  open fun dataInitializer() = ApplicationRunner {
    HandlerStrategies.withDefaults()
  }
}
