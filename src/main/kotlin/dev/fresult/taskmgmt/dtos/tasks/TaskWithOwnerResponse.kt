package dev.fresult.taskmgmt.dtos.tasks

import dev.fresult.taskmgmt.entities.User

data class TaskWithOwnerResponse(
  val user: User
)
