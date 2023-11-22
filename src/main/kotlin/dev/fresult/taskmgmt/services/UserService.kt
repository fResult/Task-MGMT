package dev.fresult.taskmgmt.services

import dev.fresult.taskmgmt.entities.User
import dev.fresult.taskmgmt.repositories.UserRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class UserService(private val repository: UserRepository) : BaseService<User, Long> {
  override fun all(): Flux<User> = repository.findAll()
    // TODO: Remove log
    .doOnEach { println(it) }

  override fun byId(id: Long): Mono<User> = repository.findById(id)

  override fun create(user: User): Mono<User> = repository.save(user)
    // TODO: Remove log
    .doOnEach { println("created: ${it.get()}") }

  override fun update(id: Long, user: User): Mono<User> = repository.save(user)
    // TODO: Remove log
    .doOnEach { println("updated: ${it.get()}") }

  override fun deleteById(id: Long): Mono<Void> = repository.deleteById(id)

  fun existsById(id: Long) = repository.existsById(id)

  val copy: (User) -> (User) -> User = { existingUser ->
    { user ->
      user.copy(
        id = existingUser.id,
        version = existingUser.version,
        createdAt = existingUser.createdAt,
        updatedAt = existingUser.updatedAt,
      )
    }
  }
}
