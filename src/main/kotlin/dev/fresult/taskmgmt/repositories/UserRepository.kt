package dev.fresult.taskmgmt.repositories

import dev.fresult.taskmgmt.entities.User
import org.springframework.data.r2dbc.repository.R2dbcRepository

interface UserRepository : R2dbcRepository<User, Long> {
}