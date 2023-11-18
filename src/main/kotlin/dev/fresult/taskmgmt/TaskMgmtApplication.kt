package dev.fresult.taskmgmt

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.runApplication
import org.springframework.context.event.EventListener
import org.springframework.core.env.Environment
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@SpringBootApplication(/*exclude = [DataSourceAutoConfiguration::class]*/)
@EnableR2dbcRepositories
class TaskMgmtApplication(private val environment: Environment) {

  @EventListener(ApplicationReadyEvent::class)
  fun printPropertyValue() {
    val yourPropertyValue = environment.getProperty("spring.r2dbc.url")
    println("Property Value: $yourPropertyValue")
  }
}

fun main(args: Array<String>) {
  runApplication<TaskMgmtApplication>(*args)
}
