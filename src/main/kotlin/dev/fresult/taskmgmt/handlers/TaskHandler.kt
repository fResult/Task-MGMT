package dev.fresult.taskmgmt.handlers

import dev.fresult.taskmgmt.dtos.TaskQueryParamValues
import dev.fresult.taskmgmt.dtos.TaskQueryParams
import dev.fresult.taskmgmt.dtos.TaskRequest
import dev.fresult.taskmgmt.entities.Task
import dev.fresult.taskmgmt.entities.TaskStatus
import dev.fresult.taskmgmt.entities.User
import dev.fresult.taskmgmt.services.TaskService
import dev.fresult.taskmgmt.services.UserService
import dev.fresult.taskmgmt.utils.requests.getPathId
import dev.fresult.taskmgmt.utils.requests.getQueryParam
import dev.fresult.taskmgmt.utils.requests.getQueryParamValues
import dev.fresult.taskmgmt.utils.responses.BadRequestResponse
import dev.fresult.taskmgmt.utils.responses.responseNotFound
import dev.fresult.taskmgmt.utils.then
import dev.fresult.taskmgmt.utils.validations.entryMapErrors
import jakarta.validation.Validator
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Flux
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.publisher.toFlux
import java.time.LocalDate

@Component
class TaskHandler(
  private val service: TaskService,
  private val userService: UserService,
  private val validator: Validator,
) {
  private val taskRespNotFound = responseNotFound(Task::class)

  suspend fun all(request: ServerRequest): ServerResponse {
    val queryParams = request.queryParams()
    val getRequestParamValues = getQueryParamValues(queryParams)
    fun mapToLocalDate(dateStr: String): LocalDate {
      val ymd = dateStr.split("-").stream().filter(String::isNotBlank).map(String::toInt).toList()

      return if (ymd.size == 3) {
        val (y, m, d) = ymd
        LocalDate.of(y, m, d)
      } else {
        throw IllegalArgumentException("[«field»] format should be [«date_pattern»]")
      }
    }

    fun toLocalDates(paramValues: List<String>?): List<LocalDate> {
      return paramValues.orEmpty().stream().filter(String::isNotBlank).map(::mapToLocalDate).toList()
    }

    fun toStatuses(paramValues: List<String>?): List<TaskStatus> {
      return paramValues.orEmpty().stream().map { enumValueOf<TaskStatus>(it) }.toList()
    }

    fun toLongs(paramValues: List<String>?): List<Long> {
      return paramValues.orEmpty().stream().map(String::toLong).toList()
    }

    val getIds =  getRequestParamValues then ::toLongs

    val taskQueryParams = TaskQueryParamValues(
      dueDates = (getRequestParamValues then ::toLocalDates)("due-date"),
      statuses = (getRequestParamValues then ::toStatuses)("status"),
      createdByUsers = getIds("created-by"),
      updatedByUsers = getIds("updated-by"),
    )

    val fetchedTasks =
      if (queryParams.isEmpty()) {
        service.all()
      } else {
        service.allByQueryParams(taskQueryParams)
      }
    return ServerResponse.ok().bodyAndAwait(fetchedTasks.map(Task::toTaskResponse).asFlow())
  }

  suspend fun byId(request: ServerRequest): ServerResponse {
    val id = getPathId(request)

    return service.byId(id).flatMap {
      ServerResponse.ok().bodyValue(it.toTaskResponse())
    }.switchIfEmpty { taskRespNotFound(id) }.awaitSingle()
  }

  suspend fun create(request: ServerRequest): ServerResponse {
    return request.bodyToMono<TaskRequest>()
      .flatMap { body ->
        val violations = validator.validate(body)

        if (violations.isEmpty()) {
          userService.byId(body.userId).map(User::toUserResponse).flatMap { exisingUser ->
            val taskToCreate = Task.fromTaskRequest(body)
            val createdTask = service.create(taskToCreate)
            ServerResponse.status(HttpStatus.CREATED).body(createdTask.map(Task::toTaskResponse))
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

    // TODO: refactor ExistingTask not found by switchIfEmpty
    return try {
      val existingTask = service.byId(id).awaitSingle()
      val updatedTaskMono = request.bodyToMono<TaskRequest>().flatMap { body ->
        val violations = validator.validate(body)

        if (violations.isEmpty()) {
          val taskFromBody = Task.fromTaskRequest(body)
          val taskToUpdate = service.copyToUpdate(existingTask)(taskFromBody)
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

  // TODO: Move to proper file

  // TODO: Move to proper file
  private fun makeParams(params: MultiValueMap<String, String>): TaskQueryParamValues {
    fun paramToUserIds(param: List<String>?): List<Long>? {
      return param.orEmpty().stream().map(String::toLong).toList()
    }

    fun mapToLocalDate(dateStr: String): LocalDate {
      val ymd = dateStr.split("-").stream().filter(String::isNotBlank).map(String::toInt).toList()

      return if (ymd.size == 3) {
        val (y, m, d) = ymd
        LocalDate.of(y, m, d)
//      } else if (ymd.isEmpty()) {
//        null
      } else {
        throw IllegalArgumentException("[«field»] format should be [«date_pattern»]")
      }
    }

    fun paramToLocalDates(param: List<String>?): List<LocalDate>? {
      return param.orEmpty().stream().filter(String::isNotBlank).map(::mapToLocalDate).toList()
    }

    val taskQueryParams = TaskQueryParamValues(
      dueDates = paramToLocalDates(params["due-date"]).orEmpty(),
      statuses = params["status"].orEmpty().stream().map { enumValueOf<TaskStatus>(it) }.toList(),
      createdByUsers = paramToUserIds(params["created-by"]).orEmpty(),
      updatedByUsers = paramToUserIds(params["updated-by"]).orEmpty(),
    )
    println("params $taskQueryParams")
    return taskQueryParams
  }

  suspend fun allByUserId(request: ServerRequest): ServerResponse {
    val getRequestParam = getQueryParam(request)

    makeParams(request.queryParams()).dueDates?.toFlux()
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

    val ymd = dueDate.split("-").stream().filter(String::isNotBlank).map(String::toInt).toList()
    println("ymd $ymd")
    val dueDateLocalDate = if (ymd.size == 3) {
      val (y, m, d) = ymd
      LocalDate.of(y, m, d)
    } else if (ymd.isEmpty()) {
      null
    } else {
      return ServerResponse.badRequest()
        .bodyValueAndAwait(BadRequestResponse(mapOf("due-date" to "[due-date] format should be [YYYY-MM-dd]")))
    }
    val conditions = TaskQueryParams(
      dueDate = dueDateLocalDate,
      status = taskStatus,
//      createdBy = createdBy?.toLong(),
//      updatedBy = updatedBy?.toLong(),
    )

    return userService.byId(userId)
      .map(User::toUserResponse)
      .flatMap { user ->
        ServerResponse.ok().body(service.allByUserId(userId, conditions).map { task ->
          task.toTaskWithUserResponse(user)
        })
      }.switchIfEmpty { ServerResponse.ok().body(Flux.empty()) }.awaitSingle()
  }
}
