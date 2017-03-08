package com.tiarebalbi.support

import org.springframework.web.reactive.function.server.RouterDsl
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.Routes
import org.springframework.web.reactive.function.server.ServerResponse

abstract class RouterFunctionProvider : () -> RouterFunction<ServerResponse> {

  override fun invoke(): RouterFunction<ServerResponse> = RouterDsl().apply(routes).router()

  abstract val routes: Routes

}