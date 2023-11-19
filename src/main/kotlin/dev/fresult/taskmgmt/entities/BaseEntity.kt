package dev.fresult.taskmgmt.entities

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.time.LocalDateTime

open class BaseEntity(open val id: Long? = null) {
  @CreatedDate
  var createdAt: Instant? = null

  @LastModifiedDate
  var updatedAt: Instant? = null

  @Version
  var version: Int? = null
}
