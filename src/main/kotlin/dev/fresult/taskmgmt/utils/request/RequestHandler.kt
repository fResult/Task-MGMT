package dev.fresult.taskmgmt

import org.springframework.web.reactive.function.server.ServerRequest

fun getPathId(request: ServerRequest): Long = request.pathVariable("id").toLong()
