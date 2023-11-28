package dev.fresult.taskmgmt.services

import dev.fresult.taskmgmt.dtos.TaskQueryParamValues
import dev.fresult.taskmgmt.entities.Task
import dev.fresult.taskmgmt.entities.TaskStatus
import dev.fresult.taskmgmt.repositories.TaskRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDate

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

  override fun update(id: Long) = { task: Task ->
    println("toUpdated $task")

    repository.save(task)
      // TODO: Remove log
      .doOnEach { println("updated: ${it.get()}") }
  }

override fun deleteById(id: Long): Mono<Void> {
  return repository.deleteById(id)
}

fun allByQueryParams(queryParams: TaskQueryParamValues): Flux<Task> {
  val (dueDates, statuses, createdBy, updatedBy) = queryParams

  return repository.findAllByConditions(
    defaultFirstLong(createdBy),
    defaultFirstLong(updatedBy),
    defaultFirstItem(::firstItemLocalDate)(dueDates),
    defaultFirstItem(::firstItemStatus)(statuses),
    createdByIsNull = createdBy.isNullOrEmpty(),
    updatedByIsNull = updatedBy.isNullOrEmpty(),
    dueDatesIsNull = dueDates.isNullOrEmpty(),
    statusesIsNull = statuses.isNullOrEmpty(),
  )
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

private fun <T> defaultFirstItem(default: () -> List<T>): (List<T>?) -> List<T> = { list ->
  list.orEmpty().ifEmpty(default)
}

private fun firstItemLong(): List<Long> = listOf(-1)
private val defaultFirstLong = defaultFirstItem(::firstItemLong)
private fun firstItemLocalDate(): List<LocalDate> = listOf(LocalDate.now())
private fun firstItemStatus(): List<TaskStatus> = listOf(TaskStatus.PENDING)
}
