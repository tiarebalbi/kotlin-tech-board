package com.tiarebalbi

import com.tiarebalbi.support.RouterFunctionProvider
import com.tiarebalbi.support.run
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse

@SpringBootApplication
open class TechBoardApplication {

  @Bean
  fun routerFunction(routesProvider: List<RouterFunctionProvider>) =
    routesProvider.map { it.invoke() }.reduce(RouterFunction<ServerResponse>::and)

}
fun main(args: Array<String>) {
  run(TechBoardApplication::class, *args)
}

