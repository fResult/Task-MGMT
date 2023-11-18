package dev.fresult.taskmgmt.entities

import jakarta.annotation.Nonnull
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("users")
data class User(
  @Id val id: Long,
  @Nonnull val firstName: String,
  @Nonnull val lastName: String,

)
