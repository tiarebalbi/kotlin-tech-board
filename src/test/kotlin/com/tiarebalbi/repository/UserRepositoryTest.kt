package com.tiarebalbi.repository

import com.tiarebalbi.AbstractIntegrationTests
import com.tiarebalbi.model.User
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired


class UserRepositoryTest : AbstractIntegrationTests() {

    @Autowired
    lateinit var repository: UserRepository

    @Before
    fun setUp() {
        this.repository.deleteAll()
    }

    @Test
    fun `Save a new user in the database`() {

        val user = User("me@tiarebalbi.com", "Tiare")
        this.repository.save(user)
                .doOnError { fail("Error to save the user") }
                .doOnSubscribe { assertThat(user.email).isEqualTo("me@tiarebalbi.com") }
                .subscribe()

    }

    @Test
    fun `Should find the user by his email`() {
        this.repository.save(User("me@tiarebalbi.com", "Tiare"))
                .doOnSuccess {
                    val search = this.repository.findOne("me@tiarebalbi.com")
                    val result = search.subscribe().peek()
                    assertThat(result).isNotNull()
                    assertThat(result.email).isEqualTo("me@tiarebalbi.com")
                }
    }
}