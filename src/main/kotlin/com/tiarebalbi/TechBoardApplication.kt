package com.tiarebalbi

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class TechBoardApplication

fun main(args: Array<String>) {
    SpringApplication.run(TechBoardApplication::class.java, *args)
}
