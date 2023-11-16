package dev.fresult.taskmgmt.routers

import dev.fresult.taskmgmt.entities.Task
import dev.fresult.taskmgmt.entities.TaskRequest
import dev.fresult.taskmgmt.handlers.TaskHandler
import dev.fresult.taskmgmt.repositories.TaskRepository
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.RouterFunctions.nest
import org.springframework.web.reactive.function.server.RouterFunctions.route
import java.util.*

@Configuration
class TaskRouter(private val taskHandler: TaskHandler, private val taskRepository: TaskRepository) {
  @Bean
  fun taskRoutes(): RouterFunction<ServerResponse> = coRouter {
//    "/tasks".nest {
//      GET("") {
//        ServerResponse.ok().bodyValueAndAwait(taskRepository.findAll())
//      }
//      GET("/{id}")
//      POST("", taskHandler::create)
//    }

    "/tasks".nest {
      GET("", taskHandler::all)
      GET("/{id}", taskHandler::byId)
      POST("", taskHandler::create)
    }
  }
}
//      GET("/{id}", {
//        val id = it.pathVariable("id").toString()
//        ServerResponse.ok().bodyValueAndAwait()
//      })