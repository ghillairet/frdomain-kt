package ch3.repository.partial

import ch3.algebra.interpreter.Account
import ch3.algebra.interpreter.Amount
import ch3.algebra.interpreter.Balance
import ch3.algebra.interpreter.common
import ch3.repository.AccountRepository
import org.funktionale.option.Option
import org.funktionale.option.Option.None
import org.funktionale.option.Option.Some
import org.funktionale.option.getOrElse
import org.funktionale.tries.Try
import org.funktionale.tries.Try.Failure
import java.util.*

abstract class AccountService<Account, in Amount, Balance> {
    abstract fun open(no: String, name: String, openingDate: Option<Date>): (AccountRepository) -> Try<Account>
    abstract fun close(no: String, closeDate: Option<Date>): (AccountRepository) -> Try<Account>
    abstract fun debit(no: String, amount: Amount): (AccountRepository) -> Try<Account>
    abstract fun credit(no: String, amount: Amount): (AccountRepository) -> Try<Account>
    abstract fun balance(no: String): (AccountRepository) -> Try<Balance>
}

object AccountServicePartialImpl : AccountService<Account, Amount, Balance>() {

    override fun open(no: String, name: String, openingDate: Option<Date>): (AccountRepository) -> Try<Account> = { repo: AccountRepository ->
        repo.query(no).flatMap { a ->
            when (a) {
                is Some<Account> -> Failure<Account>(Throwable("Fail"))
                is None ->
                    if (no.isEmpty() || name.isEmpty())
                        Failure<Account>(Throwable("Account no or name cannot be blank"))
                    else if (openingDate.getOrElse { common.today }.before(common.today))
                        Failure<Account>(Throwable("Cannot open account in the past"))
                    else
                        repo.store(Account(no, name, openingDate.getOrElse { common.today }))
            }
        }
    }

    override fun close(no: String, closeDate: Option<Date>): (AccountRepository) -> Try<Account> = { repo: AccountRepository ->
        repo.query(no).flatMap { a ->
            when (a) {
                is Some<Account> ->
                    if (closeDate.getOrElse { common.today }.before(a.get().dateOfOpening))
                        Failure<Account>(Throwable("Close date $closeDate cannot be before opening date ${a.get().dateOfOpening}"))
                    else
                        repo.store(a.get().copy(dateOfClosing = closeDate))
                is None -> Failure(Throwable("Account not found with $no"))
            }
        }
    }

    override fun debit(no: String, amount: Amount): (AccountRepository) -> Try<Account> = { repo: AccountRepository ->
        repo.query(no).flatMap { a ->
            when (a) {
                is Some<Account> ->
                    if (a.get().balance.amount < amount)
                        Failure(Throwable("Insufficient balance"))
                    else repo.store(a.get().copy(balance = Balance(a.get().balance.amount - amount)))
                else -> Failure<Account>(Throwable("Account not found with $no"))
            }
        }
    }

    override fun credit(no: String, amount: Amount): (AccountRepository) -> Try<Account> = { repo: AccountRepository ->
        repo.query(no).flatMap { a ->
            when (a) {
                is Some<Account> -> repo.store(a.get().copy(balance = Balance(a.get().balance.amount + amount)))
                is None -> Failure<Account>(Throwable("Account not found with $no"))
            }
        }
    }

    override fun balance(no: String): (AccountRepository) -> Try<Balance> = { repo: AccountRepository -> repo.balance(no) }

}

fun main(args: Array<String>) {

}