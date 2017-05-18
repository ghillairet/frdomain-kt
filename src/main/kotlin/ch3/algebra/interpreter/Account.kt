package ch3.algebra.interpreter

import org.funktionale.option.Option
import java.math.BigDecimal
import java.util.*

typealias Amount = BigDecimal

object common {
    val today: Date = Calendar.getInstance().time
}

data class Balance(val amount: Amount = BigDecimal(0))

data class Account(val no: String,
                   val name: String,
                   val dateOfOpening: Date = common.today,
                   val dateOfClosing: Option<Date> = Option.None,
                   val balance: Balance = Balance(BigDecimal(0)))

