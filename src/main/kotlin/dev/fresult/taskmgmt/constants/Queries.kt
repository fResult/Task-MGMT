package dev.fresult.taskmgmt.constants

import dev.fresult.taskmgmt.entities.TaskStatus
import java.time.LocalDate

data class TaskConditions(
  val dueDate: LocalDate? = null,
  val status: TaskStatus? = null,
  val createdBy: Long? = null,
  val updatedBy: Long? = null
)
