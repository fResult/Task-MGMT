package dev.fresult.taskmgmt.repositories

import dev.fresult.taskmgmt.entities.Task
import dev.fresult.taskmgmt.entities.TaskStatus
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.time.LocalDate

interface TaskRepository : R2dbcRepository<Task, Long> {
//  @Query(
//    "SELECT * FROM tasks " +
//        "WHERE user_id = :userId " +
//        // AND ( :'dueDate' IS NULL OR due_date = :'dueDate )'
//        "AND ( :#{conditions[0]?.second.orElse(null)} IS NULL " +
//        "OR #conditions[0].first = :#{conditions[0]?.second.orElse(null)} )" +
//        // AND ( :'status' IS NULL OR status = :'status' )'
//        "AND ( :#{conditions[0]?.second.orElse(null)} IS NULL " +
//        "OR #conditions[0].first = :#{conditions[0]?.second.orElse(null)} );"
//
//  )
//  fun findAllByUserId(userId: Long, conditions: TaskConditions): Flux<Task>

  @Query(
    "SELECT * FROM tasks " +
        "WHERE user_id = :userId " +
        "AND (:dueDate IS NULL OR due_date = :dueDate) " +
        "AND (:status IS NULL OR status = :status);"
//        "AND (:createdBy IS NULL OR created_by = :createdBy) "
//        "AND (:updatedBy IS NULL OR updated_by = :updatedBy);"
  )
  fun findAllByUserId(
    userId: Long,
    dueDate: LocalDate? = null,
    status: TaskStatus? = null,
//    createdBy: Long? = 1,
//    updatedBy: Long? = null,
  ): Flux<Task>
}

@Repository
class TaskRepositoryConcrete(private val dbClient: DatabaseClient) {
  // TODO: Fix error that cannot query by Query Params yet
//  fun findAllByUserId(userId: Long, conditions: List<TaskConditions>): Flux<Task> {
//
//    val filteredConditions = conditions.filter { cond ->
//      println("cond: $cond")
//      cond.third.isPresent && (cond.third.get() != "")
//    }
//    val criteria = filteredConditions.joinToString(" AND ") { cond -> "${cond.first} = :${cond.second}" }
//
//    val noConditions = filteredConditions.isEmpty()
//    val conditionsToAppend = if (!noConditions) "AND $criteria" else ""
//    println("conditionsToAppend: $conditionsToAppend")
//
//    val sql = dbClient.sql(
//      "SELECT * FROM tasks " +
//          "WHERE user_id = :userId $conditionsToAppend" +
//          // TODO: Remove this line if finish testing
//          "AND due_date = :dueDate "
//    )
//
//    filteredConditions.forEach { cond ->
//      sql.bind(cond.second, cond.third.get())
//    }
//
//    return sql.bind("userId", userId)
//      // TODO: Remove this line if finish testing
//      .bind("dueDate", "2024-11-20")
//      .map { row, _ ->
//      Task(
//        id = row.get("id", Integer::class.java)?.toLong() ?: -1,
//        title = row.get("title", String::class.java) ?: "",
//        description = row.get("description", String::class.java),
//        dueDate = row.get("due_date", LocalDate::class.java) ?: LocalDate.now(),
//        status = enumValueOf<TaskStatus>(row.get("status", String::class.java).toString()),
//        userId = row.get("user_id", Integer::class.java)?.toLong() ?: -1,
//        createdAt = row.get("created_at", Instant::class.java),
//        updatedAt = row.get("updated_at", Instant::class.java),
//        version = row.get("version", Integer::class.java)?.toInt() ?: -1
//      )
//    }.all()
//  }
}
