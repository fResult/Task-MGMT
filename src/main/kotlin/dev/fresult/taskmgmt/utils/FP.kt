package dev.fresult.taskmgmt.utils

infix fun <A1, A2, R> ((A1) -> A2).then(other: (A2) -> R): (A1) -> R {
  return { other(this(it)) }
}
