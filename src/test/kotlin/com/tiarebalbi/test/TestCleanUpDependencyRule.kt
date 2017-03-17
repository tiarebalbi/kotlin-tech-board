package com.tiarebalbi.test

import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class TestCleanUpDependencyRule : TestRule {

  var needsCleanUp: NeedsCleanUp? = null

  override fun apply(base: Statement, description: Description): Statement {
    needsCleanUp = description.getAnnotation(NeedsCleanUp::class.java)
    return base
  }

  fun needsCleanUp(): Boolean = needsCleanUp != null
}
