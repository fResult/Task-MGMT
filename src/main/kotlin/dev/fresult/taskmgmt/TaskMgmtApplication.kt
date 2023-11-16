package dev.fresult.taskmgmt

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.runApplication
import org.springframework.context.event.EventListener
import org.springframework.core.env.Environment

@SpringBootApplication(/*exclude = [DataSourceAutoConfiguration::class]*/)
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
