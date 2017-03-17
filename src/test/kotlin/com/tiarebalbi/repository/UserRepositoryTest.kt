package com.tiarebalbi.repository

import com.tiarebalbi.AbstractIntegrationTests
import com.tiarebalbi.model.Role
import com.tiarebalbi.model.User
import com.tiarebalbi.test.NeedsCleanUp
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
  @NeedsCleanUp
  fun setUp() {
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
        assertThat(it.lastName).isEqualTo("Balbi")
        assertThat(it.role).isEqualTo(Role.USER)
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
        assertThat(it.firstName).isEqualTo("Tiare")
        assertThat(it.lastName).isEqualTo("Balbi")
        assertThat(it.role).isEqualTo(Role.USER)
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

  @Test
  fun `Should find all records in the database`() {
    this.repository.save(getUser()).doOnSuccess {
      val data = this.repository.findAll()

      StepVerifier.create(data)
        .consumeRecordedWith {
          assertThat(it).isNotNull()
          assertThat(it.size).isEqualTo(1)
        }
        .verifyComplete()
    }
  }

  private fun getUser() = User("me@tiarebalbi.com", "Tiare", "Balbi")
}