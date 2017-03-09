import org.gradle.testing.jacoco.tasks.JacocoReport
import org.jetbrains.kotlin.noarg.gradle.NoArgExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.SpringBootPluginExtension

buildscript {
  val kotlinVersion = "1.1.0"
  val dependencyManagement = "1.0.0.RELEASE"
  val springBootVersion = "2.0.0.BUILD-SNAPSHOT"
  extra["kotlinVersion"] = kotlinVersion
  extra["springBootVersion"] = springBootVersion

  repositories {
    jcenter()
    mavenCentral()
    maven { setUrl("https://plugins.gradle.org/m2/") }
    maven { setUrl("http://dl.bintray.com/kotlin/kotlin-eap-1.1") }
    maven { setUrl("https://repo.spring.io/snapshot") }
    maven { setUrl("https://repo.spring.io/milestone") }
  }

  dependencies {
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    classpath("org.springframework.boot:spring-boot-gradle-plugin:$springBootVersion")
    classpath("org.jetbrains.kotlin:kotlin-noarg:$kotlinVersion")
    classpath("org.jetbrains.kotlin:kotlin-allopen:$kotlinVersion")
    classpath("io.spring.gradle:dependency-management-plugin:$dependencyManagement")
    classpath("org.jfrog.buildinfo:build-info-extractor-gradle:latest.release")
  }
}

apply {
  plugin("idea")
  plugin("kotlin")
  plugin("kotlin-noarg")
  plugin("kotlin-spring")
  plugin("org.springframework.boot")
  plugin("io.spring.dependency-management")
  plugin("jacoco")
  plugin("com.jfrog.artifactory")
}

version = "1.0.0-SNAPSHOT"

repositories {
  mavenCentral()
  maven { setUrl("https://dl.bintray.com/jetbrains/spek") }
  maven { setUrl("http://dl.bintray.com/kotlin/kotlin-eap-1.1") }
  maven { setUrl("https://repo.spring.io/milestone") }
  maven { setUrl("https://repo.spring.io/snapshot") }
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    jvmTarget = "1.8"
  }
}

configure<NoArgExtension> {
  annotation("org.springframework.data.mongodb.core.mapping.Document")
}

configure<JavaPluginConvention> {
  setSourceCompatibility(1.8)
  setTargetCompatibility(1.8)
}

(getTasksByName("jacocoTestReport", false).first() as JacocoReport).apply {
  reports {
    it.xml.isEnabled = true
  }
}

val kotlinVersion = extra["kotlinVersion"] as String
val springVersion = "5.0.0.BUILD-SNAPSHOT"
val springBootVersion = extra["springBootVersion"] as String
val springDataVersion = "2.0.0.BUILD-SNAPSHOT"
val jacksonVersion = "2.8.5"
val reactorVersion = "3.0.5.RELEASE"

dependencies {
  compile("org.jetbrains.kotlin:kotlin-stdlib-jre8:$kotlinVersion")
  compile("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
  compile("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
  compile("org.springframework.boot:spring-boot-starter-actuator")
  compile("org.springframework.boot:spring-boot-starter-webflux") {
    exclude(module = "hibernate-validator")
  }
  compile("io.projectreactor:reactor-kotlin:1.0.0.BUILD-SNAPSHOT")
  compile("com.atlassian.commonmark:commonmark:0.8.0")
  compile("io.projectreactor:reactor-core:$reactorVersion")
  compile("io.projectreactor.ipc:reactor-netty:0.6.0.RELEASE")
  compile("com.fasterxml.jackson.module:jackson-module-kotlin")
  compile("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
  compile("commons-logging:commons-logging:1.2")

  compileOnly("org.springframework.boot:spring-boot-configuration-processor")

  testCompile("org.springframework.boot:spring-boot-starter-test")
  testCompile("io.projectreactor.addons:reactor-test:$reactorVersion")
}

tasks.getByName("processResources")