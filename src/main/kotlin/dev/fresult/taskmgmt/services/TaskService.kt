package dev.fresult.taskmgmt.services

import dev.fresult.taskmgmt.entities.Task
import dev.fresult.taskmgmt.repositories.TaskRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class TaskService(private val repository: TaskRepository) : BaseService<Task, Long> {
  override fun all(): Flux<Task> {
    return repository.findAll()/*.delayElements(Duration.ofMillis(300))*/
      // TODO: Remove log
      .doOnEach { println(it) }
  }

  override fun byId(id: Long): Mono<Task> = repository.findById(id)

  override fun create(task: Task): Mono<Task> = repository.save(task)
    // TODO: Remove log
    .doOnEach { println("created: ${it.get()}") }

  override fun update(id: Long, task: Task): Mono<Task> {
    val taskToUpdate = task.copy(id = id)
    println("toUpdated $taskToUpdate")
    return repository.save(taskToUpdate)
      // TODO: Remove log
      .doOnEach { println("updated: ${it.get()}") }
  }

  override fun deleteById(id: Long): Mono<Void> {
    return repository.deleteById(id)
  }
}
