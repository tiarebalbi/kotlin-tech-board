# kotlin-tech-board
A Technology Board using Kotlin and Spring Boot 2.x with Reactor.

[![Join the chat at https://gitter.im/kotlin-tech-board/Lobby](https://badges.gitter.im/kotlin-tech-board/Lobby.svg)](https://gitter.im/kotlin-tech-board/Lobby?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Build Status](https://travis-ci.org/tiarebalbi/kotlin-tech-board.svg?branch=master)](https://travis-ci.org/tiarebalbi/kotlin-tech-board)

## Software design

This project software design goal is to demonstrate what a functional web application
developed with Spring Framework 5 and Kotlin can look like:
 - Reactive and non-blocking
 - More functional style and less annotation based than typical Spring applications
 - Leverage Kotlin features like [Kotlin extensions](https://kotlinlang.org/docs/reference/extensions.html) and [reified type parameters](https://kotlinlang.org/docs/reference/inline-functions.html#reified-type-parameters) for cleaner code
 - Simple, fast to start, efficient request processing, low memory consumption
 - [Constructor based injection](http://olivergierke.de/2013/11/why-field-injection-is-evil/)
 - Immutable Pojos
 - Cloud Native
 
### Technologies used

- Language: [Kotlin](https://kotlin.link/) 
- Web framework: [Spring Boot](https://projects.spring.io/spring-boot/) and [Spring Web Reactive Functional](https://spring.io/blog/2016/09/22/new-in-spring-5-functional-web-framework)
- Engine: [Netty](http://netty.io/) used for client and server
- Reactive API: [Reactor](http://projectreactor.io/)
- Persistence : [Spring Data Reactive MongoDB](https://spring.io/blog/2016/11/28/going-reactive-with-spring-data)
- Build: [Gradle Script Kotlin](https://github.com/gradle/gradle-script-kotlin)
- Testing: [Junit](http://junit.org/)

### Prerequisite
- Install [Git](https://git-scm.com/)
- [Fork](https://github.com/tiarebalbi/kotlin-tech-board#fork-destination-box) and clone [the project](https://github.com/tiarebalbi/kotlin-tech-board)
- [Install MongoDB](https://www.mongodb.com/download-center)
- [Install Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

## Contributions

### Branches
- master: All commits that are going to release
- develop: Snapshot version with all commits during the period of development
- feature/{ISSUE-NUMBER}: Branch for a specific feature