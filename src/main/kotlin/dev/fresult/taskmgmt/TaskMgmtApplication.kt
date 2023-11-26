package dev.fresult.taskmgmt

import dev.fresult.taskmgmt.entities.Task
import dev.fresult.taskmgmt.entities.TaskStatus
import dev.fresult.taskmgmt.entities.User
import dev.fresult.taskmgmt.repositories.TaskRepository
import dev.fresult.taskmgmt.repositories.UserRepository
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.runApplication
import org.springframework.context.event.EventListener
import org.springframework.core.env.Environment
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import reactor.core.publisher.Flux
import reactor.kotlin.core.publisher.toFlux
import java.time.Duration
import java.time.LocalDate
import kotlin.reflect.KClass

@SpringBootApplication(/*exclude = [DataSourceAutoConfiguration::class]*/)
@EnableR2dbcRepositories
class TaskMgmtApplication(private val environment: Environment) {

  @EventListener(ApplicationReadyEvent::class)
  fun printPropertyValue() {
    val yourPropertyValue = environment.getProperty("spring.r2dbc.url")
    println("Property Value: $yourPropertyValue")

    val usersToCreate = Flux.just(
      User(null, "john.d@example.com", "H@shedP@ssw0rd1", "John", "Doe"),
      User(null, "john.w@example.com", "H@shedP@ssw0rd2", "John", "Wick"),
      User(null, "john.c@example.com", "H@shedP@ssw0rd3", "John", "Constantine"),
    )

    val tasksToCreate = Flux.just(
      Task(null, "Task-1", "Task 1's Description", LocalDate.of(2024, 11, 21), TaskStatus.PENDING, 1, 1),
      Task(null, "Task-2", "Task 2's Description", LocalDate.of(2024, 11, 20), TaskStatus.PENDING, 2, 2),
      Task(null, "Task-3", "Task 3's Description", LocalDate.of(2024, 11, 21), TaskStatus.IN_PROGRESS, 1, 1),
      Task(null, "Task-4", "Task 4's Description", LocalDate.of(2024, 11, 20), TaskStatus.IN_PROGRESS, 3, 3),
      Task(null, "Task-5", "Task 5's Description", LocalDate.of(2024, 11, 21), TaskStatus.COMPLETED, 1, 1),
      Task(null, "Task-6", "Task 6's Description", LocalDate.of(2024, 11, 22), TaskStatus.PENDING, 2, 2),
      Task(null, "Task-7", "Task 7's Description", LocalDate.of(2024, 11, 22), TaskStatus.COMPLETED, 2, 2),
    )

    val printCreateUserErrorMsg = printCreateErrorMessage(User::class)
    val printCreateTaskErrorMsg = printCreateErrorMessage(Task::class)

    val saveAllTasksSubscription: () -> Unit = {
      taskRepo.saveAll(tasksToCreate).toFlux()
        .doOnEach { println("Create ${it.get()}") }
        .delayElements(Duration.ofMillis(500))
        .doOnError(printCreateTaskErrorMsg)
        .subscribe({}, printCreateTaskErrorMsg)
    }


    userRepo.saveAll(usersToCreate).toFlux()
      .doOnEach { println("Create ${it.get()}") }
      .delayElements(Duration.ofMillis(500))
      .doOnError(printCreateUserErrorMsg)
      .subscribe({}, printCreateUserErrorMsg, saveAllTasksSubscription)
  }

  @Autowired
  lateinit var userRepo: UserRepository

  var log: Logger = LogManager.getLogger()

  fun printCreateErrorMessage(clazz: KClass<*>) =
    { throwable: Throwable -> log.error("[Create ${clazz.simpleName}] :: ${throwable.cause?.message}") }

  @Autowired
  lateinit var taskRepo: TaskRepository
}

fun main(args: Array<String>) {
  runApplication<TaskMgmtApplication>(*args)
}
