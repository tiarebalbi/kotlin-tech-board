package com.tiarebalbi.repository

import com.tiarebalbi.AbstractIntegrationTests
import com.tiarebalbi.model.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.query.Query
import reactor.test.StepVerifier


class UserRepositoryTest : AbstractIntegrationTests() {

  @Autowired
  lateinit var repository: UserRepository

  @Before
  fun setUp() {
    this.repository.deleteAll()
  }

  @Test
  fun `Save a new user in the database`() {
    // given
    val data = getUser()

    // when
    val user = this.repository.save(data)

    // then
    StepVerifier.create(user)
      .consumeNextWith {
        assertThat(it.email).isEqualTo("me@tiarebalbi.com")
        assertThat(it.firstName).isEqualTo("Tiare")
      }
      .verifyComplete()
  }

  @Test
  fun `Should find the user by his email`() {
    // given
    this.repository.save(getUser())

    // when
    val user = this.repository.findOne("me@tiarebalbi.com")

    // then
    StepVerifier.create(user)
      .consumeNextWith {
        assertThat(it).isNotNull()
        assertThat(it.email).isEqualTo("me@tiarebalbi.com")
      }
      .verifyComplete()
  }

  @Test
  fun `Should delete all user when necessary`() {
    // given
    this.repository.save(getUser())

    // when
    this.repository.deleteAll()

    // then
    StepVerifier.create(this.repository.count(Query()))
      .expectNextCount(0)
      .verifyComplete()
  }

  fun `Should find all records in the database`() {
    // given
    this.repository.save(getUser())

    // when
    val data = this.repository.findAll()

    // then
    StepVerifier.create(data)
      .consumeRecordedWith {
        assertThat(it).isNotNull()
        assertThat(it.size).isEqualTo(1)
      }
      .verifyComplete()
  }

  private fun getUser() = User("me@tiarebalbi.com", "Tiare")
}