package dev.fresult.taskmgmt.repositories

import dev.fresult.taskmgmt.entities.Task
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Flux

interface TaskRepository : R2dbcRepository<Task, Long> {
  @Query("SELECT * FROM tasks WHERE user_id = :userId")
  fun findAllByUserId(userId: Long): Flux<Task>
}
