package dev.fresult.taskmgmt.utils.validations

import jakarta.validation.ConstraintViolation
import org.springframework.http.HttpStatus

open class ErrorResponse(val statusCode: Int, open val errors: Map<String, String>)
data class ServerErrorResponse(override val errors: Map<String, String>) :
  ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), errors)

data class BadRequestResponse(override val errors: Map<String, String>) :
  ErrorResponse(HttpStatus.BAD_REQUEST.value(), errors)
data class UnAuthorizedResponse(override val errors: Map<String, String>) :
  ErrorResponse(HttpStatus.UNAUTHORIZED.value(), errors)

fun <T> joinToJsonError(violations: Set<ConstraintViolation<T>>): String =
  "{ ${
    violations.joinToString(", ", transform = keyValEachViolation)
  } }"

fun <T : Any> entryMapErrors(violations: Set<ConstraintViolation<T>>): Map<String, String> =
  violations.associateBy(getViolationProperty, getViolationMessage)

private val keyValEachViolation: (ConstraintViolation<*>) -> String = { violation ->
  "\"${violation.propertyPath}\": \"${violation.message}\""
}

private val getViolationProperty: (ConstraintViolation<*>) -> String = { it.propertyPath.toString() }

private val getViolationMessage: (ConstraintViolation<*>) -> String = { it.message }

