/*
 * SPDX-FileCopyrightText: 2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.mui.gui.asd

import net.terramodulus.mui.gui.agim.AgimoProperty
import net.terramodulus.mui.gui.gfx.RectangleF
import net.terramodulus.util.TypedAnchorMap
import java.util.function.BiFunction

/**
 * Since rectangles are modified only during layout processing,
 * further follow-ups must not be deferred to next tick.
 */
abstract class AsdHandle internal constructor() {
	/**
	 * Caveat: Must only be modified by [Layout][net.terramodulus.mui.gui.agim.Layout].
	 * When modified, [triggerRectObservers] must be invoked.
	 */
	abstract var rect: RectangleF
		internal set

	protected val rectObservers = LinkedHashSet<() -> Unit>()

	internal fun observeRect(observer: () -> Unit) {
		rectObservers.add(observer)
	}

	internal fun unobserveRect(observer: () -> Unit) {
		rectObservers.remove(observer)
	}

	internal fun triggerRectObservers() {
		rectObservers.forEach { it() }
	}

	protected val properties = TypedAnchorMap<AgimoProperty>()
	protected val propertyObservers = HashMap<PropertyKey<out AgimoProperty>,
		LinkedHashSet<(AgimoProperty?, AgimoProperty?) -> Unit>>()

	class PropertyKey<T : AgimoProperty>(c: Class<T>) : TypedAnchorMap.Key<T>(c) {
		override fun hashCode() = type.hashCode()
		override fun equals(other: Any?): Boolean {
			if (this === other) return true
			if (other !is PropertyKey<*>) return false
			return type == other.type
		}
	}

	fun <T : AgimoProperty> T.getAsdPropertyKey() = PropertyKey(javaClass)

	@Suppress("UNCHECKED_CAST")
	fun <T: AgimoProperty> getProperty(key: PropertyKey<T>) = properties.getTyped(key) as T?

	fun <T: AgimoProperty> putProperty(key: PropertyKey<T>, value: T) {
		@Suppress("UNCHECKED_CAST")
		val old = properties.putTyped(key, value) as T?
		triggerPropertyObservers(key, old, value)
	}

	@Suppress("UNCHECKED_CAST")
	fun <T: AgimoProperty> removeProperty(key: PropertyKey<T>): T? {
		val old = properties.remove(key) as T?
		triggerPropertyObservers(key, old, null)
		return old
	}

	fun <T: AgimoProperty> containsProperty(key: PropertyKey<T>) = properties.containsKey(key)

	/**
	 * @see java.util.Map.compute
	 */
	inline fun <T: AgimoProperty> computeProperty(key: PropertyKey<T>, remappingFunction: (PropertyKey<T>, T?) -> T?) {
		val v = remappingFunction(key, getProperty(key))
		if (v === null) removeProperty(key) else putProperty(key, v)
	}

	/**
	 * @see java.util.Map.computeIfAbsent
	 */
	inline fun <T: AgimoProperty> computePropertyIfAbsent(key: PropertyKey<T>, mappingFunction: (PropertyKey<T>) -> T?) {
		if (!containsProperty(key)) {
			val v = mappingFunction(key)
			if (v !== null) putProperty(key, v)
		}
	}

	/**
	 * @see java.util.Map.computeIfPresent
	 */
	inline fun <T: AgimoProperty> computePropertyIfPresent(key: PropertyKey<T>, remappingFunction: (PropertyKey<T>, T) -> T?) {
		val v = getProperty(key)
		if (v !== null) {
			val v = remappingFunction(key, v)
			if (v !== null) putProperty(key, v)
		}
	}

	/**
	 * @see java.util.Map.merge
	 */
	inline fun <T: AgimoProperty> mergeProperty(key: PropertyKey<T>, value: T, remappingFunction: (T, T) -> T?) {
		val v = getProperty(key)
		if (v !== null) {
			val v = remappingFunction(v, value)
			if (v !== null) putProperty(key, v)
		} else putProperty(key, value)
	}

	fun <T: AgimoProperty> observeProperty(key: PropertyKey<T>, l: (T?, T?) -> Unit) {
		@Suppress("UNCHECKED_CAST")
		propertyObservers.computeIfAbsent(key) { LinkedHashSet() }.add(l as (AgimoProperty?, AgimoProperty?) -> Unit)
	}

	fun <T: AgimoProperty> unobserveProperty(key: PropertyKey<T>, l: (T?, T?) -> Unit) {
		propertyObservers[key]?.remove(l)
	}

	protected fun <T: AgimoProperty> triggerPropertyObservers(key: PropertyKey<T>, old: T?, new: T?) {
		propertyObservers[key]?.forEach { it(old, new) }
	}

	/**
	 * Registers ASD Processors from AGIMOs to [AsdManager].
	 */
	abstract fun registerAsdProcessor(processor: AsdProcessor<*>)

	// TODO likely those below are useless
	abstract class Container : AsdHandle() {}

	abstract class Menu : Container() {}

	abstract class Screen : Container() {}

// 	interface Component : LayoutHandle {} // Do we need this?
}
