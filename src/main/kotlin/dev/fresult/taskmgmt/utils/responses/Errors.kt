package dev.fresult.taskmgmt.utils.responses

import org.springframework.http.HttpStatus

open class ErrorResponse(val statusCode: Int, open val errors: Map<String, *>)

data class BadRequestResponse<T : Any>(override val errors: Map<String, T>) :
  ErrorResponse(HttpStatus.BAD_REQUEST.value(), errors)

data class UnAuthorizedResponse<T : Any>(override val errors: Map<String, T>) :
  ErrorResponse(HttpStatus.UNAUTHORIZED.value(), errors)

data class NotFoundResponse<T : Any>(override val errors: Map<String, T>) :
  ErrorResponse(HttpStatus.NOT_FOUND.value(), errors)

data class ServerErrorResponse<T : Any>(override val errors: Map<String, T>) :
  ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), errors)
