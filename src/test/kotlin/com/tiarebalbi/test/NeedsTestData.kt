package com.tiarebalbi.test

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class NeedsTestData(val value: String, val collection: String)
