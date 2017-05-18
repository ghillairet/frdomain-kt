package ch3.lens

import org.funktionale.composition.andThen

data class Lens<O, V>(val get: (O) -> V, val set: (O, V) -> O) {
    companion object {
        fun <Outer, Inner, Value> compose(outer: Lens<Outer, Inner>, inner: Lens<Inner, Value>) = Lens(
                get = outer.get andThen inner.get,
                set = { obj, value -> outer.set(obj, inner.set(outer.get(obj), value)) }
        )
    }
}
