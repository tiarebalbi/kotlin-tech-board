package com.tiarebalbi.repository

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.repository.init.Jackson2ResourceReader
import org.springframework.data.repository.init.ResourceReader
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class TestDataPopulator {

  @Autowired
  lateinit var objectMapper: ObjectMapper

  @Autowired
  lateinit var mongoTemplate: MongoTemplate

  lateinit var jackson2ResourceReader: Jackson2ResourceReader

  @PostConstruct
  fun setup() {
    this.jackson2ResourceReader = Jackson2ResourceReader(this.objectMapper)
  }

  fun populateDatabaseUsingJsonResourcesFromClasspath(classpathResource: String, collectionName: String) {
    this.populateDatabaseUsingJsonResources(ClassPathResource(classpathResource), collectionName)
  }

  fun populateDatabaseUsingJsonResources(resources: Resource, collectionName: String) {
    this.populateDatabaseUsingResources(this.jackson2ResourceReader, resources, collectionName)
  }

  private fun populateDatabaseUsingResources(resourceReader: ResourceReader, resources: Resource, collectionName: String) {
    val result = resourceReader.readFrom(resources, null)

    if (result is Collection<*>) {
      result.forEach {
        saveResource(collectionName, it)
      }
    } else {
      saveResource(collectionName, result)
    }
  }

  private fun saveResource(collectionName: String, data: Any?) {
    this.mongoTemplate.save(data, collectionName)
  }

  fun cleanDatabase() {
    this.mongoTemplate.collectionNames
      .filterNot { it.startsWith("system") }
      .forEach { this.mongoTemplate.dropCollection(it) }
  }
}