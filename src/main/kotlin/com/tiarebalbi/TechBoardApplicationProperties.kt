package com.tiarebalbi

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "tech.board")
open class TechBoardApplicationProperties {

  var domain: Domain = Domain()

  class Domain {
    var domainName: String = ""
    var verifySpecificDomain: Boolean = false
  }
}
