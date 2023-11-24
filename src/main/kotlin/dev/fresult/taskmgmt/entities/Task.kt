package dev.fresult.taskmgmt.entities

import dev.fresult.taskmgmt.dtos.*
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

  val title: String,
  val description: String?,
  val dueDate: LocalDate,
  val status: TaskStatus,

  @CreatedBy
  @Reference(User::class)
  val createdBy: Long? = null,

  @LastModifiedBy
  @Reference(User::class)
  val updatedBy: Long? = null,

  @CreatedDate
  override val createdAt: Instant? = null,

  @LastModifiedDate
  override val updatedAt: Instant? = null,

  /** Start with version 0 when created */
  @Version
  override val version: Int? = null,
) : BaseEntity(id) {
  companion object {
    fun fromTaskRequest(body: TaskRequest): Task =
      Task(
        title = body.title,
        description = body.description,
        dueDate = body.dueDate,
        status = body.status,
        createdBy = body.userId,
        updatedBy = body.userId,
      )

    fun fromUpdateTaskRequest(body: TaskRequest): Task =
      Task(
        title = body.title,
        description = body.description,
        dueDate = body.dueDate,
        status = body.status,
        updatedBy = body.userId,
      )

  }

  fun toTaskResponse(): TaskResponse = TaskResponse(
    id = id!!,
    title = title,
    description = description,
    dueDate = dueDate,
    status = status,
    createdBy = createdBy!!,
    updatedBy = updatedBy!!,
    createdAt = createdAt!!,
    updatedAt = updatedAt!!,
  )

  fun toCreateTaskResponse(user: UserResponse): CreateTaskResponse = toUpdateTaskResponse(user, user)

  fun toUpdateTaskResponse(owner: UserResponse, updater: UserResponse): CreateTaskResponse {
    val getFullName: (UserResponse) -> String = { listOf(it.firstName, it.lastName).joinToString(" ") }
    return CreateTaskResponse(
      id = id!!,
      title = title,
      description = description!!,
      dueDate = dueDate,
      status = status,
      createdBy = getFullName(owner),
      updatedBy = getFullName(updater),
    )
  }

  fun toTaskWithUserResponse(userResp: UserResponse): TaskWithOwnerResponse =
    TaskWithOwnerResponse(
      id = id!!,
      title = title,
      description = description!!,
      dueDate = dueDate,
      status = status,
      owner = userResp,
      updater = userResp,
    )
}

enum class TaskStatus {
  PENDING,
  IN_PROGRESS,
  COMPLETED
}
