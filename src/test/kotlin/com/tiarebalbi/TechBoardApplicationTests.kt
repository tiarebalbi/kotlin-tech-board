package com.tiarebalbi

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class TechBoardApplicationTests {

    @Autowired
    lateinit var properties: TechBoardApplicationProperties

    @Test
    fun contextLoads() {
        assertThat(this.properties.domain)
                .isNotNull()

    }

}