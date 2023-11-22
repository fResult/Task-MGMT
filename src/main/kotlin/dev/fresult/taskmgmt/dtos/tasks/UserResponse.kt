package dev.fresult.taskmgmt.dtos.tasks

import java.time.Instant

data class UserResponse(
  val id: Long,
  val email: String,
  val firstName: String,
  val lastName: String,
  val createdAt: Instant,
  val updatedAt: Instant,
)
