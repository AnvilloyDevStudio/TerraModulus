/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.mui.gms

import terramodulus.mui.gms.event.ScreenEvent

abstract class Screen {
	private val listeners = HashMap<Class<out ScreenEvent>, LinkedHashSet<(ScreenEvent) -> Unit>>()
	private val menus = LinkedHashSet<Menu>()

	fun <T: ScreenEvent> addListener(e: Class<T>, l: (T) -> Unit) {
		@Suppress("UNCHECKED_CAST")
		listeners.computeIfAbsent(e) { LinkedHashSet() }.add(l as (ScreenEvent) -> Unit)
	}

	fun <T: ScreenEvent> removeListener(e: Class<T>, l: (T) -> Unit) {
		listeners[e]?.remove(l)
	}

	/**
	 * Cleans up and closes any used resource here.
	 */
	abstract fun exit()
}
