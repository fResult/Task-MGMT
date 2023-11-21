package dev.fresult.taskmgmt.entities

import jakarta.validation.constraints.FutureOrPresent
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Reference
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.time.LocalDate

@Table("tasks")
data class Task(
  @Id
  override val id: Long? = null,

  @field:NotBlank(message = "title must not be empty")
  val title: String,

  val description: String,

  @field:FutureOrPresent(message = "dueDate must not be past")
  @field:NotNull(message = "dueDate must not be empty")
  val dueDate: LocalDate,

  // TODO: Use enum as a type
  // @Column(converter = TaskStatusConverter::class.java)
  // val status: TaskStatus,
  @field:NotNull(message = "status must not be empty")
  val status: TaskStatus,
//  val createdBy: Long,
//  val updatedBy: Long,

  @Reference(value = User::class)
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
