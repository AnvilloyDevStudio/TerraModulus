/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.mui.gms

import terramodulus.mui.gfx.RectangleF
import kotlin.reflect.KProperty

/**
 * [Layout] is always mutable.
 *
 * **Layout** is defined only when all its managed components all belong to the container
 * associated with this layout manager *exclusively*.
 */
abstract class Layout(private val container: Container) {
	companion object {
		const val ALIGN_START = 0F;
		const val ALIGN_CENTER = .5F;
		const val ALIGN_END = 1F;
	}

	abstract val components: Iterable<Component>

	private val containerObserver = ::layout.apply(container.rect::observe)

	/**
	 * Updates the layout output using the current layout configurations
	 * by invoking [layout] internally.
	 *
	 * It is recommended to invoke this when this layout is being initialized
	 * or any layout configuration has been changed.
	 */
	fun update() = layout(container.rect.rect)

	/**
	 * Lays out the managed [components] by this [Layout] manager.
	 *
	 * Only the `rect`s of the managed `components` should be (re)assigned;
	 * no other state-changing operations should be done beside this.
	 * @param rect the rectangle of the container at this moment
	 */
	protected abstract fun layout(rect: RectangleF)

	/**
	 * Must be invoked when this [Layout] is no longer in use.
	 */
	fun clear() {
		container.rect.unobserve(containerObserver)
	}

	/**
	 * @param components must not be empty
	 */
	protected class ComponentIterable(private vararg val components: KProperty<Component?>) : Iterable<Component> {
		override fun iterator(): Iterator<Component> = object : Iterator<Component> {
			private var index = 0

			private fun untilNotNull(): Boolean {
				do {
					if (components[index].getter.call() != null)
						return true
					else
						index++
				} while (index < components.size)
				return false
			}

			override fun hasNext(): Boolean = untilNotNull()

			override fun next(): Component = if (untilNotNull()) {
				components[index].getter.call()!!
			} else {
				throw NoSuchElementException()
			}
		}
	}

	/**
	 * Internal list of the layout elements.
	 */
	protected class ElementList<E : Any> private constructor(
		private val components: MutableList<Component>, // order is defined here
		private val elementMap: MutableMap<Component, E>, // elements are mapped here
	) : Iterable<Pair<Component, E>> {
		companion object {
			fun <E : Any> withComponentsDefault(default: () -> E, vararg components: Component): ElementList<E> {
				val map = hashMapOf<Component, E>()
				components.forEach { map[it] = default() }
				return ElementList(mutableListOf(*components), map)
			}

			fun <E : Any> withComponentsDefault(default: () -> E, components: Collection<Component>): ElementList<E> {
				val map = hashMapOf<Component, E>()
				components.forEach { map[it] = default() }
				return ElementList(ArrayList(components), map)
			}

			fun <E : Any> withElements(vararg elements: Pair<Component, E>): ElementList<E> =
				ElementList(elements.mapTo(ArrayList(elements.size)) { it.first }, hashMapOf(*elements))

			fun <E : Any> withElements(elements: Map<Component, E>): ElementList<E> =
				ElementList(elements.mapTo(ArrayList(elements.size)) { it.key }, HashMap(elements))
		}

		val componentsView: Collection<Component> = components

		override fun iterator(): Iterator<Pair<Component, E>> = object : Iterator<Pair<Component, E>> {
			private val it = components.iterator()

			override fun hasNext() = it.hasNext()

			override fun next(): Pair<Component, E> {
				val e = it.next()
				return e to elementMap[e]!!
			}
		}
	}
}
