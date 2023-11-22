package dev.fresult.taskmgmt.dtos

import dev.fresult.taskmgmt.entities.TaskStatus
import java.time.LocalDate

data class TaskResponse(
  val id: Long,
  val title: String,
  val description: String?,
  val dueDate: LocalDate,
  val status: TaskStatus,
  val userId: Long,
)

data class TaskWithOwnerResponse(
  val id: Long,
  val title: String,
  val description: String?,
  val dueDate: LocalDate,
  val status: TaskStatus,
  val owner: UserResponse,
)
