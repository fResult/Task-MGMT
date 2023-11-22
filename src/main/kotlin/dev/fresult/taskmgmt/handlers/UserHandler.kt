package dev.fresult.taskmgmt.handlers

import dev.fresult.taskmgmt.entities.User
import dev.fresult.taskmgmt.services.UserService
import dev.fresult.taskmgmt.utils.requests.getPathId
import dev.fresult.taskmgmt.utils.responses.BadRequestResponse
import dev.fresult.taskmgmt.utils.responses.responseNotFound
import dev.fresult.taskmgmt.utils.validations.entryMapErrors
import jakarta.validation.Validator
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import reactor.kotlin.core.publisher.switchIfEmpty

@Component
class UserHandler(private val service: UserService, private val validator: Validator) {
  private val userResponseNotFound = responseNotFound(User::class)

  suspend fun all(request: ServerRequest) =
    ServerResponse.ok().bodyAndAwait(service.all().map(User::toUserResponse).asFlow())

  suspend fun byId(request: ServerRequest): ServerResponse {
    val id = getPathId(request)

    return service.byId(id).flatMap {
      ServerResponse.ok().bodyValue(it.toUserResponse())
    }.switchIfEmpty { userResponseNotFound(id) }.awaitSingle()
  }

  suspend fun create(request: ServerRequest): ServerResponse {
    return request.bodyToMono(User::class.java)
      .flatMap { body ->
        val violations = validator.validate(body)

        if (violations.isEmpty()) {
          ServerResponse.status(HttpStatus.CREATED).body(service.create(body).map(User::toUserResponse))
        } else {
          val errorResponse = BadRequestResponse(entryMapErrors(violations))
          ServerResponse.badRequest().bodyValue(errorResponse)
        }
      }.awaitSingle()
  }

  suspend fun update(request: ServerRequest): ServerResponse {
    val id = getPathId(request)

    return try {
      val exitingUser = service.byId(id).awaitSingle()
      val updatedUserMono = request.bodyToMono<User>().flatMap { body ->
        val violations = validator.validate(body)

        if (violations.isEmpty()) {
          val userToUpdate = service.copy(exitingUser)(body)
          ServerResponse.ok().body(service.update(id, userToUpdate).map(User::toUserResponse))
        } else {
          val errorResponse = BadRequestResponse(entryMapErrors(violations))
          ServerResponse.badRequest().bodyValue(errorResponse)
        }
      }

      updatedUserMono.awaitSingle()
    } catch (ex: NoSuchElementException) {
      userResponseNotFound(id).awaitSingle()
    }
  }

  suspend fun deleteById(request: ServerRequest): ServerResponse {
    val id = getPathId(request)

    return service.deleteById(id).flatMap {
      ServerResponse.noContent().build()
    }.switchIfEmpty {
      println("User with ID $id does not exist")
      ServerResponse.noContent().build()
    }.awaitSingle()
  }
}
