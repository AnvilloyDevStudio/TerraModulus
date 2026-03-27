/*
 * SPDX-FileCopyrightText: 2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.util

import net.terramodulus.util.TypedAnchorMap.Key
import java.util.function.BiFunction
import java.util.function.Function

/**
 * A map with access by references to unique keys with type of values specified,
 * matching the pattern of **Typed Anchor Dynamic Mapping**.
 * Localized constrains may be applied by copying this universal structure.\
 * All bulk functions are all unsupported for type safety.
 * @param T base class for map values
 */
open class TypedAnchorMap<T : Any> private constructor(private val map: MutableMap<Key<*>, Any?>) : MutableMap<Key<*>, Any?> by map {
	constructor() : this(HashMap())

	companion object {
		fun default() = TypedAnchorMap<Any>()
	}

	/**
	 * A unique, immutable key that defines and enforces the type for values of a [TypedAnchorMap].
	 * By hiding public access to the map instance, constrains may be applied by subclassing this.
	 */
	open class Key<U : Any>(val type: Class<U>) {
		companion object {
			inline operator fun <reified T : Any> invoke() = Key(T::class.java)
		}
	}

	@Suppress("UNCHECKED_CAST") // impossible, as constrained by #put
	fun <V: T> get(key: Key<V>) = map[key] as T?

	@Suppress("UNCHECKED_CAST") // impossible, as constrained by #put
	fun <V: T> getOrDefault(key: Key<V>, defaultValue: V) = super.getOrDefault(key, defaultValue) as T?

	fun <V: T> put(key: Key<V>, value: V) = map.put(key, value)

	override fun putAll(from: Map<out Key<*>, Any?>) = throw UnsupportedOperationException()

	fun <V: T> putIfAbsent(key: Key<V>, value: V) = super.putIfAbsent(key, value)

	fun <V: T> replace(key: Key<V>, oldValue: V, newValue: V) = super.replace(key, oldValue, newValue)

	fun <V: T> replace(key: Key<V>, value: V): Any? {
		return super.replace(key, value)
	}

	override fun replaceAll(function: BiFunction<in Key<*>, in Any?, out Any?>) = throw UnsupportedOperationException()

	override fun compute(key: Key<*>, remappingFunction: BiFunction<in Key<*>, in Any?, out Any?>) = throw UnsupportedOperationException()

	override fun computeIfAbsent(key: Key<*>, mappingFunction: Function<in Key<*>, out Any?>) = throw UnsupportedOperationException()

	override fun computeIfPresent(key: Key<*>, remappingFunction: BiFunction<in Key<*>, in Any, out Any?>) = throw UnsupportedOperationException()

	override fun merge(key: Key<*>, value: Any, remappingFunction: BiFunction<in Any, in Any, out Any?>) = throw UnsupportedOperationException()
}
