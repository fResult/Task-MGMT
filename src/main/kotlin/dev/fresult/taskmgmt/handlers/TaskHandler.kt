package dev.fresult.taskmgmt.handlers

import dev.fresult.taskmgmt.entities.Task
import dev.fresult.taskmgmt.services.TaskService
import dev.fresult.taskmgmt.services.UserService
import dev.fresult.taskmgmt.utils.request.getPathId
import dev.fresult.taskmgmt.utils.validations.BadRequestResponse
import dev.fresult.taskmgmt.utils.validations.entryMapErrors
import jakarta.validation.Validator
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.publisher.toMono

@Component
class TaskHandler(
  private val service: TaskService,
  private val userService: UserService,
  private val validator: Validator,
) {
  suspend fun all(request: ServerRequest): ServerResponse {
    return ServerResponse.ok().bodyAndAwait(service.all().asFlow())
  }

  suspend fun byId(request: ServerRequest): ServerResponse {
    val id = getPathId(request)

    return service.byId(id).flatMap {
      ServerResponse.ok().bodyValue(it)
    }.switchIfEmpty {
      println("Task with ID $id does not exist.")
      ServerResponse.notFound().build()
    }.awaitSingle()
  }

  suspend fun create(request: ServerRequest): ServerResponse {
    return request.bodyToMono(Task::class.java)
      .flatMap { body ->
        val violations = validator.validate(body)
        println("vio $violations")

        if (violations.isEmpty()) {
          userService.byId(body.userId).flatMap {
            ServerResponse.status(HttpStatus.CREATED).body(service.create(body))
          }.switchIfEmpty {
            val errorMessage = "User with ID ${body.userId} does not exist."
            println(errorMessage)
//            val errorResponse = BadRequestResponse(mapOf(Pair("userId", errorMessage)))
            val errorResponse = BadRequestResponse(mapOf("userId" to errorMessage))
            ServerResponse.badRequest().bodyValue(errorResponse)
          }
        } else {
          val errorResponse = BadRequestResponse(entryMapErrors(violations))
          ServerResponse.badRequest().bodyValue(errorResponse)
        }
      }.awaitSingle()
  }

  suspend fun update(request: ServerRequest): ServerResponse {
    val id = getPathId(request)

    return try {
      val existedTask = service.byId(id).awaitSingle()
      request.bodyToMono<Task>().flatMap {
        val taskToUpdate = service.copy(existedTask)(it)

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
}
