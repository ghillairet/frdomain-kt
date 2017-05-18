package algebra

import ch3.algebra.interpreter.AccountService.credit
import ch3.algebra.interpreter.AccountService.open
import ch3.algebra.interpreter.AccountService.transfer
import java.math.BigDecimal

fun main(args: Array<String>) {
    val a1 = open("1", "Bob")
    val a2 = open("2", "Paul")

    val aa1 = a1.flatMap { a ->
        credit(a, BigDecimal(100))
    }

    val aa2 = a2.flatMap { a ->
        credit(a, BigDecimal(200))
    }

    println(transfer(aa1.get(), aa2.get(), BigDecimal(80)))
}