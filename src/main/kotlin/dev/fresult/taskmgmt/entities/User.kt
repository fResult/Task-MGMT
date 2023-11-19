package dev.fresult.taskmgmt.entities

import jakarta.annotation.Nonnull
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("users")
data class User(
  @Id override val id: Long? = null,
  @Nonnull val firstName: String,
  @Nonnull val lastName: String,

  @CreatedDate
  override val createdAt: Instant? = null,

  @LastModifiedDate
  override val updatedAt: Instant? = null,

  @Version
  override val version: Int? = 1,
) : BaseEntity(id)
