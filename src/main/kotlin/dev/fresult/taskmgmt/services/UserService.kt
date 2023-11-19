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

  override fun deleteById(id: Long): Mono<Void> {
    TODO("Not yet implemented")
  }

  override fun update(id: Long, user: User): Mono<User> {
    TODO("Not yet implemented")
  }

  override fun create(user: User): Mono<User> {
    TODO("Not yet implemented")
  }

  override fun byId(id: Long): Mono<User> {
    TODO("Not yet implemented")
  }

}