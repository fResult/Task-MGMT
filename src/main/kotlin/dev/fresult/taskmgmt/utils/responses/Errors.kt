package dev.fresult.taskmgmt.utils.responses

import org.springframework.http.HttpStatus

open class ErrorResponse(val statusCode: Int, open val errors: Map<String, String>)

data class BadRequestResponse(override val errors: Map<String, String>) :
  ErrorResponse(HttpStatus.BAD_REQUEST.value(), errors)
data class UnAuthorizedResponse(override val errors: Map<String, String>) :
  ErrorResponse(HttpStatus.UNAUTHORIZED.value(), errors)
data class NotFoundResponse(override val errors: Map<String, String>) :
  ErrorResponse(HttpStatus.NOT_FOUND.value(), errors)
data class ServerErrorResponse(override val errors: Map<String, String>) :
  ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), errors)
