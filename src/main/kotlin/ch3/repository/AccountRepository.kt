package ch3.repository

import ch3.algebra.interpreter.Account
import ch3.algebra.interpreter.Balance
import org.funktionale.option.Option
import org.funktionale.option.Option.None
import org.funktionale.option.Option.Some
import org.funktionale.option.toOption
import org.funktionale.tries.Try
import org.funktionale.tries.Try.Failure
import org.funktionale.tries.Try.Success
import java.util.*


abstract class AccountRepository : Repository<Account, String>() {

    abstract override fun query(no: String): Try<Option<Account>>
    abstract override fun store(a: Account): Try<Account>

    fun balance(no: String): Try<Balance> = query(no).flatMap { a ->
        when (a) {
            is Some -> Success(a.get().balance)
            is None -> Failure(Exception("No account exists with no $no"))
            else -> Failure<Balance>(Throwable())
        }
    }

    abstract fun query(openedOn: Date): Try<List<Account>>
}

class AccountRepositoryInMemory : AccountRepository() {
    val repo: MutableMap<String, Account> = HashMap()

    override fun query(no: String): Try<Option<Account>> {
        val r = repo[no]
        return Success(r.toOption())
    }

    override fun store(a: Account): Try<Account> {
        repo.put(a.no, a)
        return Success(a)
    }

    override fun query(openedOn: Date): Try<List<Account>> =
            Success(repo.values.filter { it -> it.dateOfOpening == openedOn })

}
