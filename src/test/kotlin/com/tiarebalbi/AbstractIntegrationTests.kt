package com.tiarebalbi

import com.tiarebalbi.repository.TestDataPopulator
import com.tiarebalbi.test.TestCleanUpDependencyRule
import com.tiarebalbi.test.TestDataDependencyRule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.web.reactive.function.client.WebClient

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class AbstractIntegrationTests {

  @LocalServerPort
  var port: Int? = null

  lateinit var client: WebClient

  @Rule @JvmField
  public val testDataDependency = TestDataDependencyRule()

  @Rule @JvmField
  public val testCleanUpDependencyRule = TestCleanUpDependencyRule()

  @Autowired
  lateinit var testDataPopulator: TestDataPopulator

  @Before
  fun setup() {
    client = WebClient.create("http://localhost:$port")
  }

  @Before
  fun setupTestData() {
    if (testDataDependency.needsTestData()) {
      this.testDataPopulator.populateDatabaseUsingJsonResourcesFromClasspath(testDataDependency.data()!!, testDataDependency.collection()!!)
    }
  }

  @After
  fun cleanUpTestData() {
    if (testDataDependency.needsTestData() || testCleanUpDependencyRule.needsCleanUp()) {
      this.testDataPopulator.cleanDatabase()
    }
  }

}