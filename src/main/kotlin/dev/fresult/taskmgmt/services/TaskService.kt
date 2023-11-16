package dev.fresult.taskmgmt.services

import dev.fresult.taskmgmt.entities.Task
import dev.fresult.taskmgmt.entities.TaskRequest
import dev.fresult.taskmgmt.repositories.TaskRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class TaskService(private val repository: TaskRepository) {
  fun all(): Flux<Task> {
    return repository.findAll().doOnEach {
      // TODO: Remove log
      println(it)
    }
  }

  fun byId(id: Long): Mono<Task> = repository.findById(id)

  fun create(taskToCreate: TaskRequest): Mono<Task> =
    repository.save(Task.fromTaskRequest(taskToCreate))
      // TODO: Remove log
      .doOnEach { println("created: ${it.get()}") }
}
