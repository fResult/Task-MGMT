package dev.fresult.taskmgmt.dtos

import dev.fresult.taskmgmt.entities.TaskStatus
import java.time.LocalDate

data class TaskQueryParams(
  val dueDate: LocalDate? = null,
  val status: TaskStatus? = null,
  val createdBy: Long? = null,
  val updatedBy: Long? = null
)

data class TaskQueryParamValues(
  val dueDates: List<LocalDate> = listOf(),
  val statuses: List<TaskStatus> = listOf(),
  val createdByUsers: List<Long> = listOf(),
  val updatedByUsers: List<Long> = listOf(),
)
