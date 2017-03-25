package com.tiarebalbi.test

import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class MultipleTestDataDependencyRule : TestRule {

  var needsMultipleTestData: NeedsMultipleTestData? = null

  override fun apply(base: Statement, description: Description): Statement {
    needsMultipleTestData = description.getAnnotation(NeedsMultipleTestData::class.java)
    return base
  }

  fun needsData(): Boolean = needsMultipleTestData != null

  fun collections() = needsMultipleTestData?.data
}
