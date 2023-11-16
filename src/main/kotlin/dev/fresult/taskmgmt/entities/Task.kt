package dev.fresult.taskmgmt.entities

import jakarta.annotation.Nonnull
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate

@Table("tasks")
data class Task(
  @Id val id: Long? = null,
  @Nonnull val title: String,
  val description: String,
  val dueDate: LocalDate,
  // TODO: Make this enum ("pending",  "in progress", "completed")
  val status: String,
  val createdBy: Long,
  val lastUpdatedBy: Long
) {
  companion object {
    fun fromTaskRequest(dto: TaskRequest): Task {
      return Task(
        title = dto.title,
        description = dto.description,
        dueDate = dto.dueDate,
        status = dto.status,
        createdBy = dto.createdBy,
        lastUpdatedBy = dto.lastUpdatedBy,
      )
    }
  }
}

data class TaskRequest(
  val title: String,
  val description: String,
  val dueDate: LocalDate,
  val status: String,
  val createdBy: Long,
  val lastUpdatedBy: Long
)
