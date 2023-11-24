package dev.fresult.taskmgmt.dtos

import dev.fresult.taskmgmt.entities.TaskStatus
import jakarta.validation.constraints.FutureOrPresent
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDate

data class TaskRequest(
  @field:Size(min = 3, message = "title must be [3] characters or more")
  @field:NotBlank(message = "title must not be empty")
  val title: String,

  val description: String?,

  @field:FutureOrPresent(message = "dueDate must not be in the past")
  @field:NotNull(message = "dueDate must not be empty")
  val dueDate: LocalDate,

  // TODO: Use enum as a type
  // @Column(converter = TaskStatusConverter::class.java)
  // val status: TaskStatus,
  @field:NotNull(message = "status must not be empty")
  val status: TaskStatus,

  @field:NotNull(message = "userId must not be empty")
  val userId: Long,
)
