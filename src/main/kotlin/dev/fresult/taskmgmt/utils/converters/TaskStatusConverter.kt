package dev.fresult.taskmgmt.utils.converters

import dev.fresult.taskmgmt.entities.TaskStatus
import org.springframework.core.convert.converter.Converter

class TaskStatusConverter : Converter<TaskStatus, String> {
  override fun convert(source: TaskStatus): String {
    return source.name
  }

  fun convertReversed(target: String): TaskStatus {
    return TaskStatus.valueOf(target)
  }
}