package com.tiarebalbi

import com.tiarebalbi.support.run
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@SpringBootApplication
open class TechBoardApplication

fun main(args: Array<String>) {
    run(TechBoardApplication::class, *args)
}

@Configuration
@ConfigurationProperties(prefix = "tech.board")
open class TechBoardApplicationProperties {

    var domain: Domain = Domain()

    class Domain {
        var domainName: String = ""
        var verifySpecificDomain: Boolean = false
    }
}


