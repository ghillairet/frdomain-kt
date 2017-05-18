package ch3.lens

import ch3.lens.Lens.Companion.compose

data class Address(val no: String, val street: String, val city: String, val state: String, val zip: String)
data class Customer(val id: Int, val name: String, val address: Address)

val noLens = Lens<Address, String>(get = { it.no }, set = { obj, value -> obj.copy(no = value) })
val streetLens = Lens<Address, String>(get = { it.street }, set = { obj, value -> obj.copy(street = value) })
val cityLens = Lens<Address, String>(get = { it.city }, set = { obj, value -> obj.copy(city = value) })
val stateLens = Lens<Address, String>(get = { it.state }, set = { obj, value -> obj.copy(state = value) })
val zipLens = Lens<Address, String>(get = { it.zip }, set = { obj, value -> obj.copy(zip = value) })

val nameLens = Lens<Customer, Int>(get = { it.id }, set = { obj, value -> obj.copy(id = value) })
val addressLens = Lens<Customer, Address>(get = { it.address }, set = { o, v -> o.copy(address = v) })

fun main(args: Array<String>) {
    val a = Address(no = "B-12", street = "Monroe Street", city = "Denver", state = "CO", zip = "80231")
    val c = Customer(12, "John D Cook", a)

    val custAddrNoLens = compose(addressLens, noLens)
    println(custAddrNoLens.get(c))
    println(custAddrNoLens.set(c, "B675"))
}