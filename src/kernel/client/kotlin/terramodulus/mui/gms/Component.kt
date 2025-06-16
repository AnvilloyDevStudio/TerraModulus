/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.mui.gms

import terramodulus.mui.gfx.ManagedRect
import terramodulus.mui.gfx.RenderSystem
import terramodulus.mui.gms.event.ComponentEvent

/**
 * [Component] can only be contained by only one [Container].
 *
 * It is an undefined behavior when the `Component` is contained repeatedly
 * or in different containers simultaneously.
 */
abstract class Component {
	private val listeners = HashMap<Class<out ComponentEvent>, LinkedHashSet<(ComponentEvent) -> Unit>>()

	/**
	 * This should only be modified by [Layout] managers.
	 */
	open lateinit var rect: ManagedRect
		internal set

	abstract fun render(renderSystem: RenderSystem)

	fun <T: ComponentEvent> addListener(e: Class<T>, l: (T) -> Unit) {
		@Suppress("UNCHECKED_CAST")
		listeners.computeIfAbsent(e) { LinkedHashSet() }.add(l as (ComponentEvent) -> Unit)
	}

	fun <T: ComponentEvent> removeListener(e: Class<T>, l: (T) -> Unit) {
		listeners[e]?.remove(l)
	}

	internal fun dispatchEvent(event: ComponentEvent) {
		listeners[event.javaClass]?.forEach { it(event) }
	}
}
