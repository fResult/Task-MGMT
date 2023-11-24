package dev.fresult.taskmgmt.dtos

import dev.fresult.taskmgmt.entities.TaskStatus
import java.time.Instant
import java.time.LocalDate

data class TaskResponse(
  val id: Long,
  val title: String,
  val description: String?,
  val dueDate: LocalDate,
  val status: TaskStatus,
  val createdBy: Long,
  val updatedBy: Long,
  val createdAt: Instant,
  val updatedAt: Instant,
)

data class CreateTaskResponse(
  val id: Long,
  val title: String,
  val description: String?,
  val dueDate: LocalDate,
  val status: TaskStatus,
  val createdBy: String,
  val updatedBy: String,
)

data class TaskWithOwnerResponse(
  val id: Long,
  val title: String,
  val description: String?,
  val dueDate: LocalDate,
  val status: TaskStatus,
  val owner: UserResponse,
  val updater: UserResponse?,
)
