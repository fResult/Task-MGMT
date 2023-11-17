package dev.fresult.taskmgmt.services

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

interface BaseService<T, ID> {
  fun all(): Flux<T>
  fun byId(id: ID): Mono<T>
  fun create(item: T): Mono<T>
  fun update(id: ID, item: T): Mono<T>
  fun deleteById(id: Long): Mono<Void>
}