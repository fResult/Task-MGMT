package dev.fresult.taskmgmt.entities

import jakarta.validation.constraints.FutureOrPresent
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.springframework.data.annotation.*
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.time.LocalDate

@Table("tasks")
data class Task(
  @Id
  override val id: Long? = null,

  @field:Size(min = 3, message = "title must be [3] characters or more")
  @field:NotBlank(message = "title must not be empty")
  val title: String,

  val description: String,

  @field:FutureOrPresent(message = "dueDate must not be in the past")
  @field:NotNull(message = "dueDate must not be empty")
  val dueDate: LocalDate,

  // TODO: Use enum as a type
  // @Column(converter = TaskStatusConverter::class.java)
  // val status: TaskStatus,
  @field:NotNull(message = "status must not be empty")
  val status: TaskStatus,
//  val createdBy: Long,
//  val updatedBy: Long,

  @Reference(User::class)
  val userId: Long,

  @CreatedDate
  override val createdAt: Instant? = null,

  @LastModifiedDate
  override val updatedAt: Instant? = null,

  /** Start with version 0 when created */
  @Version
  override val version: Int? = null,
) : BaseEntity(id)

enum class TaskStatus {
  PENDING,
  IN_PROGRESS,
  COMPLETED
}
