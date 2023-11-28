package dev.fresult.taskmgmt.handlers

import dev.fresult.taskmgmt.dtos.TaskQueryParamValues
import dev.fresult.taskmgmt.dtos.TaskRequest
import dev.fresult.taskmgmt.dtos.TaskStatusRequest
import dev.fresult.taskmgmt.entities.Task
import dev.fresult.taskmgmt.services.TaskService
import dev.fresult.taskmgmt.services.UserService
import dev.fresult.taskmgmt.utils.convertions.toLocalDates
import dev.fresult.taskmgmt.utils.convertions.toLongs
import dev.fresult.taskmgmt.utils.convertions.toStatuses
import dev.fresult.taskmgmt.utils.requests.getPathId
import dev.fresult.taskmgmt.utils.requests.getQueryParamValues
import dev.fresult.taskmgmt.utils.responses.BadRequestResponse
import dev.fresult.taskmgmt.utils.responses.responseNotFound
import dev.fresult.taskmgmt.utils.then
import dev.fresult.taskmgmt.utils.validations.entryMapErrors
import jakarta.validation.Validator
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingle
import org.apache.logging.log4j.LogManager
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Component
class TaskHandler(
  private val service: TaskService,
  private val userService: UserService,
  private val validator: Validator,
) {
  private val log = LogManager.getLogger(TaskHandler::class.java)
  private val taskRespNotFound = responseNotFound(Task::class, TaskHandler::class)

  suspend fun all(request: ServerRequest): ServerResponse {
    val queryParams = request.queryParams()
    val getRequestParamValues = getQueryParamValues(queryParams)
    val getIdsFromQueryParams = getRequestParamValues then ::toLongs

    val taskQueryParams = TaskQueryParamValues(
      dueDates = (getRequestParamValues then ::toLocalDates)("due-date"),
      statuses = (getRequestParamValues then ::toStatuses)("status"),
      createdByUsers = getIdsFromQueryParams("created-by"),
      updatedByUsers = getIdsFromQueryParams("updated-by"),
    )

    val fetchedTask =
      if (queryParams.isEmpty()) service.all()
      else service.allByQueryParams(taskQueryParams)

    return ServerResponse.ok().bodyAndAwait(fetchedTask.map(Task::toTaskResponse).asFlow())
  }

  suspend fun byId(request: ServerRequest): ServerResponse {
    val id = getPathId(request)

    return service.byId(id)
      .flatMap { ServerResponse.ok().bodyValue(it.toTaskResponse()) }
      .switchIfEmpty { taskRespNotFound(id) }.awaitSingle()
  }

  suspend fun create(request: ServerRequest): ServerResponse {
    return request.bodyToMono<TaskRequest>()
      .flatMap(::doCreate).awaitSingle()
    // .flatMap { body ->
    //   val violations = validator.validate(body)
    //
    //   if (violations.isEmpty()) {
    //     userService.byId(body.userId).map(User::toUserResponse).flatMap { _ ->
    //       val taskToCreate = Task.fromTaskRequest(body)
    //       val createdTask = service.create(taskToCreate)
    //       ServerResponse.status(HttpStatus.CREATED).body(createdTask.map(Task::toTaskResponse))
    //     }.switchIfEmpty(userIdDoesNotExists(body.userId))
    //   } else {
    //     val badRequestResp = BadRequestResponse(entryMapErrors(violations))
    //     ServerResponse.badRequest().bodyValue(badRequestResp)
    //   }
    // }.awaitSingle()
  }

  private fun bodyToTask(body: TaskRequest) = Task.fromTaskRequest(body)
  private fun createTaskAndMapResp(task: Task) = service.create(task).map(Task::toTaskResponse)
  private fun doCreate(body: TaskRequest): Mono<ServerResponse> {
    val violations = validator.validate(body)
    return if (violations.isEmpty()) {
      val createTaskResponse = ::bodyToTask then ::createTaskAndMapResp
      ServerResponse.status(HttpStatus.CREATED).body(createTaskResponse(body))
    } else {
      val badRequestResp = BadRequestResponse(entryMapErrors(violations))
      ServerResponse.badRequest().bodyValue(badRequestResp)
    }
  }

  suspend fun update(request: ServerRequest): ServerResponse {
    val id = getPathId(request)

    // TODO: refactor ExistingTask not found by switchIfEmpty
    return try {
      val existingTask = service.byId(id).awaitSingle()
      val updatedTaskMono = request.bodyToMono<TaskRequest>().flatMap { body ->
        val violations = validator.validate(body)

        if (violations.isEmpty()) {
          userService.byId(body.userId)
            .flatMap { _ ->
              val taskFromBody = Task.fromTaskRequest(body)
              val taskToUpdate = service.copyToUpdate(existingTask)(taskFromBody)
              ServerResponse.ok().body(service.update(id)(taskToUpdate).map(Task::toTaskResponse))
            }
            .switchIfEmpty(userIdDoesNotExists(body.userId))
        } else {
//          val badRequestResp = entryMapErrors(violations).toMono().map { BadRequestResponse(it) }
          val errorResponse = BadRequestResponse(entryMapErrors(violations))
          ServerResponse.badRequest().bodyValue(errorResponse)
        }
      }

      updatedTaskMono.awaitSingle()
    } catch (ex: NoSuchElementException) {
      taskRespNotFound(id).awaitSingle()
    }
  }

  suspend fun updateStatus(request: ServerRequest): ServerResponse {
    val id = getPathId(request)

    return request.bodyToMono<TaskStatusRequest>().flatMap { body ->
      val violations = validator.validate(body)
      if (violations.isEmpty()) {
        service.byId(id).flatMap { existingTask ->
          // val taskToUpdate = service.copyToUpdate(existingTask)(body)
          val taskToUpdate = existingTask.copy(status = body.status, updatedBy = body.userId)
          val taskResponse = service.update(id)(taskToUpdate).map(Task::toTaskResponse)
          ServerResponse.ok().body(taskResponse)
        }.switchIfEmpty { taskRespNotFound(id) }
      } else {
        val badRequestResp = BadRequestResponse(entryMapErrors(violations))
        ServerResponse.badRequest().bodyValue(badRequestResp)
      }
    }.awaitSingle()
  }

  suspend fun deleteById(request: ServerRequest): ServerResponse {
    val id = getPathId(request)
    val noContentResponse = ServerResponse.noContent().build()

    return service.deleteById(id).flatMap {
      noContentResponse
    }.switchIfEmpty {
      log.info("Task with ID {} does not exist", id)
      noContentResponse
    }.awaitSingle()
  }

  private fun userIdDoesNotExists(userId: Long): () -> Mono<ServerResponse> = {
    val errorMessage = "User with ID [{}] does not exist"
    // val badRequestResp = BadRequestResponse(mapOf(Pair("userId", errorMessage)))
    val badRequestResp = BadRequestResponse(mapOf("userId" to errorMessage.replace("{}", userId.toString())))
    log.error(errorMessage, userId)
    ServerResponse.badRequest().bodyValue(badRequestResp)
  }
}
