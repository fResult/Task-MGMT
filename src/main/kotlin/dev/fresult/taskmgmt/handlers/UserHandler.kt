package dev.fresult.taskmgmt.handlers

import dev.fresult.taskmgmt.services.UserService
import kotlinx.coroutines.reactive.asFlow
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyAndAwait

@Component
class UserHandler(private val service: UserService) {
  suspend fun all(request: ServerRequest) =
    ServerResponse.ok().bodyAndAwait(service.all().asFlow())
}
