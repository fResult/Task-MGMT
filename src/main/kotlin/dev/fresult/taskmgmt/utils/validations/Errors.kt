package dev.fresult.taskmgmt.utils.validations

import jakarta.validation.ConstraintViolation

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
