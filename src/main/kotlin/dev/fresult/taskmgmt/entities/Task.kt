package dev.fresult.taskmgmt.entities

import jakarta.annotation.Nonnull
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.time.LocalDate

@Table("tasks")
data class Task(
//  @Id override val id: Long? = null,
  @Id val id: Long? = null,
  @Nonnull val title: String,
  val description: String,
  val dueDate: LocalDate,
//  @Column(converter = TaskStatusConverter::class.java)
//  val status: TaskStatus,
  val status: String,
  val createdBy: Long,
  val updatedBy: Long,
  @CreatedDate
  val createdAt: Instant? = null,
  @LastModifiedDate
  val updatedAt: Instant? = null,

  @Version
  val version: Int,
)/* : BaseEntity(id) */ {

}

enum class TaskStatus {
  PENDING,
  IN_PROGRESS,
  COMPLETED
}
