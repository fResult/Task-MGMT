package dev.fresult.taskmgmt.utils.responses

import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import kotlin.reflect.KClass

typealias Id = Long

fun responseNotFound(clazz: KClass<*>): (Id) -> Mono<ServerResponse> {
  return { id ->
    println("[${clazz.simpleName}] with ID [$id] does not exist")
    ServerResponse.notFound().build()
  }
}
