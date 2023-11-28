package dev.fresult.taskmgmt.utils.responses

import dev.fresult.taskmgmt.utils.validations.entryMapErrors
import org.apache.logging.log4j.LogManager
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import kotlin.reflect.KClass

typealias Id = Long

fun responseNotFound(clazz: KClass<*>): (Id) -> Mono<ServerResponse> {
  val log = LogManager.getLogger()
  return { id ->
    val errorMessage = "[${clazz.simpleName}] with ID [{}] does not exist"
    val badRequestResp = BadRequestResponse(mapOf("message" to errorMessage.replace("{}", id.toString())))
    log.error(errorMessage, id)
    ServerResponse.status(HttpStatus.NOT_FOUND).bodyValue(badRequestResp)
  }
}
