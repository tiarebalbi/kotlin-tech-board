package com.tiarebalbi

import com.tiarebalbi.support.run
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class TechBoardApplication

fun main(args: Array<String>) {
  run(TechBoardApplication::class, *args)
}

