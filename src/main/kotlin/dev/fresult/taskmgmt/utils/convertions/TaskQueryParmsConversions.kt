package dev.fresult.taskmgmt.utils.convertions

import dev.fresult.taskmgmt.entities.TaskStatus
import java.time.LocalDate

/**
 * @return the LocalDate value
 * @throws IllegalArgumentException
 *
 * ```kt
 * "YYYY-MM-dd".toLocalDate()
 * ```
 */
fun String.toLocalDate(/*dateStr: String*/): LocalDate {
  val ymd = this.split("-").stream().filter(String::isNotBlank).map(String::toInt).toList()

  return if (ymd.size == 3) {
    val (y, m, d) = ymd
    LocalDate.of(y, m, d)
  } else {
    throw IllegalArgumentException("[«field»] format should be [«date_pattern»]")
  }
}

fun toLocalDates(paramValues: List<String>): List<LocalDate>? {
  return paramValues.stream().filter(String::isNotBlank).map(String::toLocalDate).toList().ifEmpty { null }
}

fun toStatuses(paramValues: List<String>): List<TaskStatus>? {
  return paramValues.stream().map { enumValueOf<TaskStatus>(it) }.toList().ifEmpty { null }
}

fun toLongs(paramValues: List<String>): List<Long>? {
  // return if (paramValues?.isNotEmpty()!!) paramValues.stream().map(String::toLong).toList() else null
  return paramValues.stream().map(String::toLong).toList().ifEmpty { null }
}
