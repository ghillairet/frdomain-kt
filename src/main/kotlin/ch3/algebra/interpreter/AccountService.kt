package ch3.algebra.interpreter

import org.funktionale.option.Option
import org.funktionale.tries.Try
import org.funktionale.tries.Try.Failure
import org.funktionale.tries.Try.Success
import java.util.*

object AccountService {

    fun open(no: String, name: String, openingDate: Optional<Date> = Optional.of(common.today)): Try<Account> =
            if (no.isEmpty() || name.isEmpty()) Failure(Exception())
            else if (openingDate.orElse(common.today).before(common.today)) Failure(Exception())
            else Success(Account(no, name, openingDate.orElse(common.today)))

    fun close(account: Account, closeDate: Optional<Date>): Try<Account> =
            if (closeDate.orElse(common.today).before(account.dateOfOpening))
                Failure(Exception())
            else Success(account.copy(dateOfClosing = Option.Some(closeDate.orElse(common.today))))

    fun debit(account: Account, amount: Amount): Try<Account> =
            if (account.balance.amount < amount)
                Failure(Exception())
            else Success(account.copy(balance = Balance(account.balance.amount - amount)))

    fun credit(account: Account, amount: Amount): Try<Account> =
            Success(account.copy(balance = Balance(account.balance.amount + amount)))

    fun balance(account: Account): Try<Balance> =
            Success(account.balance)

    fun transfer(from: Account, to: Account, amount: Amount): Try<Triple<Account, Account, Amount>> =
            debit(from, amount).flatMap { a ->
                credit(to, amount).map { b ->
                    Triple(a, b, amount)
                }
            }

}