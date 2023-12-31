package dev.fresult.taskmgmt.utils.requests

import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.server.ServerRequest
import kotlin.jvm.optionals.getOrNull

fun getPathId(request: ServerRequest): Long = request.pathVariable("id").toLong()

private typealias QueryParam = String

fun getQueryParam(request: ServerRequest): (QueryParam) -> String? {
  return { param ->
    request.queryParam(param).getOrNull()
  }
}

fun getQueryParamValues(queryParams:  MultiValueMap<String, String>): (QueryParam) -> List<String> {
  return { param ->
    queryParams[param].orEmpty()
  }
}
