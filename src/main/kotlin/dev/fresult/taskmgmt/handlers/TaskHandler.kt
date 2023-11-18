package dev.fresult.taskmgmt.handlers

import dev.fresult.taskmgmt.entities.Task
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
      val id = getPathId(request)

      ServerResponse.ok().bodyValueAndAwait(service.byId(id).awaitSingle())
    } catch (ex: NoSuchElementException) {
      ServerResponse.notFound().buildAndAwait()
    }
  }

  suspend fun create(request: ServerRequest): ServerResponse {
    return request.bodyToMono(Task::class.java)
      .flatMap {
        ServerResponse.status(HttpStatus.CREATED).body(service.create(it))
//          .switchIfEmpty(ServerResponse.notFound().build())
      }.awaitSingle()
  }

  suspend fun update(request: ServerRequest): ServerResponse {
    val id = getPathId(request)

    return try {
      val existedTask = service.byId(id).awaitSingle()
      request.bodyToMono<Task>().flatMap {
        val taskToUpdate = it.copy(id = existedTask.id, version = existedTask.version, createdAt = existedTask.createdAt, updatedAt = existedTask.updatedAt)
        ServerResponse.ok().body(service.update(id, taskToUpdate))
      }.awaitSingle()
    } catch (ex: NoSuchElementException) {
      ex.message?.let { error("ERROR: $it") }
      ServerResponse.notFound().buildAndAwait()
    }
  }

  suspend fun deleteById(request: ServerRequest): ServerResponse {
    val id = getPathId(request)

    return try {
      service.deleteById(id).awaitSingle()
      ServerResponse.noContent().buildAndAwait()
    } catch (ex: NoSuchElementException) {
      println("ERROR: $ex")
      ServerResponse.noContent().buildAndAwait()
    }
  }

  private fun getPathId(request: ServerRequest): Long {
    return request.pathVariable("id").toLong()
  }
}
