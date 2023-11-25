package dev.fresult.taskmgmt.services

import dev.fresult.taskmgmt.dtos.TaskQueryParamValues
import dev.fresult.taskmgmt.dtos.TaskQueryParams
import dev.fresult.taskmgmt.entities.Task
import dev.fresult.taskmgmt.repositories.TaskRepository
import dev.fresult.taskmgmt.repositories.TaskRepositoryConcrete
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class TaskService(
  private val repository: TaskRepository,
  private val repositoryConcrete: TaskRepositoryConcrete,
) : BaseService<Task, Long> {
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

  fun allByUserId(userId: Long, conditions: TaskQueryParams): Flux<Task> {
    val (dueDate, status, createdBy, updatedBy) = conditions

    return repository.findAllByUserId(
      userId,
      dueDate,
      status,
      // FIXME: createdBy is redundant with userId
//      createdBy,
//      updatedBy,
    )
      // TODO: Remove log
      .doOnEach { println("Task by userId [$userId]: ${it.get()}") }
  }

  fun allByQueryParams(taskQueryParams: TaskQueryParamValues): Flux<Task> {
    TODO("Not yet implemented")
  }

  val copyToUpdate: (Task) -> (Task) -> Task = { existingTask ->
    { task ->
      task.copy(
        id = existingTask.id,
        version = existingTask.version,
        createdAt = existingTask.createdAt,
        updatedAt = existingTask.updatedAt,
        createdBy = existingTask.createdBy
      )
    }
  }
}
