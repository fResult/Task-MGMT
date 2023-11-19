package dev.fresult.taskmgmt.entities

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import java.time.Instant

//open class BaseEntity(open val id: Long? = null) {
abstract class BaseEntity(open val id: Long? = null) {
  /**
   * ```kt
   * @CreatedDate
   * override val createdAt: Instant? = null
   * ```
   */
  abstract val createdAt: Instant?

  /**
   * ```kt
   * @LastModifiedDate
   * override val updatedAt: Instant? = null,
   * ```
   */
  abstract val updatedAt: Instant?

  /**
   * ```kt
   * @Version
   * override val version: Int? = 1,
   * ```
   */
  abstract val version: Int?
}
