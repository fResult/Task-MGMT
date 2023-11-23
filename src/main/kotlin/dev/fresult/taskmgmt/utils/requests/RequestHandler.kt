package dev.fresult.taskmgmt.utils.requests

import org.springframework.web.reactive.function.server.ServerRequest
import kotlin.jvm.optionals.getOrNull

fun getPathId(request: ServerRequest): Long = request.pathVariable("id").toLong()

private typealias QueryParam = String
fun getQueryParam(request: ServerRequest):  (QueryParam) -> String? {
  return { param ->
    request.queryParam(param).getOrNull()
  }
}
