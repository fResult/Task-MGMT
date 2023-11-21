package dev.fresult.taskmgmt.utils.requests

import org.springframework.web.reactive.function.server.ServerRequest

fun getPathId(request: ServerRequest): Long = request.pathVariable("id").toLong()
