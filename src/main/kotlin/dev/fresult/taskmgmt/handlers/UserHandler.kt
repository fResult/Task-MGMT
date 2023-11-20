package dev.fresult.taskmgmt.handlers

import dev.fresult.taskmgmt.entities.User
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

@Component
class UserHandler(private val service: UserService, private val validator: Validator) {
  suspend fun all(request: ServerRequest) =
    ServerResponse.ok().bodyAndAwait(service.all().asFlow())

  suspend fun byId(request: ServerRequest): ServerResponse {
    val id = getPathId(request)

    return try {
      ServerResponse.ok().bodyValueAndAwait(service.byId(id).awaitSingle())
    } catch (ex: NoSuchElementException) {
      ServerResponse.notFound().buildAndAwait()
    }
  }

  suspend fun create(request: ServerRequest): ServerResponse {
    return request.bodyToMono(User::class.java)
      .flatMap { body ->
        val violations = validator.validate(body)

        if (violations.isEmpty()) {
          ServerResponse.status(HttpStatus.CREATED).body(service.create(body))
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
          ServerResponse.ok().body(service.update(id, userToUpdate))
        } else {
          val errorResponse = BadRequestResponse(entryMapErrors(violations))
          ServerResponse.badRequest().bodyValue(errorResponse)
        }
      }

      updatedUserMono.awaitSingle()
    } catch (ex: NoSuchElementException) {
      println("ERROR: Not Found User $ex")
      ServerResponse.notFound().build().awaitSingle()
    }
  }

  suspend fun deleteById(request: ServerRequest): ServerResponse {
    val id = getPathId(request)

    return try {
      service.deleteById(id).awaitSingle()
      ServerResponse.noContent().buildAndAwait()
    } catch (ex: NoSuchElementException) {
      println("ERROR: Not Found User $ex")
      ServerResponse.noContent().buildAndAwait()
    }
  }
}
