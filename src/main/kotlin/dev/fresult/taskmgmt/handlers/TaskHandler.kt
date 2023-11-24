package dev.fresult.taskmgmt.handlers

import dev.fresult.taskmgmt.constants.TaskConditions
import dev.fresult.taskmgmt.entities.Task
import dev.fresult.taskmgmt.entities.TaskStatus
import dev.fresult.taskmgmt.entities.User
import dev.fresult.taskmgmt.services.TaskService
import dev.fresult.taskmgmt.services.UserService
import dev.fresult.taskmgmt.utils.requests.getPathId
import dev.fresult.taskmgmt.utils.requests.getQueryParam
import dev.fresult.taskmgmt.utils.responses.BadRequestResponse
import dev.fresult.taskmgmt.utils.responses.responseNotFound
import dev.fresult.taskmgmt.utils.validations.entryMapErrors
import jakarta.validation.Validator
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Flux
import reactor.kotlin.core.publisher.switchIfEmpty
import java.time.LocalDate

@Component
class TaskHandler(
  private val service: TaskService,
  private val userService: UserService,
  private val validator: Validator,
) {
  private val taskRespNotFound = responseNotFound(Task::class)

  suspend fun all(request: ServerRequest): ServerResponse {
//    return ServerResponse.ok().body(service.all().map(Task::toTaskResponse)).awaitSingle()
    return ServerResponse.ok().bodyAndAwait(service.all().map(Task::toTaskResponse).asFlow())
  }

  suspend fun byId(request: ServerRequest): ServerResponse {
    val id = getPathId(request)

    return service.byId(id).flatMap {
      ServerResponse.ok().bodyValue(it.toTaskResponse())
    }.switchIfEmpty { taskRespNotFound(id) }.awaitSingle()
  }

  suspend fun create(request: ServerRequest): ServerResponse {
    return request.bodyToMono(Task::class.java)
      .flatMap { body ->
        val violations = validator.validate(body)

        if (violations.isEmpty()) {
          userService.existsById(body.userId).flatMap { isExisting ->
            if (isExisting) {
              ServerResponse.status(HttpStatus.CREATED).body(service.create(body).map(Task::toTaskResponse))
            } else {
              val errorMessage = "User with ID ${body.userId} does not exist."
              println(errorMessage)
//            val errorResponse = BadRequestResponse(mapOf(Pair("userId", errorMessage)))
              val errorResponse = BadRequestResponse(mapOf("userId" to errorMessage))
              ServerResponse.badRequest().bodyValue(errorResponse)
            }
          }
        } else {
          val errorResponse = BadRequestResponse(entryMapErrors(violations))
          ServerResponse.badRequest().bodyValue(errorResponse)
        }
      }.awaitSingle()
  }

  suspend fun update(request: ServerRequest): ServerResponse {
    val id = getPathId(request)

    // TODO: refactor ExistingTask not found by switchIfEmpty
    return try {
      val existingTask = service.byId(id).awaitSingle()
      val updatedTaskMono = request.bodyToMono<Task>().flatMap { body ->
        val violations = validator.validate(body)

        if (violations.isEmpty()) {
          val taskToUpdate = service.copy(existingTask)(body)
          ServerResponse.ok().body(service.update(id, taskToUpdate).map(Task::toTaskResponse))
        } else {
//          val errorResponse = entryMapErrors(violations).toMono().map { BadRequestResponse(it) }
          val errorResponse = BadRequestResponse(entryMapErrors(violations))
          ServerResponse.badRequest().bodyValue(errorResponse)
        }
      }

      updatedTaskMono.awaitSingle()
    } catch (ex: NoSuchElementException) {
      taskRespNotFound(id).awaitSingle()
    }
  }

  suspend fun deleteById(request: ServerRequest): ServerResponse {
    val id = getPathId(request)
    val noContentResponse = ServerResponse.noContent().build()

    return service.deleteById(id).flatMap {
      noContentResponse
    }.switchIfEmpty {
      println("Task with ID $id does not exist")
      noContentResponse
    }.awaitSingle()
  }

  suspend fun allByUserId(request: ServerRequest): ServerResponse {
    val getRequestParam = getQueryParam(request)

    // TODO: Refactor here
    val userId = request.pathVariable("userId").toLong()
    // TODO: Validate due-date date format -> It's 500 when invalid format right now
    val dueDate = getRequestParam("due-date").orEmpty()

    val statusParam = request.queryParam("status")
    val status = (if (statusParam.isPresent) statusParam.get() else TaskStatus.PENDING) as String
    // TODO: Validate status -> It's 500 when invalid format right now
    val taskStatus = enumValueOf<TaskStatus>(status)
    // TODO: Validate created-by date format -> It's 500 when invalid format right now
    val createdBy = getRequestParam("created-by")
    // TODO: Validate updated-by date format -> It's 500 when invalid format right now
    val updatedBy = getRequestParam("updated-by")

    val ymd = dueDate.split("-").stream().filter { it.isNotBlank() }.map(String::toInt).toList()
    val dueDateLocalDate = if (ymd.size == 3) {
      val (y, m, d) = ymd
      LocalDate.of(y, m, d)
    } else null
    val conditions = TaskConditions(
      dueDate = dueDateLocalDate,
      status = taskStatus,
      createdBy = createdBy?.toLong(),
      updatedBy = updatedBy?.toLong(),
    )

    return userService.byId(userId)
      .map(User::toUserResponse)
      .flatMap { user ->
        ServerResponse.ok().body(service.allByUserId(userId, conditions).map { task ->
          task.toTaskWithUserResponse(user)
        })
      }.switchIfEmpty {
        ServerResponse.ok().body(Flux.empty<Task>())
      }.awaitSingle()
  }
}
