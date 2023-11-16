package dev.fresult.taskmgmt

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TaskMgmtApplication

fun main(args: Array<String>) {
	runApplication<TaskMgmtApplication>(*args)
}
