package dev.fresult.taskmgmt.repositories

import dev.fresult.taskmgmt.entities.Task
import dev.fresult.taskmgmt.entities.TaskStatus
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.data.repository.query.Param
import reactor.core.publisher.Flux
import java.time.LocalDate

interface TaskRepository : R2dbcRepository<Task, Long> {
  //  @Query(
//    """
//    SELECT * FROM tasks
//    WHERE (COALESCE(:#{#createdBy[0]}, NULL) IS NULL OR created_by IN (:createdBy))
//      AND (COALESCE(:#{#updatedBy[0]}, NULL) IS NULL OR updated_by IN (:updatedBy))
//      AND (COALESCE(:#{#dueDates[0]}, NULL) IS NULL OR due_date IN (:dueDates))
//      AND (COALESCE(:#{#statuses[0]}, NULL) IS NULL OR status IN (:statuses))
//    """
//  )
  @Query(
    """
    SELECT * FROM tasks
          /* :createdBy IS NULL */
    WHERE (:createdByIsNull OR created_by IN (:createdBy))
          /* :updatedBy IS NULL */
      AND (:updatedByIsNull OR updated_by IN (:updatedBy))
          /* :dueDate IS NULL */
      AND (:dueDatesIsNull OR due_date IN (:dueDates))
          /* :status IS NULL */
      AND (:statusesIsNull OR status IN (COALESCE(:statuses, 'PENDING')))
    """
  )
  fun findAllByConditions(
    @Param("createdBy") createdBy: List<Long>,
    @Param("updatedBy") updatedBy: List<Long>,
    @Param("dueDates") dueDates: List<LocalDate>,
    @Param("statuses") statuses: List<TaskStatus>,
    /**```kt
     * queryParams.createdByUsers.isNullOrEmpty()
     * ```
     */
    createdByIsNull: Boolean,
    /**```kt
     * queryParams.updatedByUsers.isNullOrEmpty()
     * ```
     */
    updatedByIsNull: Boolean,
    /**```kt
     * queryParams.dueDates.isNullOrEmpty()
     * ```
     */
    dueDatesIsNull: Boolean,
    /**```kt
     * queryParams.statuses.isNullOrEmpty()
     * ```
     */
    statusesIsNull: Boolean,
  ): Flux<Task>
}
