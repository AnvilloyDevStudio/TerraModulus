/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.mui.gms

import java.util.ArrayDeque

abstract class Menu {
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
