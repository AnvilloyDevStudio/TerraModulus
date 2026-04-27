/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.mui.gms

import net.terramodulus.mui.gms.event.MenuEvent
import java.util.ArrayDeque

abstract class Menu : Container {
	private val listeners = HashMap<Class<out MenuEvent>, LinkedHashSet<(MenuEvent) -> Unit>>()
	private val components = LinkedHashSet<Component>()
	private val componentQueue = ArrayDeque<ComponentOperation>()
	val handle: Handle = HandleImpl()

	private sealed interface ComponentOperation {
		class Add(val component: () -> Component) : ComponentOperation

		class Remove(val component: Component) : ComponentOperation
	}

	/**
	 * It is strongly suggested only using this function during initialization.
	 */
	protected fun addComponent(component: Component) {
		components.add(component)
	}

	/**
	 * It is strongly suggested only using this function during initialization.
	 */
	protected fun removeComponent(component: Component) {
		components.remove(component)
	}

	fun <T: MenuEvent> addListener(e: Class<T>, l: (T) -> Unit) {
		@Suppress("UNCHECKED_CAST")
		listeners.computeIfAbsent(e) { LinkedHashSet() }.add(l as (MenuEvent) -> Unit)
	}

	fun <T: MenuEvent> removeListener(e: Class<T>, l: (T) -> Unit) {
		listeners[e]?.remove(l)
	}

	internal fun dispatchEvent(event: MenuEvent) {
		listeners[event.javaClass]?.forEach { it(event) }
	}

	sealed interface Handle {
		fun addComponent(component: () -> Component)

		fun removeComponent(component: Component)
	}

	private inner class HandleImpl : Handle {
		override fun addComponent(component: () -> Component) {
			componentQueue.add(ComponentOperation.Add(component))
		}

		override fun removeComponent(component: Component) {
			componentQueue.add(ComponentOperation.Remove(component))
		}
	}

	abstract fun render()
}
