package ch3.repository

import org.funktionale.option.Option
import org.funktionale.tries.Try

abstract class Repository<A, IdType> {
    abstract fun query(no: IdType): Try<Option<A>>
    abstract fun store(a: A): Try<A>
}