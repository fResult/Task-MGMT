package dev.fresult.taskmgmt.entities

import dev.fresult.taskmgmt.dtos.CreateUserRequest
import dev.fresult.taskmgmt.dtos.UpdateUserRequest
import dev.fresult.taskmgmt.dtos.UserResponse
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

  val email: String,

  // TODO: Hash Password
  val password: String? = null,
  val firstName: String,
  val lastName: String,

  @CreatedDate
  override val createdAt: Instant? = null,

  @LastModifiedDate
  override val updatedAt: Instant? = null,

  /** Start with version 0 when created */
  @Version
  override val version: Int? = null,
) : BaseEntity(id) {
  companion object {
    fun fromCreateUserRequest(body: CreateUserRequest): User = User(
      email = body.email,
      password = body.password,
      firstName = body.firstName,
      lastName = body.lastName,
    )

    fun fromUpdateUserRequest(body: UpdateUserRequest): User = User(
      email = body.email,
      firstName = body.firstName,
      lastName = body.lastName,
    )
  }

  fun toUserResponse(): UserResponse = UserResponse(
    id = id!!,
    email = email,
    firstName = firstName,
    lastName = lastName,
    createdAt = createdAt!!,
    updatedAt = updatedAt!!,
  )
}
