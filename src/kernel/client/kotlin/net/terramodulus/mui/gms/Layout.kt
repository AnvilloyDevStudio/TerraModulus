/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.mui.gms

import net.terramodulus.mui.gfx.RectangleF
import net.terramodulus.mui.gms.impl.SequenceLayout.Element
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
	 * Should not rely on indices in the [Layout] since they are not meaningful.
	 */
	abstract class Group(container: Container) : Layout(container) {
		/**
		 * Checks if the specified [component] is contained in this layout.
		 * One should not rely on this function for efficiency and effectiveness.
		 */
		abstract fun contains(component: Component): Boolean

		/**
		 * Adds the [component] to this layout.
		 */
		abstract fun add(component: Component)

		/**
		 * Removes the [component] from this layout.
		 * Any error may occur if `target` does not exist in the layout.
		 */
		abstract fun remove(component: Component)

		/**
		 * Adds the [component] just before the [target] in this layout.
		 * Any error may occur if `target` does not exist in the layout.
		 */
		abstract fun addBefore(target: Component, component: Component)

		/**
		 * Adds the [component] just after the [target] in this layout.
		 * Any error may occur if `target` does not exist in the layout.
		 */
		abstract fun addAfter(target: Component, component: Component)

		/**
		 * Replaces the [target] in this layout by the [component].
		 * Any error may occur if `target` does not exist in the layout.
		 */
		abstract fun replace(target: Component, component: Component)
	}

	abstract class ElementGroup<E : Any> protected constructor(
		container: Container,
		protected val elements: ElementList<E>,
	) : Group(container) {
		final override val components = elements.componentsView

		override fun contains(component: Component): Boolean = elements.contains(component)

		/**
		 * Adds the [component] with the default element to this layout.
		 */
		abstract override fun add(component: Component)

		/**
		 * Adds the [component] with the [element] to this layout.
		 */
		open fun add(component: Component, element: E) = elements.add(component, element)

		override fun remove(component: Component) = elements.remove(component)

		/**
		 * Adds the [component] with the default element just before the [target] in this layout.
		 * Any error may occur if `target` does not exist in the layout.
		 */
		abstract override fun addBefore(target: Component, component: Component)

		/**
		 * Adds the [component] with the default element just after the [target] in this layout.
		 * Any error may occur if `target` does not exist in the layout.
		 */
		abstract override fun addAfter(target: Component, component: Component)

		/**
		 * Replaces the [target] in this layout by the [component] reusing `target`'s `element`.
		 * Any error may occur if `target` does not exist in the list.
		 */
		open fun replaceKeep(target: Component, component: Component) =
			elements.replaceKeep(target, component)

		/**
		 * Replaces the [target] in this layout by the [component] with the default element.
		 * Any error may occur if `target` does not exist in the layout.
		 */
		abstract override fun replace(target: Component, component: Component)

		/**
		 * Replaces the [target] in this layout by the [component] with the [element].
		 * Any error may occur if `target` does not exist in the layout.
		 */
		open fun replace(target: Component, component: Component, element: E) =
			elements.replace(target, component, element)
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

		/**
		 * Checks if the specified [component] is contained in this list.
		 * One should not rely on this function for efficiency and effectiveness.
		 */
		fun contains(component: Component): Boolean = components.contains(component)

		/**
		 * Adds the [component] with the [element] to this list.
		 */
		fun add(component: Component, element: E) {
			components.add(component)
			elementMap[component] = element
		}

		/**
		 * Removes the [component] from this list.
		 */
		fun remove(component: Component) {
			components.remove(component)
			elementMap.remove(component)
		}

		/**
		 * Adds the [component] with the [element] just before the [target] in this list.
		 * Any error may occur if `target` does not exist in the list.
		 */
		fun addBefore(target: Component, component: Component, element: E) {
			components.add(components.indexOf(target), component)
			elementMap[component] = element
		}

		/**
		 * Adds the [component] with the [element] just after the [target] in this list.
		 * Any error may occur if `target` does not exist in the list.
		 */
		fun addAfter(target: Component, component: Component, element: E) {
			components.add(components.indexOf(target) + 1, component)
			elementMap[component] = element
		}

		/**
		 * Replaces the [target] in this list by the [component] reusing `target`'s `element`.
		 * Any error may occur if `target` does not exist in the list.
		 */
		fun replaceKeep(target: Component, component: Component) {
			components.add(components.indexOf(target), component)
			elementMap[component] = elementMap[target]!!
			remove(target)
		}

		/**
		 * Replaces the [target] in this list by the [component] with the [element].
		 * Any error may occur if `target` does not exist in the list.
		 */
		fun replace(target: Component, component: Component, element: E) {
			components.add(components.indexOf(target), component)
			remove(target)
			elementMap[target] = element
		}
	}
}
