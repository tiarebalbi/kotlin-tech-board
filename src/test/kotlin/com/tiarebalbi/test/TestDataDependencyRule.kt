package com.tiarebalbi.test

import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class TestDataDependencyRule : TestRule {

  var needsTestData: NeedsTestData? = null

  override fun apply(base: Statement, description: Description): Statement {
    needsTestData = description.getAnnotation(NeedsTestData::class.java)
    return base
  }

  fun needsTestData() = needsTestData != null

  fun data() = needsTestData?.value

  fun collection() = needsTestData?.collection
}
