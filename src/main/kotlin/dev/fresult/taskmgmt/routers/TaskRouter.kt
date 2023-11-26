package dev.fresult.taskmgmt.routers

import dev.fresult.taskmgmt.handlers.TaskHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class TaskRouter(private val handler: TaskHandler) {
  @Bean
  fun taskRoutes(): RouterFunction<ServerResponse> = coRouter {
    "/tasks".nest {
      GET("", handler::all)
      GET("/{id}", handler::byId)
      POST("", handler::create)
      PUT("/{id}", handler::update)
      DELETE("/{id}", handler::deleteById)
    }
  }
}
