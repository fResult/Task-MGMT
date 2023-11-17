package dev.fresult.taskmgmt.routers

import dev.fresult.taskmgmt.handlers.TaskHandler
import dev.fresult.taskmgmt.repositories.TaskRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class TaskRouter(private val taskHandler: TaskHandler, private val taskRepository: TaskRepository) {
  @Bean
  fun taskRoutes(): RouterFunction<ServerResponse> = coRouter {
    "/tasks".nest {
      GET("", taskHandler::all)
      GET("/{id}", taskHandler::byId)
      POST("", taskHandler::create)
      PUT("/{id}", taskHandler::update)
      DELETE("/{id}", taskHandler::deleteById)
    }
  }
}