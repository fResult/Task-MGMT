package dev.fresult.taskmgmt.repositories

import dev.fresult.taskmgmt.entities.Task
import org.springframework.data.r2dbc.repository.R2dbcRepository

interface TaskRepository : R2dbcRepository<Task, Long> {
}
