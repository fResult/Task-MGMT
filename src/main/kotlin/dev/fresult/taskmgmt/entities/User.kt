package dev.fresult.taskmgmt.entities

import dev.fresult.taskmgmt.dtos.CreateUserRequest
import dev.fresult.taskmgmt.dtos.UpdateUserRequest
import dev.fresult.taskmgmt.dtos.UserPasswordRequest
import dev.fresult.taskmgmt.dtos.UserResponse
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("users")
data class User(
  @Id override val id: Long? = null,

  val email: String? = null,

  // TODO: Hash Password
  val password: String? = null,
  val firstName: String? = null,
  val lastName: String? = null,

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

    fun fromChangePasswordRequest(body: UserPasswordRequest): User = User(
      password = body.password
    )
  }

  fun toUserResponse(): UserResponse = UserResponse(
    id = id!!,
    email = email.orEmpty(),
    firstName = firstName.orEmpty(),
    lastName = lastName.orEmpty(),
    createdAt = createdAt!!,
    updatedAt = updatedAt!!,
  )
}
