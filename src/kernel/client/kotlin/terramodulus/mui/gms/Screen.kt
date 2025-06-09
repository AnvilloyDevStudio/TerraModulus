/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.mui.gms

import terramodulus.mui.gms.event.ScreenEvent
import java.util.ArrayDeque

abstract class Screen {
	private val listeners = HashMap<Class<out ScreenEvent>, LinkedHashSet<(ScreenEvent) -> Unit>>()
	private val menus = LinkedHashSet<Menu>()
	private val components = LinkedHashSet<Component>()
	private val componentQueue = ArrayDeque<ComponentOperation>()
	private val menuQueue = ArrayDeque<MenuOperation>()
	val handle: Handle = HandleImpl()

	private sealed interface ComponentOperation {
		class Add(val component: () -> Component) : ComponentOperation

		class Remove(val component: Component) : ComponentOperation
	}

	private sealed interface MenuOperation {
		class Add(val menu: () -> Menu) : MenuOperation

		class Remove(val menu: Menu) : MenuOperation
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

	/**
	 * It is strongly suggested only using this function during initialization.
	 */
	protected fun addMenu(menu: Menu) {
		menus.add(menu)
	}

	/**
	 * It is strongly suggested only using this function during initialization.
	 */
	protected fun removeMenu(menu: Menu) {
		menus.remove(menu)
	}

	fun <T: ScreenEvent> addListener(e: Class<T>, l: (T) -> Unit) {
		@Suppress("UNCHECKED_CAST")
		listeners.computeIfAbsent(e) { LinkedHashSet() }.add(l as (ScreenEvent) -> Unit)
	}

	fun <T: ScreenEvent> removeListener(e: Class<T>, l: (T) -> Unit) {
		listeners[e]?.remove(l)
	}

	sealed interface Handle {
		fun addComponent(component: () -> Component)

		fun removeComponent(component: Component)

		fun addMenu(menu: () -> Menu)

		fun removeMenu(menu: Menu)
	}

	private inner class HandleImpl : Handle {
		override fun addComponent(component: () -> Component) {
			componentQueue.add(ComponentOperation.Add(component))
		}

		override fun removeComponent(component: Component) {
			componentQueue.add(ComponentOperation.Remove(component))
		}

		override fun addMenu(menu: () -> Menu) {
			menuQueue.add(MenuOperation.Add(menu))
		}

		override fun removeMenu(menu: Menu) {
			menuQueue.add(MenuOperation.Remove(menu))
		}
	}

	/**
	 * Cleans up and closes any used resource here.
	 */
	abstract fun exit()
}
