package dev.fresult.taskmgmt.services

import dev.fresult.taskmgmt.entities.BaseEntity
import dev.fresult.taskmgmt.entities.Task
import dev.fresult.taskmgmt.repositories.TaskRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class TaskService(private val repository: TaskRepository) : BaseService<Task, Long> {
  override fun all(): Flux<Task> = repository.findAll()
    /*.delayElements(Duration.ofMillis(300))*/
    // TODO: Remove log
    .doOnEach { println(it) }

  override fun byId(id: Long): Mono<Task> = repository.findById(id)

  override fun create(task: Task): Mono<Task> {
    println("before Create $task")

    return repository.save(task)
      // TODO: Remove log
      .doOnEach { println("created: ${it.get()}") }
  }

  override fun update(id: Long, task: Task): Mono<Task> {
    println("toUpdated $task")

    return repository.save(task)
      // TODO: Remove log
      .doOnEach { println("updated: ${it.get()}") }
  }

  override fun deleteById(id: Long): Mono<Void> {
    return repository.deleteById(id)
  }

  val copy: (Task) -> (Task) -> Task = { existingTask ->
    { originalTask ->
      originalTask.copy(
        id = existingTask.id,
        version = existingTask.version,
        createdAt = existingTask.createdAt,
        updatedAt = existingTask.updatedAt,
      )
    }
  }
}
