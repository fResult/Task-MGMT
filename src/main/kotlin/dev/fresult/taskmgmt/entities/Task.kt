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
  @Id override val id: Long? = null,
//  @Id val id: Long? = null,
  @Nonnull val title: String,
  val description: String,
  val dueDate: LocalDate,
//  @Column(converter = TaskStatusConverter::class.java)
//  val status: TaskStatus,
  val status: String,
  val createdBy: Long,
  val updatedBy: Long,

  @CreatedDate
  override val createdAt: Instant? = null,

  @LastModifiedDate
  override val updatedAt: Instant? = null,

  /** Start with version 0 when created */
  @Version
  override val version: Int? = null,
) : BaseEntity(id)

/* {
  fun <T: BaseEntity> withBase(base: T): Task {
//    super.id = base.id
    super.version = base.version
    super.createdAt = base.createdAt
    super.updatedAt = base.updatedAt

    return this
  }
}*/

enum class TaskStatus {
  PENDING,
  IN_PROGRESS,
  COMPLETED
}
