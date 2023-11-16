package dev.fresult.taskmgmt.routers

import dev.fresult.taskmgmt.handlers.TaskHandler
import org.springframework.context.annotation.Configuration

@Configuration
class TaskRouter(private val taskController: TaskHandler) {
}