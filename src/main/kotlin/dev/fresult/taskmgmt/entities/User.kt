package dev.fresult.taskmgmt.entities

import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("users")
data class User(
  @Id override val id: Long? = null,

  @field:Email
  val email: String,

  @field:NotBlank(message = "password must not be empty")
  @field:Size(min = 6, message = "password length must be more than [6] characters")
  val password: String,

  @field:NotBlank(message = "firstName must not be empty")
  @field:Size(min = 2, message = "firstName must be more than [2] characters")
  val firstName: String,

  @field:NotBlank(message = "lastName must not be empty")
  val lastName: String,

  @CreatedDate
  override val createdAt: Instant? = null,

  @LastModifiedDate
  override val updatedAt: Instant? = null,

  /** Start with version 0 when created */
  @Version
  override val version: Int? = null,
) : BaseEntity(id)
