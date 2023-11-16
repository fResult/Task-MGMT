package dev.fresult.taskmgmt.handlers

import dev.fresult.taskmgmt.entities.TaskRequest
import dev.fresult.taskmgmt.services.TaskService
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*

@Component
class TaskHandler(private val service: TaskService) {
  suspend fun all(request: ServerRequest): ServerResponse {
    return ServerResponse.ok().bodyAndAwait(service.all().asFlow())
  }

  suspend fun byId(request: ServerRequest): ServerResponse {
    return try {
      val id = request.pathVariable("id").toLong()

      ServerResponse.ok().bodyValueAndAwait(service.byId(id).awaitSingle())
    } catch (ex: NoSuchElementException) {
      ServerResponse.notFound().buildAndAwait()
    }
  }

  suspend fun create(request: ServerRequest): ServerResponse {
    return request.bodyToMono(TaskRequest::class.java)
      .flatMap {
        ServerResponse.status(HttpStatus.CREATED).body(service.create(it))
          .switchIfEmpty(ServerResponse.notFound().build())
      }.awaitSingle()
  }
}
